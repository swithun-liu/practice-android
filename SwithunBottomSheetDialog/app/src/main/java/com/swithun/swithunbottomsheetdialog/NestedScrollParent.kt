package com.swithun.swithunbottomsheetdialog

import android.animation.ValueAnimator
import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.VelocityTracker
import android.view.View
import android.view.ViewConfiguration
import android.view.animation.OvershootInterpolator
import android.widget.TextView
import androidx.core.view.NestedScrollingParent3
import androidx.core.view.NestedScrollingParentHelper
import androidx.core.view.ViewCompat
import androidx.core.view.ViewCompat.NestedScrollType
import androidx.core.view.children
import androidx.customview.widget.ViewDragHelper.INVALID_POINTER
import kotlin.math.abs

class ParentNestedScrollView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ChildNestedScrollView(context, attrs, defStyleAttr), NestedScrollingParent3 {

    private var lastMotionYForInterceptTouchEvent = 0
    private var lastDownYForInterceptEvent: Int = 0

    // scroll下负
    private val state0Scroll
        get() = -height + 200


    val state1ParentTopDistance = 1000

    private val state1Scroll
        get() = -state1ParentTopDistance

    // scroll上正
    private val state2Scroll
        get() = 0

    private val stateList
        get() = listOf(
            state0Scroll, // -2691
            state1Scroll, // -1000
            state2Scroll // 0
        )

    private val openState: Pair<Int, Int>
        get() {
            var oldState = stateList.size - 1
            for (i in stateList.indices.reversed()) {
                if (scrollY >= stateList[i]) {
                    return i to oldState
                }
                oldState = i
            }
            return 0 to oldState
        }

    private val parentHelper by lazy {
        NestedScrollingParentHelper(this)
    }

    private val velocityTracker = VelocityTracker.obtain()

    // 第一个View
    private val firstView by lazy {
        children.first()
    }

    private var animateValue = AnimateValue(0, 0)

    private val overshootInterpolatorAnimator = ValueAnimator()
    private val overshootInterpolator = OvershootInterpolator(1f)
    private var activePointerId = INVALID_POINTER

    init {
        this.post {
        }
        overshootInterpolatorAnimator.addUpdateListener {
            val process = (it.animatedValue as Float)
            val overshootProcess = overshootInterpolator.getInterpolation(process)
            val passed = (animateValue.endY - animateValue.startY) * overshootProcess
            val newScrollY = animateValue.startY + passed
            Log.d(TAG, "[AUTO_SETTLE_ANIM] listener $process | $newScrollY")
            Log.d(
                TAG,
                "[Fling Animate] $process $overshootProcess | ${animateValue.endY}, ${animateValue.startY} | $passed $newScrollY"
            )
            this.scrollTo(scrollX, newScrollY.toInt())
        }
    }

