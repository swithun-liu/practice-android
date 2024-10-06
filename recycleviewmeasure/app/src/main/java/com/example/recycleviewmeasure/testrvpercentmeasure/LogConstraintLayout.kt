package com.example.recycleviewmeasure.testrvpercentmeasure

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout

open class LogConstraintLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttrs: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttrs) {

    override fun onMeasure(widthSpec: Int, heightSpec: Int) {
        this.logOnMeasure(widthSpec, heightSpec)
        val heightSpec = MeasureSpec.makeMeasureSpec(
            ((this.parent!! as View).height * 0.4).toInt(),
            MeasureSpec.AT_MOST
        )
        super.onMeasure(widthSpec, heightSpec)
    }

}

fun ConstraintLayout.logOnMeasure(widthSpec: Int, heightSpec: Int) {
    Log.i(
        "swithun-xxxx",
        "#LogConstraintLayout#onMeasure WidthMode: ${widthSpec.getModeStr()} WidthSize: ${widthSpec.getSize()} HeightMode: ${heightSpec.getModeStr()} HeightSize: ${heightSpec.getSize()} parentHeight: ${(this.parent as LogRecyclerView).height}"
    )
}