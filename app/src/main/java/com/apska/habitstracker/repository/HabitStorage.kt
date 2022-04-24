package com.apska.habitstracker.repository

import com.apska.habitstracker.model.Habit
import com.apska.habitstracker.model.HabitPriority

object HabitStorage {

    var currentSortDirection = HabitSort.NONE

    private val habits = arrayListOf<Habit>()

    fun getAllHabits() = habits

    fun getHabit(habitId: Long) = habits.firstOrNull { it.id == habitId }

    fun addHabit(habit: Habit) {
        habits.add(habit)
        applyCurrentSort()
    }

    fun updateHabit(habit: Habit) {
        for (index in 0..habits.size) {
            if (habits[index].id == habit.id) {
                habits[index] = habit
                break
            }
        }

        applyCurrentSort()
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

    private fun applyCurrentSort() {
        if (currentSortDirection == HabitSort.SORT_ASC) {
            habits.sortBy { it.period }
        } else if (currentSortDirection == HabitSort.SORT_DESC) {
            habits.sortByDescending { it.period }
        }
    }

    fun sortHabitByPeriod() : ArrayList<Habit> {
        if (currentSortDirection == HabitSort.SORT_DESC || currentSortDirection == HabitSort.NONE) {
            habits.sortBy { it.period }
            currentSortDirection = HabitSort.SORT_ASC
        } else if (currentSortDirection == HabitSort.SORT_ASC) {
            habits.sortByDescending { it.period }
            currentSortDirection = HabitSort.SORT_DESC
        }

        return habits
    }
}