package com.swithun.swithunbottomsheetdialog

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.widget.FrameLayout
import androidx.core.view.NestedScrollingParent3
import androidx.core.view.NestedScrollingParentHelper
import androidx.core.view.ViewCompat
import androidx.core.view.ViewCompat.NestedScrollType
import androidx.core.view.children

class ParentNestedScrollView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr), NestedScrollingParent3 {

    val openState: OpenState = OpenState.OPEN

    private val parentHelper by lazy {
        NestedScrollingParentHelper(this)
    }

    // 第一个View
    private val firstView by lazy {
        children.first()
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

    override fun onNestedScroll(
        target: View,
        dxConsumed: Int,
        dyConsumed: Int,
        dxUnconsumed: Int,
        dyUnconsumed: Int,
        type: Int,
        consumed: IntArray
    ) {
        Log.i(TAG, "[onNestedScroll] $dyUnconsumed")
        doNestedPreScroll(dyUnconsumed, consumed, type)
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

    override fun onNestedPreScroll(target: View, dx: Int, dy: Int, consumed: IntArray, type: Int) {
        Log.i(TAG, "[onNestedPreScroll] 1- $dy")
        // 下 负数 上 正数

        val parentWantToConsume = when {
            dy >= 0 -> dy
            else -> 0
        }

        doNestedPreScroll(parentWantToConsume, consumed, type)
    }

    private fun doNestedPreScroll(parentWantToConsume: Int, consumed: IntArray, @NestedScrollType type: Int) {
        val nextY = scrollY + parentWantToConsume
        val maxNextY = (firstView.height - height)
        val minNextY = 0
        val safeNextY = nextY.coerceIn(minNextY..maxNextY)

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
        if (y >= 0 && y <= (firstView.height - height)) {
            Log.d(TAG, "[scrollTo]#[true] $y ${firstView.height}")
            super.scrollTo(x, y)
        } else {
            Log.d(TAG, "[scrollTo]#[false] $y ${firstView.height}")
        }
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
