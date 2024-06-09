package com.swithun.nestedscrolledittext

import android.content.Context
import android.graphics.Rect
import android.util.AttributeSet
import android.view.View
import androidx.core.widget.NestedScrollView

class ParentNestedScrollView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : NestedScrollView(context, attrs, defStyleAttr) {

   private var isRequestingChildFocus = false

    /**
     * ScrollView大小变化后，会找有没有focus的view，则会滚动到focus的EditText处
     */
    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
    }


    /**
     * EditText requestFocus 会调用 parent的的requestChildFocus，NestedScrollView会自动滚动到focus child
     * 但是这里的没有考虑光标位置，导致不符合预期
     * 通过 [isRequestingChildFocus] 强制让 [computeScrollDeltaToGetChildRectOnScreen]——此次的滚动delta变为0
     */
    override fun requestChildFocus(child: View?, focused: View?) {
        isRequestingChildFocus = true
        super.requestChildFocus(child, focused)
        isRequestingChildFocus = false
    }

    override fun computeScrollDeltaToGetChildRectOnScreen(rect: Rect?): Int {
        return if (isRequestingChildFocus) {
            0
        } else {
            super.computeScrollDeltaToGetChildRectOnScreen(rect)
        }
    }

}