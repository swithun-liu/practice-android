package com.swithun.nestedscrolledittext.nestedscroledittext

import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.widget.NestedScrollView
import android.graphics.Rect

class NestedScrollEditText @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : NestedScrollView(context, attrs, defStyleAttr) {

    protected val editText: NestedScrollEditTextInner = NestedScrollEditTextInner(context).also {
        it.isFocusableInTouchMode = true
    }

    protected var bottomGapPx = 200

    init {
        editText.setText(
            "萨萨是的发送到发送到发送到发送到发送到发送到发送到发送到发送萨是的发送到发送到发送到发送到发送到发送到发送到发送到发送到发萨是的发送到发送到发送到发送到发送到发送到发送到发送到发送到发萨是的发送到发送到发送到发送到发送到发送到发送到发送到发送到发萨是的发送到发送到发送到发送到发送到发送到发送到发送到发送到发萨是的发送到发送到发送到发送到发送到发送到发送到发送到发送到发萨是的发送到发送到发送到发送到发送到发送到发送到发送到发送到发萨是的发送到发送到发送到发送到发送到发送到发送到发送到发送到发萨是的发送到发送到发送到发送到发送到发送到发送到发送到发送到发萨是的发送到发送到发送到发送到发送到发送到发送到发送到发送到发萨是的发送到发送到发送到发送到发送到发送到发送到发送到发送到发萨是的发送到发送到发送到发送到发送到发送到发送到发送到发送到发萨是的发送到发送到发送到发送到发送到发送到发送到发送到发送到发萨是的发送到发送到发送到发送到发送到发送到发送到发送到发送到发萨是的发送到发送到发送到发送到发送到发送到发送到发送到发送到发萨是的发送到发送到发送到发送到发送到发送到发送到发送到发送到发萨是的发送到发送到发送到发送到发送到发送到发送到发送到发送到发萨是的发送到发送到发送到发送到发送到发送到发送到发送到发送到发萨是的发送到发送到发送到发送到发送到发送到发送到发送到发送到发萨是的发送到发送到发送到发送到发送到发送到发送到发送到发送到发萨是的发送到发送到发送到发送到发送到发送到发送到发送到发送到发萨是的发送到发送到发送到发送到发送到发送到发送到发送到发送到发萨是的发送到发送到发送到发送到发送到发送到发送到发送到发送到发萨是的发送到发送到发送到发送到发送到发送到发送到发送到发送到发萨是的发送到发送到发送到发送到发送到发送到发送到发送到发送到发萨是的发送到发送到发送到发送到发送到发送到发送到发送到发送到发萨是的发送到发送到发送到发送到发送到发送到发送到发送到发送到发萨是的发送到发送到发送到发送到发送到发送到发送到发送到发送到发萨是的发送到发送到发送到发送到发送到发送到发送到发送到发送到发萨是的发送到发送到发送到发送到发送到发送到发送到发送到发送到发萨是的发送到发送到发送到发送到发送到发送到发送到发送到发送到发萨是的发送到发送到发送到发送到发送到发送到发送到发送到发送到发萨是的发送到发送到发送到发送到发送到发送到发送到发送到发送到发到发是的发送到发送到发送到发送到发送到发送到发送到发送到发送萨是的发送到发送到发送到发送到发送到发送到发送到发送到发送到发萨是的发送到发送到发送到发送到发送到发送到发送到发送到发送到发萨是的发送到发送到发送到发送到发送到发送到发送到发送到发送到发萨是的发送到发送到发送到发送到发送到发送到发送到发送到发送到发萨是的发送到发送到发送到发送到发送到发送到发送到发送到发送到发萨是的发送到发送到发送到发送到发送到发送到发送到发送到发送到发萨是的发送到发送到发送到发送到发送到发送到发送到发送到发送到发萨是的发送到发送到发送到发送到发送到发送到发送到发送到发送到发萨是的发送到发送到发送到发送到发送到发送到发送到发送到发送到发萨是的发送到发送到发送到发送到发送到发送到发送到发送到发送到发萨是的发送到发送到发送到发送到发送到发送到发送到发送到发送到发萨是的发送到发送到发送到发送到发送到发送到发送到发送到发送到发萨是的发送到发送到发送到发送到发送到发送到发送到发送到发送到发萨是的发送到发送到发送到发送到发送到发送到发送到发送到发送到发萨是的发送到发送到发送到发送到发送到发送到发送到发送到发送到发萨是的发送到发送到发送到发送到发送到发送到发送到发送到发送到发萨是的发送到发送到发送到发送到发送到发送到发送到发送到发送到发萨是的发送到发送到发送到发送到发送到发送到发送到发送到发送到发萨是的发送到发送到发送到发送到发送到发送到发送到发送到发送到发萨是的发送到发送到发送到发送到发送到发送到发送到发送到发送到发萨是的发送到发送到发送到发送到发送到发送到发送到发送到发送到发萨是的发送到发送到发送到发送到发送到发送到发送到发送到发送到发萨是的发送到发送到发送到发送到发送到发送到发送到发送到发送到发萨是的发送到发送到发送到发送到发送到发送到发送到发送到发送到发萨是的发送到发送到发送到发送到发送到发送到发送到发送到发送到发萨是的发送到发送到发送到发送到发送到发送到发送到发送到发送到发萨是的发送到发送到发送到发送到发送到发送到发送到发送到发送到发萨是的发送到发送到发送到发送到发送到发送到发送到发送到发送到发萨是的发送到发送到发送到发送到发送到发送到发送到发送到发送到发萨是的发送到发送到发送到发送到发送到发送到发送到发送到发送到发萨是的发送到发送到发送到发送到发送到发送到发送到发送到发送到发萨是的发送到发送到发送到发送到发送到发送到发送到发送到发送到发到发"
        )
        this.addView(
            editText,
            LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        )
    }


    fun bringPointIntoView(): Boolean {

        val offset = editText.selectionStart
        editText.bringPointIntoView(offset)

        return true
    }

    inner class NestedScrollEditTextInner @JvmOverloads constructor(
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
                parentRect.bottom = parent.scrollY + parent.height + bottomGapPx
                parent.requestRectangleOnScreen(parentRect)
            }
            return scrolled
        }

    }


}