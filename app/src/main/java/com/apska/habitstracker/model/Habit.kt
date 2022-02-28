package com.apska.habitstracker.model

import android.graphics.Color

data class Habit(
    val header: String,
    val description: String,
    val priority: HabitPriority,
    val type: HabitType,
    val executeCount: Int,
    val period: String,
    val color: Color
)
