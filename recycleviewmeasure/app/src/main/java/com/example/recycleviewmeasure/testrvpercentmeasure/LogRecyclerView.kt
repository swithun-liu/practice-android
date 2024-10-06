package com.example.recycleviewmeasure.testrvpercentmeasure

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.View.MeasureSpec
import androidx.recyclerview.widget.RecyclerView

class LogRecyclerView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttrs: Int = 0
) : RecyclerView(context, attrs, defStyleAttrs) {
    override fun onMeasure(widthSpec: Int, heightSpec: Int) {
        Log.d(
            "swithun-xxxx",
            "#LogRecyclerView#onMeasure WidthMode: ${widthSpec.getModeStr()} WidthSize: ${widthSpec.getSize()} HeightMode: ${heightSpec.getModeStr()} HeightSize: ${heightSpec.getSize()}"
        )
        super.onMeasure(widthSpec, heightSpec)
    }
}


fun Int.getModeStr() = when (MeasureSpec.getMode(this)) {
    MeasureSpec.AT_MOST -> "AT_MOST"
    MeasureSpec.EXACTLY -> "EXACTLY"
    MeasureSpec.UNSPECIFIED -> "UNSPECIFIED"
    else -> "ERROR"
}

fun Int.getSize() = MeasureSpec.getSize(this)