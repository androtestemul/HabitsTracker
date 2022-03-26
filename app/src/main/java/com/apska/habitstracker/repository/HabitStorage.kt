package com.apska.habitstracker.repository

import com.apska.habitstracker.model.Habit

object HabitStorage {
    private val habits = arrayListOf<Habit>()

    fun getAllHabits() = habits

    fun getHabit(index: Int) = habits[index]

    fun addHabit(habit: Habit) {
        habits.add(habit)
    }

    fun updateHabit(habit: Habit) {
        habits[habit.id] = habit
    }
}