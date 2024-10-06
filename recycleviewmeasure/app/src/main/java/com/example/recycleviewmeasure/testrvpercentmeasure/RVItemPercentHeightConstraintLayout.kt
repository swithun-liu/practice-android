package com.example.recycleviewmeasure.testrvpercentmeasure

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.recycleviewmeasure.R

class RVItemPercentHeightConstraintLayout @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttrs: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttrs) {

    var heightPercentOfParent: Float? = null
        set(value) {
            val oldValue = field
            field = value
            if (oldValue != value) {
                requestLayout()
            }
        }

    init {
        attrs?.let { nonNullAttrs ->
            val typedArray: TypedArray =
                context.obtainStyledAttributes(
                    nonNullAttrs,
                    R.styleable.RVItemPercentHeightConstraintLayout
                )
            heightPercentOfParent = typedArray.getFloat(
                R.styleable.RVItemPercentHeightConstraintLayout_percentHeightOfRV, -1f
            ).takeIf { xmlValue -> xmlValue != -1f }
            typedArray.recycle()
        }

    }

    override fun onMeasure(widthSpec: Int, heightSpec: Int) {
        this.logOnMeasure(widthSpec, heightSpec)

        val parent = this.parent as? RecyclerView
        val percent = heightPercentOfParent
        val handledHeightSpec = if (parent != null && percent != null) {
            MeasureSpec.makeMeasureSpec(
                (parent.height * percent).toInt(), MeasureSpec.EXACTLY
            )
        } else {
            heightSpec
        }

        super.onMeasure(widthSpec, handledHeightSpec)
    }
}