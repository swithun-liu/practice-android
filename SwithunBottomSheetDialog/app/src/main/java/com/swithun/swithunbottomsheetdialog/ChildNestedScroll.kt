package com.swithun.swithunbottomsheetdialog

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.widget.FrameLayout
import android.widget.OverScroller
import androidx.core.view.NestedScrollingChild3
import androidx.core.view.NestedScrollingChildHelper
import androidx.core.view.children

open class ChildNestedScrollView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr), NestedScrollingChild3 {


    protected val childHelper by lazy {
        NestedScrollingChildHelper(this).apply { isNestedScrollingEnabled = true }
    }

    protected var scrollCauser: ScrollCauser = ScrollCauser.NONE

    protected val mScroller = OverScroller(context)

    override fun startNestedScroll(axes: Int, type: Int): Boolean {
        Log.i(TAG, "child startNestedScroll axes:$axes type:$type ")
        return childHelper.startNestedScroll(axes, type)
    }

    override fun stopNestedScroll(type: Int) {
        Log.i(TAG, "child stopNestedScroll $type")
        childHelper.stopNestedScroll(type)
    }

    override fun hasNestedScrollingParent(type: Int): Boolean {
        return childHelper.hasNestedScrollingParent(type)
    }

    override fun dispatchNestedScroll(
        dxConsumed: Int,
        dyConsumed: Int,
        dxUnconsumed: Int,
        dyUnconsumed: Int,
        offsetInWindow: IntArray?,
        type: Int,
        consumed: IntArray
    ) {

    }

    override fun dispatchNestedScroll(
        dxConsumed: Int,
        dyConsumed: Int,
        dxUnconsumed: Int,
        dyUnconsumed: Int,
        offsetInWindow: IntArray?,
        type: Int
    ): Boolean {
        return childHelper.dispatchNestedScroll(
            dxConsumed,
            dyConsumed,
            dxUnconsumed,
            dyUnconsumed, offsetInWindow, type
        )
    }

    override fun dispatchNestedPreScroll(
        dx: Int,
        dy: Int,
        consumed: IntArray?,
        offsetInWindow: IntArray?,
        type: Int
    ): Boolean {
        return childHelper.dispatchNestedPreScroll(dx, dy, consumed, offsetInWindow, type)
    }

    interface ScrollCauser {
        object NONE : ScrollCauser

        object USER_TOUCH : ScrollCauser

        object FLING : ScrollCauser
    }

    protected fun originalScrollTo(x: Int, y: Int) {
        super.scrollTo(x, y)
    }


    companion object {
        private const val TAG = "ChildNestedScrollView"
        private const val INVALID_POINTER = -1
    }

}
