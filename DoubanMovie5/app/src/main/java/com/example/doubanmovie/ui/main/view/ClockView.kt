package com.example.doubanmovie.ui.main.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import java.util.Calendar
import java.util.Timer
import java.util.TimerTask
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.sin

/**
 * TODO: document your custom view class.
 */
class ClockView(context: Context, attrs: AttributeSet) : View(context, attrs) {

    private var mCenterX: Float = 0F
    private var mCenterY: Float = 0F
    private lateinit var mPaint: Paint
    private lateinit var mCalendar: Calendar

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val len = min(measuredWidth, measuredHeight)
        mCalendar = Calendar.getInstance()
        mPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        drawPlate(canvas, len)
        drawPoints(canvas, len)
    }

    private fun drawPlate(canvas: Canvas, len: Int) {
        mCenterX = measuredWidth / 2.toFloat()
        mCenterY = measuredHeight / 2.toFloat()

        val r = (len / 2).toFloat()
        canvas.drawCircle(mCenterX, mCenterY, r, mPaint)

        for (i in 0..59) {
            mPaint.color = Color.CYAN
            if (i % 5 == 0) {
                mPaint.strokeWidth = 8F
                canvas.drawLine(
                    mCenterX, mCenterY - r, mCenterX, mCenterY - r + 30F, mPaint
                )
                mPaint.textSize = 70F
                canvas.drawText(((i + 1) / 5).toString(), mCenterX, mCenterY - r + 80F, mPaint)
            } else {
                mPaint.strokeWidth = 3F
                canvas.drawLine(
                    mCenterX, mCenterY - r, mCenterX, mCenterY - r + 20F, mPaint
                )
            }
            canvas.rotate(6F, mCenterX, mCenterY)
        }
    }

    private fun drawPoints(canvas: Canvas, len: Int) {
        mCalendar.timeInMillis = System.currentTimeMillis()
        val hour = mCalendar.get(Calendar.HOUR) % 12
        val minute = mCalendar.get(Calendar.MINUTE)
        val second = mCalendar.get(Calendar.SECOND)
        canvas.rotate(-90F, mCenterX, mCenterY)
        drawHourPoint(canvas, hour, minute, len / 2)
        drawMinutePoint(canvas, minute, second, len / 2)
        drawSecondPoint(canvas, second, len / 2)
    }

    private fun drawMinutePoint(canvas: Canvas, minute: Int, second: Int, r: Int) {
        val degree = 360 / 60 * (minute + second / 60F)
        val radians = Math.toRadians(degree.toDouble())
        mPaint.color = Color.BLUE
        mPaint.strokeWidth = 10F

        val endX = mCenterX + r * cos(radians) * 0.8
        val endY = mCenterY + r * sin(radians) * 0.8
        canvas.drawLine(mCenterX, mCenterY, endX.toFloat(), endY.toFloat(), mPaint)
    }

    private fun drawSecondPoint(canvas: Canvas, second: Int, r: Int) {
        val degree = 360 / 60 * second
        val radians = Math.toRadians(degree.toDouble())
        mPaint.color = Color.RED
        mPaint.strokeWidth = 5F

        val endX = mCenterX + r * cos(radians)
        val endY = mCenterY + r * sin(radians)
        canvas.drawLine(mCenterX, mCenterY, endX.toFloat(), endY.toFloat(), mPaint)
    }

    private fun drawHourPoint(canvas: Canvas, hour: Int, minute: Int, r: Int) {
        val degree = 360 / 12 * (hour + minute / 60F)
        val radians = Math.toRadians(degree.toDouble())
        mPaint.color = Color.GREEN
        mPaint.strokeWidth = 5F

        val endX = mCenterX + r * cos(radians) * 0.7
        val endY = mCenterY + r * sin(radians) * 0.7
        canvas.drawLine(mCenterX, mCenterY, endX.toFloat(), endY.toFloat(), mPaint)
    }

    fun start() {
        Timer().schedule(object : TimerTask() {
            override fun run() {
                postInvalidate()
            }

        }, 0, 1000)
    }

}