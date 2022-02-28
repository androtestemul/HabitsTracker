package com.apska.habitstracker.model

import com.apska.habitstracker.R

sealed class HabitType {
    abstract val name: String
}

class Bad : HabitType() {
    override val name = R.string.habit_type_bad.toString()
}

class Good : HabitType() {
    override val name = R.string.habit_type_good.toString()
}

class Neutral : HabitType() {
    override val name = R.string.habit_type_neutral.toString()
}