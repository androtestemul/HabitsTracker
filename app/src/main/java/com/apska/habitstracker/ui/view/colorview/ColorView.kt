package com.apska.habitstracker.ui.view.colorview

import android.graphics.Color
import android.graphics.Paint

interface ColorView {

    companion object {
        const val DEFAULT_COLOR = Color.WHITE
        const val DEFAULT_STROKE_WIDTH = 6f
    }

    val paint: Paint

    var canvasBackgroundColor: Int
}