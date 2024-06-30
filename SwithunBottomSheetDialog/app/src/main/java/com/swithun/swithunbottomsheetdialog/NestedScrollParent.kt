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
import android.widget.LinearLayout
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

    override fun fling(fl: Float) {
        if (scrollCauser != AUTO_SETTLE) {
            super.fling(fl)
        }
    }


    private val openState: OpenState
        get() {
            return when {
                scrollY > state2Scroll -> OpenState.STATE2
                scrollY == state2Scroll -> OpenState.STATE2
                scrollY > state1Scroll -> OpenState.STATE2_1
                scrollY == state1Scroll -> OpenState.STATE1
                scrollY > state0Scroll -> OpenState.STATE1_0
                scrollY == state0Scroll -> OpenState.STATE0
                else -> OpenState.STATE0
            }
        }

    // scroll下负
    private val state0Scroll
        get() = -(height - state2Scroll) + 200

    private val state1Scroll = 0

    // scroll上正
    private val state2Scroll
        get() = getMaxScroll()


    private val parentHelper by lazy {
        NestedScrollingParentHelper(this)
    }

    private val velocityTracker = VelocityTracker.obtain()

    // 第一个View
    private val firstView by lazy {
        children.first()
    }

    private val halfTop = 1000
    private var animateValue = AnimateValue(
        0, 0
    )


    private val overshootInterpolatorAnimator = ValueAnimator()
    private val overshootInterpolator = OvershootInterpolator(1f)
    private var activePointerId = INVALID_POINTER

    init {
        this.post {
            val view = (firstView as LinearLayout)
            view.setPadding(0, halfTop, 0, 0)
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

    private var eatMove = false

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        Log.d(TAG, "[dispatchTouchEvent] ${ev?.y} ${ev?.actionMasked}")
        when (ev?.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                eatMove = false
                scrollCauser = ScrollCauser.NONE
                overshootInterpolatorAnimator.cancel()
                activePointerId = ev.getPointerId(0)
            }

            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                when (openState) {
                    OpenState.STATE2, OpenState.STATE1, OpenState.STATE0 -> {}
                    OpenState.STATE2_1, OpenState.STATE1_0 -> {
                        // 下正
                        velocityTracker.computeCurrentVelocity(
                            1000,
                            ViewConfiguration.get(context).scaledMaximumFlingVelocity.toFloat()
                        )
                        val initialVelocity: Float = velocityTracker.getYVelocity(activePointerId)
                        Log.d(TAG, "[dispatchTouchEvent] UP/CANCEL ${ev?.actionMasked} | $initialVelocity")

                        if (initialVelocity > 0f) {
                            autoSettle(scrollY, true, "1")
                        } else if (initialVelocity < 0f) {
                            eatMove = true
                            autoSettle(scrollY, false, "2")
                        } else {
                            when (openState) {
                                OpenState.STATE2_1 -> {
                                    if (abs(scrollY - state2Scroll) > abs(scrollY - state1Scroll)) {
                                        autoSettle(scrollY, true, "3")
                                    } else {
                                        eatMove = true
                                        autoSettle(scrollY, false, "4")
                                    }
                                }

                                OpenState.STATE1_0 -> {
                                    if (abs(scrollY - state1Scroll) > abs(scrollY - state0Scroll)) {
                                        autoSettle(scrollY, true, "5")
                                    } else {
                                        eatMove = true
                                        autoSettle(scrollY, false, "6")
                                    }
                                }

                                else -> {}
                            }
                        }
                    }
                }

                activePointerId = INVALID_POINTER
            }
        }
        velocityTracker.addMovement(ev)
        return super.dispatchTouchEvent(ev)
    }

    private var lastDownY: Int = 0
    private val touchSlop: Int

    init {
        val configuration = ViewConfiguration.get(context)
        touchSlop = configuration.scaledTouchSlop
    }

    private var lastMotionY = 0

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        Log.d(TAG, "[onInterceptTouchEvent] ${ev.y} ${ev.actionMasked}")
        when (ev.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                lastDownY = ev.y.toInt()
                lastTouchY = ev.y.toInt()
            }
            MotionEvent.ACTION_MOVE -> {
                val y = ev.y.toInt()
                val yDiff = abs(y - lastDownY)
                val yMoveDiff = lastMotionY - ev.y.toInt()
                if (yDiff > touchSlop) {
                    if ((yMoveDiff + scrollY) in state0Scroll..state2Scroll) {
                        return true // 拦截触摸事件，处理滚动
                    }
                }
            }
        }
        lastMotionY = ev.y.toInt()
        return super.onInterceptTouchEvent(ev) // 不拦截触摸事件，传递给子视图
    }

    private fun autoSettle(scrollY: Int, isDown: Boolean, reason: String) {
        val animateY = when (openState) {
            OpenState.STATE2 -> state2Scroll
            OpenState.STATE2_1 -> if (isDown) state1Scroll else state2Scroll
            OpenState.STATE1 -> null
            OpenState.STATE1_0 -> if (isDown) state0Scroll else state1Scroll
            OpenState.STATE0 -> null
        } ?: return
        Log.d(TAG, "[scrollCauser] to AUTO_SETTLE")
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
        @NestedScrollType
        type: Int,
        consumed: IntArray
    ) {
        Log.i(TAG, "[onNestedScroll] $dyUnconsumed")
        if ((halfTop - scrollY) == 0 || firstView.height - scrollY <= height) {
            // 到顶
            when (type) {

                ViewCompat.TYPE_NON_TOUCH -> {
                }

                ViewCompat.TYPE_TOUCH -> {
                    doNestedPreScroll(dyUnconsumed, consumed, type)
                }
            }
        } else { // 没到顶
            when (type) {

                ViewCompat.TYPE_NON_TOUCH -> {
                }

                ViewCompat.TYPE_TOUCH -> {
                    doNestedPreScroll(dyUnconsumed, consumed, type)
                }
            }
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (event != null && event.y + scrollY < state2Scroll) {
            Log.d(TAG, "[onTouchEvent] position ${event?.y} ${event.actionMasked} [return]")
            return true
        }

        Log.d(TAG, "[onTouchEvent] position ${event?.y} ${event.actionMasked}")
        return super.onTouchEvent(event)
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

    override fun onNestedPreScroll(
        target: View,
        dx: Int,
        dy: Int,
        consumed: IntArray,
        @NestedScrollType type: Int
    ) {
        Log.i(TAG, "[onNestedPreScroll] 1- $dy [$scrollY] ||| ${System.currentTimeMillis()}")
        // dy 下 负数 上 正数
        // scrollY 上 正 下 负

        when {
            // 上
            dy >= 0 -> {
                val parentWantToConsume = dy
                if ((halfTop - scrollY) == 0 || firstView.height - scrollY <= height) {
                    // 到顶
                    doNestedPreScroll(parentWantToConsume, consumed, type)
                    if (eatMove) {
                        consumed[1] = dy
                    }
                } else { // 没到顶
                    when (type) {
                        ViewCompat.TYPE_NON_TOUCH -> {
                            consumed[1] = parentWantToConsume
                        }

                        ViewCompat.TYPE_TOUCH -> {
                            doNestedPreScroll(parentWantToConsume, consumed, type)
                        }
                    }
                }

            }
            // 下
            else -> {}
        }
    }

    private fun doNestedPreScroll(
        parentWantToConsume: Int,
        consumed: IntArray,
        @NestedScrollType type: Int
    ) {
        val nextY = scrollY + parentWantToConsume
        val maxNextY = state2Scroll
        val minNextY = state0Scroll

        val safeNextY = if (minNextY > maxNextY) {
            0
        } else {
            nextY.coerceIn(minNextY..maxNextY)
        }

        when (val safePrentWantToConsume = safeNextY - scrollY) {
            0 -> Log.i(TAG, "[onNestedPreScroll]#false ($type)")
            else -> {
                Log.i(TAG, "[onNestedPreScroll]#true ($type) ${scrollY} ${firstView.height}")
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
                if (y > state2Scroll) {
                    stopFling()
                    autoSettle(scrollY, false, "scrollTo")
                    return
                } else {
                    if (y in state0Scroll..state2Scroll) {
                        Log.d(TAG, "[scrollTo]#[true] $y ${firstView.height}")
                        originalScrollTo(x, y)
                    } else {
                        Log.d(TAG, "[scrollTo]#[false] $y ${firstView.height}")
                    }
                }

            }
        }

    }

    fun getMaxScroll(): Int {
        return Math.min(halfTop, firstView.height - height)
    }

    data class AnimateValue(
        var startY: Int,
        var endY: Int,
    )

    private fun setAnimatedValue(reducer: AnimateValue.() -> AnimateValue) {
        this.animateValue = reducer(this.animateValue)
    }

    object AUTO_SETTLE : ScrollCauser

    enum class OpenState {
        STATE2,
        STATE2_1,
        STATE1,
        STATE1_0,
        STATE0
    }

    companion object {
        private const val TAG = "ParentNestedScrollView"
    }

}
