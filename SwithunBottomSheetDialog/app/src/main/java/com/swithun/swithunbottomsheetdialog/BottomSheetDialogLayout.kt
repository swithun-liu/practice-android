package com.swithun.swithunbottomsheetdialog

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Rect
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.VelocityTracker
import android.view.View
import android.view.ViewConfiguration
import android.view.ViewGroup
import android.view.animation.OvershootInterpolator
import android.widget.FrameLayout
import androidx.core.view.NestedScrollingParent3
import androidx.core.view.NestedScrollingParentHelper
import androidx.core.view.ViewCompat
import androidx.core.view.ViewCompat.NestedScrollType
import androidx.core.view.children
import androidx.customview.widget.ViewDragHelper.INVALID_POINTER
import kotlin.math.abs

class BottomSheetDialogLayout @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr), NestedScrollingParent3 {

    /** 第一个View */
    private val firstView: View?
        get() = children.firstOrNull()

    //// Event 记录
    /** [onTouchEvent]记录上一个EventY */
    private var lastMotionYForOnTouchEvent = 0

    /** [onTouchEvent]记录上一个DownEventY */
    private var lastDownYForOnTouchEvent = 0

    /** [onInterceptTouchEvent]记录上一个EventY */
    private var lastMotionYForInterceptTouchEvent = 0

    /** [onInterceptTouchEvent]记录上一个DownEventY */
    private var lastDownYForInterceptEvent: Int = 0

    /** 造成滚动的原因 */
    private var scrollCauser: ScrollCauser = ScrollCauser.NONE

    //// 吸附位置
    /** 默认的最低吸附位置 */
    private val stateLowest get() = -height
    /** 默认的最高吸附位置 */
    private val stateHighest get() = 0

    /** 自定义的吸附位置列表 */
    var customStateList: List<CustomHeight>? = null

    /** 想要的吸附位置列表 */
    private val wantStateList: List<Int>
        get() {
            val custom: List<CustomHeight>? = customStateList
            return custom?.map {
                when (it) {
                    is CustomHeight.Bottom -> stateLowest + it.add
                    CustomHeight.Highest -> stateHighest
                    is CustomHeight.Number -> -it.value
                    is CustomHeight.Percent -> (stateLowest * it.f).toInt()
                }
            } ?: listOf(
                stateLowest + 200, // -2691
                (stateLowest * 0.4).toInt(), // -1000
                stateHighest // 0
            )
        }

    /** 当contentView太矮可能无法满足[wantStateList]，需要check一下 */
    private val safeStateList: List<Int>
        get() {
            val firstView = firstView ?: return wantStateList
            val firstViewHeight = firstView.height
            val list = mutableListOf<Int>()
            val sortedStateList = wantStateList.sorted()
            for (i in sortedStateList.indices) {
                if (-(height - firstViewHeight) > sortedStateList[i]) {
                    list.add(sortedStateList[i])
                }
            }
            if (list.size < sortedStateList.size) {
                list.add(-(height - firstViewHeight))
            }
            return list
        }

    /** <当前State，更高的State> */
    private val openState: Pair<Int, Int>
        get() {
            var oldState = safeStateList.size - 1
            for (i in safeStateList.indices.reversed()) {
                if (scrollY >= safeStateList[i]) {
                    return i to oldState
                }
                oldState = i
            }
            return 0 to oldState
        }

    private val parentHelper by lazy {
        NestedScrollingParentHelper(this)
    }

    /// fling 检测
    private val velocityTracker = VelocityTracker.obtain()
    private var activePointerId = INVALID_POINTER

    // autoSettle动画相关
    private var animateStartY2EndY = AnimateValue(0, 0)
    private val autoSettleAnimator = ValueAnimator().also {
        it.interpolator = OvershootInterpolator(1f)
    }

