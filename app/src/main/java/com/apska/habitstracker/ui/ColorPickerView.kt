package com.apska.habitstracker.ui

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import kotlin.math.min


class ColorPickerView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    companion object {
        const val STROKE_WIDTH = 6f
    }

    var isViewSelected = false
        set(value) {
            field = value
            invalidate()
        }

    var canvasBackgroundColor = Color.WHITE
        set(value) {
            field = value
            fillColorsRGB(value)
            invalidate()
        }

    private var backgroundRed = 0
    private var backgroundGreen = 0
    private var backgroundBlue = 0


    private val paint = Paint()
    private var rectF = RectF(STROKE_WIDTH/2,STROKE_WIDTH/2,60f,60f)
    private var radius = 0.0f

    init {
        canvasBackgroundColor = Color.WHITE

        paint.strokeWidth = STROKE_WIDTH
        paint.style = Paint.Style.STROKE
    }

    constructor(
        context: Context,
        layoutParams: LinearLayout.LayoutParams,
        backgroundColor: Int
    ) : this(context) {

        canvasBackgroundColor = backgroundColor

        this.layoutParams = layoutParams
    }

    override fun onSizeChanged(width: Int, height: Int, oldWidth: Int, oldHeight: Int) {
        radius = (min(width, height) / 2.0 * 0.25).toFloat()

        rectF = RectF(
            STROKE_WIDTH / 2,
            STROKE_WIDTH / 2,
            width.toFloat() - STROKE_WIDTH / 2,
            height.toFloat() - STROKE_WIDTH / 2)
    }

    override fun onDraw(canvas: Canvas) {
        canvas.drawRGB(backgroundRed, backgroundGreen, backgroundBlue)
        canvas.drawRect(rectF, paint)

        if (isViewSelected) {
            canvas.drawCircle((width / 2).toFloat(), (height / 2).toFloat(), radius, paint)
        }
    }

    private fun fillColorsRGB(color: Int) {
        backgroundBlue = Color.blue(color)
        backgroundGreen = Color.green(color)
        backgroundRed = Color.red(color)
    }


}
