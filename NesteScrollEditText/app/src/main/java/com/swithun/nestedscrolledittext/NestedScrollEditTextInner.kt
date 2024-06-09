package com.swithun.nestedscrolledittext

import android.content.Context
import android.graphics.Rect
import android.util.AttributeSet
import android.view.View
import androidx.appcompat.widget.AppCompatEditText

class NestedScrollEditTextInner @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatEditText(context, attrs, defStyleAttr) {


    /** 禁止内部EdiText的滚动 */
    override fun scrollTo(x: Int, y: Int) {
    }

    override fun requestRectangleOnScreen(rectangle: Rect, immediate: Boolean): Boolean {
        val copyRect = Rect(rectangle)
        val scrolled = super.requestRectangleOnScreen(rectangle, immediate)

        val parentRect = Rect(copyRect)
        val parent = parent as NestedScrollEditText
        parent.post { // 这里一定要post，上面 immediate是false，所以parent.scrollY没有立即生效
            parentRect.bottom = parent.scrollY + parent.height + 200
            parent.requestRectangleOnScreen(parentRect)
        }
        return scrolled
    }

}