package com.swithun.nestedscrolledittext

import android.text.Spannable
import android.text.method.ArrowKeyMovementMethod
import android.view.MotionEvent
import android.widget.TextView

class NestedScrollArrowKeyMovementMethod: ArrowKeyMovementMethod() {

    override fun onTouchEvent(widget: TextView?, buffer: Spannable?, event: MotionEvent?): Boolean {
        return super.onTouchEvent(widget, buffer, event)
    }

}