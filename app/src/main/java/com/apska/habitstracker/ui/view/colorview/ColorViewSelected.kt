package com.apska.habitstracker.ui.view.colorview

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import kotlin.math.min

open class ColorViewSelected(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : ColorViewFramed(context, attrs, defStyleAttr), ColorViewSelectable {

    override var isViewSelected = false
        set(value) {
            field = value
            invalidate()
        }

    override var radius = 0.0f

    override fun onSizeChanged(width: Int, height: Int, oldWidth: Int, oldHeight: Int) {
        super.onSizeChanged(width, height, oldWidth, oldHeight)

        radius = (min(width, height) / 2.0 * 0.25).toFloat()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        if (isViewSelected) {
            canvas.drawCircle((width / 2).toFloat(), (height / 2).toFloat(), radius, paint)

        }
    }

}