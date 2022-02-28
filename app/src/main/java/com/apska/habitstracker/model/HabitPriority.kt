package com.apska.habitstracker.model

import com.apska.habitstracker.R

sealed class HabitPriority{
    abstract val priority: Int
    abstract val name: String
}

class High : HabitPriority() {
    override val priority = 1000
    override val name = R.string.habit_priority_high.toString()
}

class Middle : HabitPriority() {
    override val priority = 500
    override val name = R.string.habit_priority_middle.toString()
}
class Low : HabitPriority() {
    override val priority: Int = 1
    override val name = R.string.habit_priority_low.toString()
}