    fun init() {
        this.post {
            scrollTo(scrollX, safeStateList[0])
        }
        autoSettleAnimator.addUpdateListener {
            val process = (it.animatedValue as Float)
            val passed = (animateStartY2EndY.endY - animateStartY2EndY.startY) * process
            val newScrollY = animateStartY2EndY.startY + passed
            Log.d(TAG, "[AUTO_SETTLE_ANIM] listener $process | $newScrollY")
            Log.d(
                TAG,
                "[Fling Animate] $process $process | ${animateStartY2EndY.endY}, ${animateStartY2EndY.startY} | $passed $newScrollY"
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

    override fun onTouchEvent(event: MotionEvent): Boolean {
        Log.d(TAG, "「onTouchEvent」 [t: ${event.actionMasked}] [y: ${event.y}]")
        val touchY = event.y.toInt()
        when (event.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                recordDownForOnTouchEvent(touchY)
            }

            MotionEvent.ACTION_MOVE -> {
                // 要滚多少
                val moveY = lastMotionYForOnTouchEvent - touchY
                scrollCauser = ScrollCauser.USER_TOUCH
                scrollBy(0, moveY)
                lastMotionYForOnTouchEvent = touchY
            }
        }
        return true
    }

    private fun recordDownForOnTouchEvent(touchY: Int) {
        scrollCauser = ScrollCauser.NONE
        autoSettleAnimator.cancel()
        lastDownYForOnTouchEvent = touchY
        lastMotionYForOnTouchEvent = touchY
    }

    override fun onStopNestedScroll(target: View, type: Int) {
        Log.i(TAG, "[onStopNestedScroll] $type")
        parentHelper.onStopNestedScroll(target, type)
    }

    private var disableTouch = false

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        Log.d(TAG, "「dispatchTouchEvent」 ${ev?.y} ${ev?.actionMasked}")
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
                autoSettleAnimator.cancel()
                activePointerId = ev.getPointerId(0)
            }

            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                if (openState.first != openState.second && scrollY != safeStateList[openState.first]) {
                    // 下正
                    velocityTracker.computeCurrentVelocity(
                        1000, ViewConfiguration.get(context).scaledMaximumFlingVelocity.toFloat()
                    )
                    val initialVelocity: Float = velocityTracker.getYVelocity(activePointerId)
                    settle(initialVelocity)
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
        Log.d(TAG, "「onInterceptTouchEvent」 [t: ${ev.actionMasked}] [y: ${ev.y}]")
        when (ev.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                // 记录Down
                lastDownYForInterceptEvent = ev.y.toInt()
                // 帮[onTouchEvent]记录一下
                recordDownForOnTouchEvent(ev.y.toInt())
            }

            MotionEvent.ACTION_MOVE -> {
                val y = ev.y.toInt()
                // 认为算是滚动
                val yDiffDown = abs(y - lastDownYForInterceptEvent)
                if (yDiffDown > touchSlop) {
                    val yDiffMotion = lastMotionYForInterceptTouchEvent - ev.y.toInt()
                    if (yDiffMotion > 0) { // 向上
                        // 当我还可以滚动的时候，优先滚动我自己
                        if ((yDiffMotion + scrollY) in verticalScrollRange()) {
                            Log.d(
                                TAG,
                                "[onInterceptTouchEvent] [return] $yDiffMotion $scrollY | ${verticalScrollRange()}"
                            )
                            lastMotionYForInterceptTouchEvent = ev.y.toInt()
                            return true
                        }
                    } else if (yDiffMotion < 0) { // 向下

                        fun isTouchNestedScrollChild(
                            parent: ViewGroup, child: View, x: Int, y: Int
                        ): Boolean {
                            fun isMeTouchNestedScrollChild(
                                parent: ViewGroup, child: View, x: Int, y: Int
                            ): Boolean {
                                val r = Rect()
                                val offsetX = parent.scrollX - child.left
                                val offsetY = parent.scrollY - child.top
                                val newX = x + offsetX
                                val newY = y + offsetY

                                r.top = child.top
                                r.left = child.left
                                r.right = child.right
                                r.bottom = child.bottom

                                Log.d(
                                    TAG,
                                    "isMeTouchNestedScrollChild: p: $parent c: $child x: $x y: $y sY: ${parent.scrollX} t: ${child.top}"
                                )

                                val contain = r.contains(newX, newY)
                                val nestedScrollChild = child.isNestedScrollingEnabled
                                Log.d(
                                    TAG,
                                    "isMeTouchNestedScrollChild: $child contain $contain $nestedScrollChild"
                                )
                                return contain && nestedScrollChild
                            }

                            return isMeTouchNestedScrollChild(
                                parent, child, x, y
                            ) || (child is ViewGroup && child.children.any { childChild ->

                                val offsetX = parent.scrollX - child.left
                                val offsetY = parent.scrollY - child.top
                                val newX = x + offsetX
                                val newY = y + offsetY

                                isTouchNestedScrollChild(child, childChild, newX, newY)
                            })
                        }

                        children.forEach { child ->
                            if (!isTouchNestedScrollChild(
                                    this, child, ev.x.toInt(), ev.y.toInt()
                                )
                            ) {
                                return true
                            }
                        }
                    }
                }
            }
        }

        // 在拦截事件之前，走不到onTouch，所以需要在这里记录一下，
        // 拦截事件之后，就不会走[onInterceptEvent]了，只走[onTouchEvent]，所以对之后的"onTouchEvent对lastMotionForOnTouch"的记录不回有影响
        lastMotionYForOnTouchEvent = ev.y.toInt()
        lastMotionYForInterceptTouchEvent = ev.y.toInt()
        return super.onInterceptTouchEvent(ev) // 不拦截触摸事件，传递给子视图
    }

