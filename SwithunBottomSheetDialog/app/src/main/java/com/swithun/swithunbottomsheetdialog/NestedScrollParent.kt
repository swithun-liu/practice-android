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

class ParentNestedScrollView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ChildNestedScrollView(context, attrs, defStyleAttr), NestedScrollingParent3 {

    val openState: OpenState = OpenState.OPEN

    private val parentHelper by lazy {
        NestedScrollingParentHelper(this)
    }

    private val velocityTracker = VelocityTracker.obtain()

    // 第一个View
    private val firstView by lazy {
        children.first()
    }

    private val halfTop = 1000


    private val overshootInterpolatorAnimator = ValueAnimator()
    private val overshootInterpolator = OvershootInterpolator()
    private var animateScrollY = 0
    private var isDown = false
    private var activePointerId = INVALID_POINTER

    init {
        this.post {
            val view = (firstView as LinearLayout)
            view.setPadding(0, halfTop, 0, 0)
        }
        overshootInterpolatorAnimator.addUpdateListener {
            val process = (it.animatedValue as Float)
            val f = overshootInterpolator.getInterpolation(process)
            val finalScrollY = if (isDown) {
                val passed = animateScrollY * f
                animateScrollY - passed
            } else {
                val passed = (getMaxScroll() - animateScrollY) * f
                animateScrollY + passed
            }
            this.scrollTo(scrollX, finalScrollY.toInt())
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
        when (ev?.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                eatMove = false
                isAnimating = false
                overshootInterpolatorAnimator.cancel()
                activePointerId = ev.getPointerId(0)
            }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                if (scrollY != getMaxScroll() && // 嘴上
                    scrollY != 0// 中间
                    ) {

                    // 下正
                    velocityTracker.computeCurrentVelocity(1000, ViewConfiguration.get(context).scaledMaximumFlingVelocity.toFloat())
                    val initialVelocity: Float = velocityTracker.getYVelocity(activePointerId)
                    Log.d(TAG, "[dispatchTouchEvent] $initialVelocity")

                    if (initialVelocity > 0f) {
                        test(scrollY, true)
                    } else if (initialVelocity < 0f) {
                        eatMove = true
                        test(scrollY, false)
                    } else {
                        if (Math.abs((scrollY - getMaxScroll())) > Math.abs((scrollY - 0))) {
                            test(scrollY, true)
                        } else {
                            eatMove = true
                            test(scrollY, false)
                        }
                    }


                    activePointerId = INVALID_POINTER
                }
            }
        }
        velocityTracker.addMovement(ev)
        return super.dispatchTouchEvent(ev)
    }

    private fun test(scrollY: Int, isDown: Boolean) {
        isAnimating = true
        this.isDown = isDown
        overshootInterpolatorAnimator.cancel()

        overshootInterpolatorAnimator.duration = 400
        animateScrollY = scrollY
        if (isDown) {
            overshootInterpolatorAnimator.setFloatValues(0f, 1f)
        } else {
            overshootInterpolatorAnimator.setFloatValues(0f, 1f)
        }
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

    override fun onNestedPreScroll(target: View, dx: Int, dy: Int, consumed: IntArray, @NestedScrollType type: Int) {
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
            else -> { }
        }
    }

    private fun doNestedPreScroll(parentWantToConsume: Int, consumed: IntArray, @NestedScrollType type: Int) {
        val nextY = scrollY + parentWantToConsume
        val maxNextY = getMaxScroll()
        val minNextY = 0

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
        if (isAnimating) {
            super.scrollTo(x, y)
            return
        }


        if (y >= 0 && y <= (firstView.height - height)) {
            Log.d(TAG, "[scrollTo]#[true] $y ${firstView.height}")
            super.scrollTo(x, y)
        } else {
            Log.d(TAG, "[scrollTo]#[false] $y ${firstView.height}")
        }
    }

    fun getMaxScroll(): Int {
        return Math.min(halfTop, firstView.height - height)
    }

    enum class OpenState {
        OPEN,
        HALF_OPEN,
        CLOSE
    }

    companion object {
        private const val TAG = "ParentNestedScrollView"
    }

}
