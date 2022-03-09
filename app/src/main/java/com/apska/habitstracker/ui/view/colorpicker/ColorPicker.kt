package com.apska.habitstracker.ui.view.colorpicker

import android.app.Activity
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.view.setMargins
import com.apska.habitstracker.getScreenWidth
import com.apska.habitstracker.ui.view.colorview.ColorViewSelectable

class ColorPicker(
    activity: Activity,
    parent: ViewGroup,
    root: View
) {

    fun interface OnColorClickListener {
        fun onColorClick(colorPickerView: ColorPickerView)
    }

    var selectedColor: Int = Color.WHITE
    var onColorClickListener: OnColorClickListener? = null

    private val squareTotalCount = 16
    private val startDegree: Float = 360 / squareTotalCount.toFloat() / 2
    private val intColors = ArrayList<Int>(squareTotalCount)

    init {

        for (i in 0 until squareTotalCount) {
            val degree = startDegree + (startDegree * 2 * i)
            val hueColor = Color.HSVToColor(floatArrayOf(degree, 100f, 100f))
            intColors.add(hueColor)
        }

        val drawable = GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT,
            intArrayOf(Color.RED, *intColors.toIntArray(), Color.RED))

        drawable.shape = GradientDrawable.RECTANGLE
        drawable.gradientType = GradientDrawable.LINEAR_GRADIENT

        parent.background = drawable

        val squareOnScreenCount = 4
        val pickerWidth =
            activity.getScreenWidth() - root.paddingLeft - root.paddingRight
        val squareMarginWidth: Int = pickerWidth / squareOnScreenCount / 6
        val squareWidth: Int = squareMarginWidth * 4

        val layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT)
        layoutParams.setMargins(squareMarginWidth)
        layoutParams.width = squareWidth
        layoutParams.height = squareWidth

        for (i in 0 until squareTotalCount) {
            val squareView = ColorPickerView(activity, layoutParams)

            squareView.apply {
                canvasBackgroundColor = intColors[i]

                if (canvasBackgroundColor == selectedColor) {
                    isViewSelected = true
                }
            }

            squareView.setOnClickListener {
                val colorPickerView = it as ColorPickerView

                if (colorPickerView.isViewSelected) {
                    return@setOnClickListener
                }

                //Снимаем флаг isViewSelected с предыдущей выбранной View
                if (selectedColor != Color.WHITE) {
                    (parent.getChildAt(intColors.indexOf(selectedColor)) as ColorPickerView).isViewSelected =
                        false
                }

                selectedColor = colorPickerView.canvasBackgroundColor
                colorPickerView.isViewSelected = true

                onColorClickListener?.onColorClick(colorPickerView)
            }

            parent.addView(squareView)
        }
    }
}