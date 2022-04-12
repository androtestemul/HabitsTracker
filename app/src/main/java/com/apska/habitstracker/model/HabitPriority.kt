package com.apska.habitstracker.model

import android.content.Context
import android.os.Parcelable
import com.apska.habitstracker.R
import kotlinx.parcelize.Parcelize



enum class HabitPriority(private val value: Int) {
    HIGH(R.string.habit_priority_high),
    MIDDLE(R.string.habit_priority_middle),
    LOW(R.string.habit_priority_low);

    fun getTextValue(context: Context) = context.getString(this.value)
}