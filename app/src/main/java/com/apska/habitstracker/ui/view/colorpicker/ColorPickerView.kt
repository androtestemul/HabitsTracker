package com.apska.habitstracker.ui.view.colorpicker

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout
import com.apska.habitstracker.ui.view.colorview.ColorViewSelectable
import com.apska.habitstracker.ui.view.colorview.ColorViewSelected


class ColorPickerView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ColorViewSelected(context, attrs, defStyleAttr), ColorViewSelectable {

    constructor(
        context: Context,
        layoutParams: LinearLayout.LayoutParams
    ) : this(context) {

        this.layoutParams = layoutParams
    }

}
