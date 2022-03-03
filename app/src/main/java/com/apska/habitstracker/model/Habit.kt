package com.apska.habitstracker.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Habit(
    val header: String,
    val description: String,
    val priority: HabitPriority,
    val type: HabitType,
    val executeCount: Int,
    val period: String,
    val color: String = "#ff0000"
) : Parcelable