    override fun onStartNestedScroll(child: View, target: View, axes: Int, type: Int): Boolean {
        return true
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        measureChildren(widthMeasureSpec, MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED))
    }

    override fun onNestedScrollAccepted(child: View, target: View, axes: Int, type: Int) {
        Log.i(TAG, "[onNestedScrollAccepted] $type")
        parentHelper.onNestedScrollAccepted(child, target, axes, type)
    }

    override fun onStopNestedScroll(target: View, type: Int) {
        Log.i(TAG, "[onStopNestedScroll] $type")
        parentHelper.onStopNestedScroll(target, type)
    }

    private var disableTouch = false

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        Log.d(TAG, "[dispatchTouchEvent] ${ev?.y} ${ev?.actionMasked}")
        when {
            ev.actionMasked == MotionEvent.ACTION_DOWN && ev.y + scrollY < verticalScrollRange().last -> {
                disableTouch = true
                Log.d(TAG, "[dispatchTouchEvent] [return] ${ev?.y} ${ev?.actionMasked}")
                return true
            }

            ev.actionMasked == MotionEvent.ACTION_UP || ev.actionMasked == MotionEvent.ACTION_CANCEL -> {
                disableTouch = false
            }

            else -> {}
        }

        if (disableTouch) return true

        when (ev?.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                scrollCauser = ScrollCauser.NONE
                overshootInterpolatorAnimator.cancel()
                activePointerId = ev.getPointerId(0)
            }

            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                if (openState.first != openState.second) {
                    // 下正
                    velocityTracker.computeCurrentVelocity(
                        1000,
                        ViewConfiguration.get(context).scaledMaximumFlingVelocity.toFloat()
                    )
                    val initialVelocity: Float = velocityTracker.getYVelocity(activePointerId)
                    if (initialVelocity > 0) { // 速度向下
                        autoSettle(scrollY, true, "")
                    } else if (initialVelocity < 0) { // 速度向上
                        autoSettle(scrollY, false, "")
                    } else { // 速度为0
                        if (Math.abs(scrollY - openState.first) > Math.abs(scrollY - openState.second)) {
                            autoSettle(scrollY, true, "")
                        } else {
                            autoSettle(scrollY, false, "")
                        }
                    }
                }
                activePointerId = INVALID_POINTER
            }
        }
        velocityTracker.addMovement(ev)
        return super.dispatchTouchEvent(ev)
    }

    private val touchSlop: Int

    init {
        val configuration = ViewConfiguration.get(context)
        touchSlop = configuration.scaledTouchSlop
    }

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        Log.d(TAG, "[onInterceptTouchEvent] ${ev.y} ${ev.actionMasked}")
        when (ev.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                // 记录Down
                lastDownYForInterceptEvent = ev.y.toInt()
            }

            MotionEvent.ACTION_MOVE -> {
                val y = ev.y.toInt()
                // 认为算是滚动
                val yDiffDown = abs(y - lastDownYForInterceptEvent)
                if (yDiffDown > touchSlop) {
                    // 当我还可以滚动的时候，优先滚动我自己
                    val yDiffMotion = lastMotionYForInterceptTouchEvent - ev.y.toInt()
                    if (yDiffMotion > 0) { // 向上
                        if ((yDiffMotion + scrollY) in verticalScrollRange()) {
                            Log.d(
                                TAG,
                                "[onInterceptTouchEvent] [return] $yDiffMotion $scrollY | ${verticalScrollRange()}"
                            )
                            lastMotionYForInterceptTouchEvent = ev.y.toInt()
                            return true
                        }
                    }
                }
            }
        }

        // 在拦截事件之前，走不到onTouch，所以需要在这里记录一下，
        // 拦截事件之后，就不会走[onInterceptEvent]了，只走[onTouchEvent]，所以对之后的"onTouchEvent对lastMotionForOnTouch"的记录不回有影响
        lastMotionForOnTouch = ev.y.toInt()
        lastMotionYForInterceptTouchEvent = ev.y.toInt()
        return super.onInterceptTouchEvent(ev) // 不拦截触摸事件，传递给子视图
    }

    private fun autoSettle(scrollY: Int, isDown: Boolean, reason: String) {
        val up = openState.second
        val down = openState.first

        val animateY = if (isDown) {
            stateList[down]
        } else {
            stateList[up]
        }
        Log.d(TAG, "[autoSettle] [up: $up] [down: $down] [isD: $isDown] [toY: $animateY]")

        scrollCauser = AUTO_SETTLE
        mScroller.abortAnimation()
        stopNestedScroll(ViewCompat.TYPE_NON_TOUCH)

        setAnimatedValue { AnimateValue(scrollY, animateY) }
        overshootInterpolatorAnimator.cancel()
        overshootInterpolatorAnimator.duration = 400
        overshootInterpolatorAnimator.setFloatValues(0f, 1f)
        overshootInterpolatorAnimator.start()
    }

    override fun onNestedScroll(
        target: View,
        dxConsumed: Int,
        dyConsumed: Int,
        dxUnconsumed: Int,
        dyUnconsumed: Int,
        @NestedScrollType type: Int,
        consumed: IntArray
    ) {
        // dy 负 向下
        Log.i(TAG, "[onNestedScroll] $dyUnconsumed")
        if (dyUnconsumed < 0) { // 向下
            when (type) {

                // fling造成的不滚动
                ViewCompat.TYPE_NON_TOUCH -> { }

                ViewCompat.TYPE_TOUCH -> {
                    doNestedPreScroll(dyUnconsumed, consumed, type)
                }
            }
        }
    }

    override fun onNestedScroll(
        target: View,
        dxConsumed: Int,
        dyConsumed: Int,
        dxUnconsumed: Int,
        dyUnconsumed: Int,
        type: Int
    ) {
        Log.i(TAG, "[onNestedScroll]2")
    }

    override fun onScrollChanged(l: Int, t: Int, oldl: Int, oldt: Int) {
        super.onScrollChanged(l, t, oldl, oldt)
        findViewById<TextView>(R.id.headerTitle).text =
            "header [s: $t] [s2: $openState] [h: $height] [ch: ${firstView.height}]"
    }

    private fun verticalScrollRange(): IntRange {
        return stateList[0]..stateList.last()
    }

    override fun onNestedPreScroll(
        target: View, dx: Int, dy: Int, consumed: IntArray, @NestedScrollType type: Int
    ) {
        Log.i(
            TAG,
            "[onNestedPreScroll] [dy: $dy] [s: $scrollY]"
        )
        // dy 下 负数 上 正数
        // scrollY 上 正 下 负

        if (dy > 0) { // 只处理向上滚动 —— 向上优先滚动外壳
            if (scrollY >= verticalScrollRange().last) { // 外壳滚到最高了
                doNestedPreScroll(dy, consumed, type)
            } else {
                consumed[1] = dy
            }
        }
    }

    private fun doNestedPreScroll(
        parentWantToConsume: Int, consumed: IntArray, @NestedScrollType type: Int
    ) {
        val nextY = scrollY + parentWantToConsume
        val maxNextY = verticalScrollRange().last
        val minNextY = verticalScrollRange().first

        val safeNextY = if (minNextY > maxNextY) {
            0
        } else {
            nextY.coerceIn(minNextY..maxNextY)
        }

        when (val safePrentWantToConsume = safeNextY - scrollY) {
            0 -> Log.i(TAG, "[doNestedPreScroll]#false ($type)")
            else -> {
                Log.i(TAG, "[doNestedPreScroll]#true ($type) ${scrollY} ${firstView.height}")
                consumed[1] = safePrentWantToConsume
                scrollBy(0, safePrentWantToConsume)
            }
        }
    }

    override fun scrollTo(x: Int, y: Int) {
        Log.d(TAG, "[scrollTo] $y $scrollCauser")
        when {
            scrollCauser == AUTO_SETTLE -> {
                originalScrollTo(x, y)
                return
            }

            else -> {
                if (y > verticalScrollRange().last) {
                    stopFling()
                    autoSettle(scrollY, false, "scrollTo")
                    return
                } else {
                    if (y in verticalScrollRange()) {
                        Log.d(TAG, "[scrollTo]#[true] $y ${firstView.height}")
                        originalScrollTo(x, y)
                    } else {
                        Log.d(TAG, "[scrollTo]#[false] $y ${firstView.height}")
                    }
                }

            }
        }
    }

    override fun fling(fl: Float) {
        if (scrollCauser != AUTO_SETTLE) {
            super.fling(fl)
        }
    }

    data class AnimateValue(
        var startY: Int,
        var endY: Int,
    )

    private fun setAnimatedValue(reducer: AnimateValue.() -> AnimateValue) {
        this.animateValue = reducer(this.animateValue)
    }

    object AUTO_SETTLE : ScrollCauser

    companion object {
        private const val TAG = "ParentNestedScrollView"
    }

}
