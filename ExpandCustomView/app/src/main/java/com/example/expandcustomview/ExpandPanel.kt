package com.example.expandcustomview

import android.animation.Animator
import android.animation.ValueAnimator
import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView


//https://www.cnblogs.com/JczmDeveloper/p/3782586.html

class ExpandPanel @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {
    private var expandAnimation = ValueAnimator()
    private var closeAnimation = ValueAnimator()

    var state = ExpandPanelState.CLOSED
        private set(value) {
            field = value
            stateChangeListener?.notifyNewState(value)
        }

    var stateChangeListener: StateChangeListener? = null

    var animationTime = 300L

    private val bg: LinearLayout = LinearLayout(context).also {
        it.setBackgroundColor(resources.getColor(com.google.android.material.R.color.material_blue_grey_800))
        it.orientation = LinearLayout.VERTICAL
    }

    private var lastY = 0f
    private var virtualTranslationY = 0f

    private val touchBar: LinearLayout = LinearLayout(context).also {
        it.setBackgroundColor(resources.getColor(com.google.android.material.R.color.abc_btn_colored_text_material))
        it.orientation = LinearLayout.VERTICAL
        it.addView(TextView(context).also {
            it.text = "touch me"
        })
    }

    private var closeAnimatorListener = object : Animator.AnimatorListener {
        override fun onAnimationStart(animation: Animator) {
            state = ExpandPanelState.CLOSING
        }

        override fun onAnimationEnd(animation: Animator) {
            state = ExpandPanelState.CLOSED
        }

        override fun onAnimationCancel(animation: Animator) {
        }

        override fun onAnimationRepeat(animation: Animator) {}

    }

    private var expandAnimatorListener = object : Animator.AnimatorListener {
        override fun onAnimationStart(animation: Animator) {
            state = ExpandPanelState.OPENING
        }

        override fun onAnimationEnd(animation: Animator) {
            state = ExpandPanelState.OPENED
        }

        override fun onAnimationCancel(animation: Animator) {
        }

        override fun onAnimationRepeat(animation: Animator) {}
    }

    init {
        for (i in 0..4) {
            val tv = TextView(context).also {
                it.text = "haha${i}"
                it.setTextColor(resources.getColor(R.color.white))
            }
            bg.addView(tv)
        }
        val layoutParams = FrameLayout.LayoutParams(
            FrameLayout.LayoutParams.MATCH_PARENT,
            FrameLayout.LayoutParams.WRAP_CONTENT
        )
        bg.addView(touchBar, layoutParams)
        addView(bg, layoutParams)

        touchBar.setOnTouchListener(::handelTouchBarTouch)
        expandAnimation.addUpdateListener(::onAnimationUpdate)
        closeAnimation.addUpdateListener(::onAnimationUpdate)

        expandAnimation.addListener(expandAnimatorListener)
        closeAnimation.addListener(closeAnimatorListener)
    }

    private fun handelTouchBarTouch(view: View, motionEvent: MotionEvent): Boolean {
        when (motionEvent.action) {
            MotionEvent.ACTION_DOWN -> {
                // 记录 内容最开始的偏移量
                virtualTranslationY = bg.translationY
                // 记录位置
                lastY = motionEvent.rawY
            }

            MotionEvent.ACTION_MOVE -> {
                // 新位置
                val newY = motionEvent.rawY
                // 计算移动距离
                val moveY = newY - lastY

                // 记录同步偏移后的内容偏移量
                virtualTranslationY += moveY
                when {
                    // 内容移动偏移量不能大于0，将其偏移量设置为0
                    virtualTranslationY > 0f -> bg.translationY = 0f
                    // 内容移动偏移量不能小于(-高度)，将其偏移量设置为(-高度)
                    virtualTranslationY < -height.toFloat() -> bg.translationY =
                        -height.toFloat()
                    // 其他情况合法
                    else -> bg.translationY = virtualTranslationY
                }
                // 记录位置
                lastY = newY
            }

            // 松手后自动动画
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                // 当松手时不是完全展开，就关闭（0可调整为其他阈值，比如总高度*百分之多少）
                if (bg.translationY < 0) {
                    close()
                } else {
                    expand()
                }
            }
        }
        return true
    }

    private fun onAnimationUpdate(valueAnimator: ValueAnimator) {
        val process = valueAnimator.animatedValue as? Float ?: return
        bg.translationY = (-bg.height).toFloat() * (1 - process)
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        when (state) {
            ExpandPanelState.CLOSING -> close()
            ExpandPanelState.OPENING -> expand()
            ExpandPanelState.CLOSED -> bg.translationY = (-bg.height).toFloat()
            ExpandPanelState.OPENED -> bg.translationY = 0f
        }
    }


    fun close() {
        closeAnimation.cancel()
        expandAnimation.cancel()
        val height = bg.height.takeUnless { it == 0 } ?: return
        val process = Math.max((height + bg.translationY) / height, 0f)
        closeAnimation.setFloatValues(process, 0f)
        closeAnimation.duration = ((process * animationTime).toLong())
        closeAnimation.start()
    }

    fun expand() {
        // 先停止所有动画
        closeAnimation.cancel()
        expandAnimation.cancel()
        // 当前内容(包含拖拽条)的高度
        val height = bg.height.takeUnless { it == 0 } ?: return
        // 当前展开占比
        val process = (height + bg.translationY) / height
        // 设置动画 开始-结束
        expandAnimation.setFloatValues(process, 1f)
        // 设置动画耗时
        expandAnimation.duration = ((1f - process) * animationTime).toLong()
        // 启动动画
        expandAnimation.start()
    }

    enum class ExpandPanelState {
        CLOSING,
        OPENING,
        CLOSED,
        OPENED
    }

    fun interface StateChangeListener {
        fun notifyNewState(state: ExpandPanelState)
    }

}