    private fun doSettle(scrollY: Int, isDown: Boolean, reason: String) {
        val up = openState.second
        val down = openState.first

        val animateY = if (isDown) {
            safeStateList[down]
        } else {
            safeStateList[up]
        }
        Log.d(TAG, "[autoSettle] [up: $up] [down: $down] [isD: $isDown] [toY: $animateY]")

        scrollCauser = ScrollCauser.AUTO_SETTLE

        setAnimatedValue { AnimateValue(scrollY, animateY) }
        autoSettleAnimator.cancel()
        autoSettleAnimator.duration = 400
        autoSettleAnimator.setFloatValues(0f, 1f)
        autoSettleAnimator.start()
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
        Log.i(TAG, "「onNestedScroll」 $dyUnconsumed")
        if (dyUnconsumed < 0) { // 向下
            when (type) {

                // fling造成的不滚动
                ViewCompat.TYPE_NON_TOUCH -> {}

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

    private fun verticalScrollRange(): IntRange {
        return safeStateList[0]..safeStateList.last()
    }

    override fun onNestedPreScroll(
        target: View, dx: Int, dy: Int, consumed: IntArray, @NestedScrollType type: Int
    ) {
        Log.i(TAG, "「onNestedPreScroll」 [dy: $dy] [s: $scrollY]")
        // dy 下 负数 上 正数
        // scrollY 上 正 下 负

        when (type) {
            ViewCompat.TYPE_NON_TOUCH -> {
                // fling造成的不处理，内部自己滚
            }

            ViewCompat.TYPE_TOUCH -> {
                if (scrollY >= safeStateList.last()) {
                    // 外壳到顶了，先滚内部
                } else {
                    if (dy > 0) {
                        // 手指下滑，优先滚内部，才会走到这里
                        // 外壳没到顶，向上滚动，先滚外壳
                        doNestedPreScroll(dy, consumed, type)
                    } else {
                        // 手指上滑，优先滚外壳，在onTouchEvent那里处理了，所有不会走到这里
                    }
                }
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
                Log.i(TAG, "[doNestedPreScroll]#true ($type) ${scrollY} ${firstView?.height}")
                consumed[1] = safePrentWantToConsume
                scrollBy(0, safePrentWantToConsume)
            }
        }
    }

    override fun scrollTo(x: Int, y: Int) {
        Log.d(TAG, "[scrollTo] $y $scrollCauser")
        when {
            scrollCauser == ScrollCauser.AUTO_SETTLE -> {
                super.scrollTo(x, y)
                return
            }

            else -> {
                if (y > verticalScrollRange().last) {
                    doSettle(scrollY, false, "scrollTo")
                    return
                } else {
                    if (y in verticalScrollRange()) {
                        Log.d(TAG, "[scrollTo]#[true] $y ${firstView?.height}")
                        super.scrollTo(x, y)
                    } else {
                        Log.d(TAG, "[scrollTo]#[false] $y ${firstView?.height}")
                    }
                }

            }
        }
    }

    private fun settle(fl: Float) {
        // 下正
        if (fl > 0) { // 速度向下
            doSettle(scrollY, true, "")
        } else if (fl < 0) { // 速度向上
            doSettle(scrollY, false, "")
        } else { // 速度为0
            if (Math.abs(scrollY - openState.first) > Math.abs(scrollY - openState.second)) {
                doSettle(scrollY, true, "")
            } else {
                doSettle(scrollY, false, "")
            }
        }
    }

    data class AnimateValue(
        var startY: Int,
        var endY: Int,
    )

    private fun setAnimatedValue(reducer: AnimateValue.() -> AnimateValue) {
        this.animateStartY2EndY = reducer(this.animateStartY2EndY)
    }

    interface ScrollCauser {
        object NONE : ScrollCauser

        object USER_TOUCH : ScrollCauser

        object AUTO_SETTLE : ScrollCauser
    }

    sealed interface CustomHeight {
        class Number(val value: Int) : CustomHeight
        class Percent(val f: Float) : CustomHeight
        class Bottom(val add: Int) : CustomHeight
        data object Highest : CustomHeight
    }


    companion object {
        private const val TAG = "ParentNestedScrollView"
    }

}