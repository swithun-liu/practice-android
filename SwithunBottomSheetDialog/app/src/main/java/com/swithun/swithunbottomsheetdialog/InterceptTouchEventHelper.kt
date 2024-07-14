package com.swithun.swithunbottomsheetdialog

import android.graphics.Rect
import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.core.view.children

class InterceptTouchEventHelper {

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

    companion object {
        private const val TAG = "InterceptTouchEventHelper"
    }

}