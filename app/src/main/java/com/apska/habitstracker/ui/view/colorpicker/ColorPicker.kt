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

        return "${hsvColorArr[0].toInt()} ${hsvColorArr[1].toInt() * 100} ${hsvColorArr[2].toInt() * 100}"
    }
}