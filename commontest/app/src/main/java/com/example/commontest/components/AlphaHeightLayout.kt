package com.example.commontest.components

import android.animation.ValueAnimator
import android.content.Context
import android.util.AttributeSet
import androidx.constraintlayout.widget.ConstraintLayout

class AlphaHeightLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private val animator: ValueAnimator = ValueAnimator()
    private val animateTime = 2000L
    private var progress: Float = 1f

    init {
        animator.addUpdateListener { valueAnimator ->
            (valueAnimator.animatedValue as Float).let { progress ->
                this.progress = progress
                this.alpha = progress
                this.requestLayout()
            }
        }
    }

    fun toInvisible() {
        animator.cancel()
        val currentProgress = this.alpha.coerceIn(0f, 1f)
        animator.setFloatValues(currentProgress, 0f)
        val duration = currentProgress * animateTime
        animator.setDuration(duration.toLong())
        animator.start()
    }

    fun toVisible() {
        animator.cancel()
        val currentProgress = this.alpha.coerceIn(0f, 1f)
        animator.setFloatValues(currentProgress, 1f)
        val duration = (1 - currentProgress) * animateTime
        animator.setDuration(duration.toLong())
        animator.start()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val height = MeasureSpec.makeMeasureSpec(
            (this.measuredHeight * progress).toInt(),
            MeasureSpec.EXACTLY
        )
        setMeasuredDimension(widthMeasureSpec, height)
    }
}