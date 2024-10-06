package com.swithun.learnestedscrolling

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import androidx.core.widget.NestedScrollView
import java.util.UUID

class LogNestedScrollView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : NestedScrollView(
    context, attrs, defStyleAttr
) {

    var TAG = ""

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        val id = UUID.randomUUID()
        Log.d(TAG, "dispatchTouchEvent($id) begin")
        return super.dispatchTouchEvent(ev).also {
            Log.d(TAG, "dispatchTouchEvent($id) end")
        }
    }

    override fun onTouchEvent(ev: MotionEvent): Boolean {
        val actionName = when(ev.actionMasked) {
            MotionEvent.ACTION_DOWN -> "ACTION_DOWN"
            MotionEvent.ACTION_MOVE -> "ACTION_MOVE"
            MotionEvent.ACTION_UP -> "ACTION_UP"
            MotionEvent.ACTION_CANCEL -> "ACTION_CANCEL"
            else -> "OTHER_ACTOIN"
        }
        return super.onTouchEvent(ev).also {
            Log.d(TAG, "touchEvent: $actionName | return $it")
        }
    }

    override fun startNestedScroll(axes: Int): Boolean {
        return super.startNestedScroll(axes).also {
            Log.d(TAG, "CHILD-1: startNestedScroll $axes")
        }
    }

    override fun startNestedScroll(axes: Int, type: Int): Boolean {
        return super.startNestedScroll(axes, type).also {
            Log.d(TAG, "CHILD-2: startNestedScroll $axes")
        }
    }

    override fun stopNestedScroll() {
        super.stopNestedScroll().also {
            Log.d(TAG, "CHILD-1: stopNestedScroll")
        }
    }

    override fun hasNestedScrollingParent(): Boolean {
        return super.hasNestedScrollingParent().also {
            Log.d(TAG, "CHILD-1: hasNestedScrollingParent $it")
        }
    }

    override fun dispatchNestedScroll(
        dxConsumed: Int,
        dyConsumed: Int,
        dxUnconsumed: Int,
        dyUnconsumed: Int,
        offsetInWindow: IntArray?
    ): Boolean {
        return super.dispatchNestedScroll(
            dxConsumed,
            dyConsumed,
            dxUnconsumed,
            dyUnconsumed,
            offsetInWindow
        ).also {
            Log.d(TAG, "CHILD-1: dispatchNestedScroll $it")
        }
    }

    override fun dispatchNestedScroll(
        dxConsumed: Int,
        dyConsumed: Int,
        dxUnconsumed: Int,
        dyUnconsumed: Int,
        offsetInWindow: IntArray?,
        type: Int
    ): Boolean {
        return super.dispatchNestedScroll(
            dxConsumed,
            dyConsumed,
            dxUnconsumed,
            dyUnconsumed,
            offsetInWindow,
            type
        ).also {
            Log.d(TAG, "CHILD-2: dispatchNestedScroll $it")
        }
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
        super.dispatchNestedScroll(
            dxConsumed,
            dyConsumed,
            dxUnconsumed,
            dyUnconsumed,
            offsetInWindow,
            type,
            consumed
        ).also {
            Log.d(TAG, "CHILD-3: dispatchNestedScroll $it")
        }
    }

    override fun dispatchNestedPreScroll(
        dx: Int,
        dy: Int,
        consumed: IntArray?,
        offsetInWindow: IntArray?,
        type: Int
    ): Boolean {
        return super.dispatchNestedPreScroll(dx, dy, consumed, offsetInWindow, type).also {
            Log.d(TAG, "CHILD-2: dispatchNestedPreScroll $it")
        }
    }

    override fun dispatchNestedPreScroll(
        dx: Int,
        dy: Int,
        consumed: IntArray?,
        offsetInWindow: IntArray?
    ): Boolean {
        return super.dispatchNestedPreScroll(dx, dy, consumed, offsetInWindow).also {
            Log.d(TAG, "CHILD-3: dispatchNestedPreScroll $it")
        }
    }


    override fun dispatchNestedFling(
        velocityX: Float,
        velocityY: Float,
        consumed: Boolean
    ): Boolean {
        return super.dispatchNestedFling(velocityX, velocityY, consumed).also {
            Log.d(TAG, "CHILD-1: dispatchNestedFling $it")
        }
    }

    override fun dispatchNestedPreFling(velocityX: Float, velocityY: Float): Boolean {
        return super.dispatchNestedPreFling(velocityX, velocityY).also {
            Log.d(TAG, "CHILD-1: dispatchNestedPreFling $it")
        }
    }

    override fun onStartNestedScroll(child: View, target: View, axes: Int): Boolean {
        return super.onStartNestedScroll(child, target, axes).also {
            Log.i(TAG, "PARENT-1: onStartNestedScroll $it")
        }
    }

    override fun onStartNestedScroll(child: View, target: View, axes: Int, type: Int): Boolean {
        return super.onStartNestedScroll(child, target, axes, type).also {
            Log.i(TAG, "PARENT-2: onStartNestedScroll $it")
        }
    }

    override fun onNestedScrollAccepted(child: View, target: View, axes: Int) {
        super.onNestedScrollAccepted(child, target, axes).also {
            Log.i(TAG, "PARENT-1: onNestedScrollAccepted $it")
        }
    }

    override fun onNestedScrollAccepted(child: View, target: View, axes: Int, type: Int) {
        super.onNestedScrollAccepted(child, target, axes, type).also {
            Log.i(TAG, "PARENT-2: onNestedScrollAccepted $it")
        }
    }

    override fun onNestedScroll(
        target: View,
        dxConsumed: Int,
        dyConsumed: Int,
        dxUnconsumed: Int,
        dyUnconsumed: Int
    ) {
        super.onNestedScroll(target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed).also {
            Log.i(TAG, "PARENT-1: onNestedScroll $it")
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
        super.onNestedScroll(target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, type).also {
            Log.i(TAG, "PARENT-2: onNestedScroll $it")
        }
    }

    override fun onNestedPreScroll(target: View, dx: Int, dy: Int, consumed: IntArray) {
        super.onNestedPreScroll(target, dx, dy, consumed).also {
            Log.i(TAG, "PARENT-1: onNestedPreScroll $it")
        }
    }

    override fun onNestedPreScroll(target: View, dx: Int, dy: Int, consumed: IntArray, type: Int) {
        super.onNestedPreScroll(target, dx, dy, consumed, type).also {
            Log.i(TAG, "PARENT-2: onNestedPreScroll $it")
        }
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
        super.onNestedScroll(
            target,
            dxConsumed,
            dyConsumed,
            dxUnconsumed,
            dyUnconsumed,
            type,
            consumed
        ).also {
            Log.i(TAG, "PARENT-3: onNestedScroll $it")
        }
    }

    override fun onNestedFling(
        target: View,
        velocityX: Float,
        velocityY: Float,
        consumed: Boolean
    ): Boolean {
        return super.onNestedFling(target, velocityX, velocityY, consumed).also {
            Log.i(TAG, "PARENT-1: onNestedFling $it")
        }
    }

    override fun onNestedPreFling(target: View, velocityX: Float, velocityY: Float): Boolean {
        return super.onNestedPreFling(target, velocityX, velocityY).also {
            Log.i(TAG, "PARENT-1: onNestedPreFling $it")
        }
    }

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        return super.onInterceptTouchEvent(ev).also {
            Log.i(TAG, " onInterceptTouchEvent: $it")
        }
    }

}