package com.apska.habitstracker.model

import android.content.Context
import android.os.Parcelable
import com.apska.habitstracker.R
import kotlinx.parcelize.Parcelize

@Parcelize
enum class HabitType(private val value: Int) : Parcelable {
    BAD(R.string.habit_type_bad),
    GOOD(R.string.habit_type_good),
    NEUTRAL(R.string.habit_type_neutral);

    fun getTextValue(context: Context) = context.getString(value)

}