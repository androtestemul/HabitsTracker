package com.apska.habitstracker.repository

import com.apska.habitstracker.model.Habit
import com.apska.habitstracker.model.HabitPriority

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

    fun getFilteredHabits(habitHeader: String? = null, habitPriority: HabitPriority? = null) : ArrayList<Habit> {
        return habits.filter { habit ->
            val priorityFilter = if (habitPriority == null) {
                true
            } else {
                habit.priority == habitPriority
            }

            val headerFilter = if (habitHeader == null) {
                true
            } else {
                habit.header.contains(habitHeader, true)
            }

            headerFilter && priorityFilter
        } as ArrayList<Habit>
    }
}