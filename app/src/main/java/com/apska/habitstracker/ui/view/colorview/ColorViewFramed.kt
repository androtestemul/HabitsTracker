package com.apska.habitstracker.ui.view.colorview

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View

open class ColorViewFramed @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr), ColorViewFrameable {

    override val paint = Paint().also {
        it.strokeWidth = ColorView.DEFAULT_STROKE_WIDTH
        it.style = Paint.Style.STROKE
    }

    override var rectF = RectF(0f,0f,0f,0f)

    override var canvasBackgroundColor = ColorView.DEFAULT_COLOR
        set(value) {
            field = value
            this.setBackgroundColor(value)
        }

    override fun onSizeChanged(width: Int, height: Int, oldWidth: Int, oldHeight: Int) {
        rectF = RectF(
            ColorView.DEFAULT_STROKE_WIDTH / 2,
            ColorView.DEFAULT_STROKE_WIDTH / 2,
            width.toFloat() - ColorView.DEFAULT_STROKE_WIDTH / 2,
            height.toFloat() - ColorView.DEFAULT_STROKE_WIDTH / 2)
    }

    override fun onDraw(canvas: Canvas) {
        canvas.drawRect(rectF, paint)
    }
}