package com.apska.habitstracker.ui.view.colorpicker

import android.app.Activity
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.view.setMargins
import com.apska.habitstracker.getScreenWidth
import com.apska.habitstracker.ui.view.colorview.ColorView

class ColorPicker(
    activity: Activity,
    private val parent: ViewGroup,
    root: View
) {

    fun interface OnColorClickListener {
        fun onColorClick(colorPickerView: ColorPickerView)
    }

    companion object {
        private const val SQUARE_TOTAL_COUNT = 16
        private const val HSV_HUE_TOTAL_DEGREE = 360
        private const val HSV_SATURATION = 100f
        private const val HSV_VALUE = 100f
        private const val HSV_HUE_INDEX = 0
        private const val HSV_SATURATION_INDEX = 1
        private const val HSV_VALUE_INDEX = 2
        private const val HSV_COLOR_TO_INT_MULTIPLIER = 100
        private const val SQUARE_WIDTH_MULTIPLIER = 4
        private const val SQUARE_ON_SCREEN_COUNT = 4
        private const val DIVIDER_CENTER_OF_SQUARE = 2
        private const val DIVIDER_FOR_SQUARE_MARGIN = 6

    }

    var selectedColor: Int = ColorView.DEFAULT_COLOR
        set(value) {
            field = value
            val selectedColorIndex = intColors.indexOf(value)

            if (selectedColorIndex >= 0) {
                (parent.getChildAt(selectedColorIndex) as ColorPickerView).apply {
                    isViewSelected = true
                    invalidate()
                }
            }
        }

    var onColorClickListener: OnColorClickListener? = null

    private val startDegree: Float = HSV_HUE_TOTAL_DEGREE / SQUARE_TOTAL_COUNT.toFloat() / DIVIDER_CENTER_OF_SQUARE
    private val intColors = ArrayList<Int>(SQUARE_TOTAL_COUNT)

    init {

        for (i in 0 until SQUARE_TOTAL_COUNT) {
            val degree = startDegree + (startDegree * DIVIDER_CENTER_OF_SQUARE * i)
            val hueColor = Color.HSVToColor(floatArrayOf(degree, HSV_SATURATION, HSV_VALUE))
            intColors.add(hueColor)
        }

        val drawable = GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT,
            intArrayOf(Color.RED, *intColors.toIntArray(), Color.RED))

        drawable.shape = GradientDrawable.RECTANGLE
        drawable.gradientType = GradientDrawable.LINEAR_GRADIENT

        parent.background = drawable

        val pickerWidth =
            activity.getScreenWidth() - root.paddingLeft - root.paddingRight
        val squareMarginWidth: Int = pickerWidth / SQUARE_ON_SCREEN_COUNT / DIVIDER_FOR_SQUARE_MARGIN
        val squareWidth: Int = squareMarginWidth * SQUARE_WIDTH_MULTIPLIER

        val layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT)
        layoutParams.setMargins(squareMarginWidth)
        layoutParams.width = squareWidth
        layoutParams.height = squareWidth

        for (i in 0 until SQUARE_TOTAL_COUNT) {
            val squareView = ColorPickerView(activity, layoutParams)

            squareView.canvasBackgroundColor = intColors[i]

            squareView.setOnClickListener {
                val colorPickerView = it as ColorPickerView

                if (colorPickerView.isViewSelected) {
                    return@setOnClickListener
                }

                //Снимаем флаг isViewSelected с предыдущей выбранной View
                if (selectedColor != ColorView.DEFAULT_COLOR) {
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

    fun selectedColorAsRgb() = "${Color.red(selectedColor)} ${Color.green(selectedColor)} ${Color.blue(selectedColor)}"

    fun selectedColorAsHsv() : String {
        val hsvColorArr = FloatArray(3)
        Color.colorToHSV(selectedColor, hsvColorArr)

        return "${hsvColorArr[HSV_HUE_INDEX].toInt()} " +
               "${hsvColorArr[HSV_SATURATION_INDEX].toInt() * HSV_COLOR_TO_INT_MULTIPLIER} " +
               "${hsvColorArr[HSV_VALUE_INDEX].toInt() * HSV_COLOR_TO_INT_MULTIPLIER}"
    }
}