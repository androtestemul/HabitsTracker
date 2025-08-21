package com.apska.habitstracker.presentation

import android.app.Activity
import android.content.Context
import android.graphics.Insets
import android.os.Build
import android.util.DisplayMetrics
import android.view.WindowInsets
import com.apska.habitstracker.domain.model.HabitPriority
import com.apska.habitstracker.domain.model.HabitType

fun Activity.getScreenWidth(): Int {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        val windowMetrics = this.windowManager.currentWindowMetrics
        val insets: Insets = windowMetrics.windowInsets
            .getInsetsIgnoringVisibility(WindowInsets.Type.systemBars())
        windowMetrics.bounds.width() - insets.left - insets.right
    } else {
        val displayMetrics = DisplayMetrics()
        @Suppress("DEPRECATION")
        this.windowManager.defaultDisplay.getMetrics(displayMetrics)
        displayMetrics.widthPixels
    }
}

fun HabitType.getTextValue(context: Context) : String =
    when(this) {
        HabitType.BAD -> context.getString(R.string.habit_type_bad)
        HabitType.GOOD -> context.getString(R.string.habit_type_good)
        HabitType.NEUTRAL -> context.getString(R.string.habit_type_neutral)
        else -> {""}
    }

fun HabitPriority.getTextValue(context: Context) : String =
    when(this) {
        HabitPriority.HIGH -> context.getString(R.string.habit_priority_high)
        HabitPriority.MIDDLE -> context.getString(R.string.habit_priority_middle)
        HabitPriority.LOW -> context.getString(R.string.habit_priority_low)
        else -> {""}
    }