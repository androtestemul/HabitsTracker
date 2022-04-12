package com.apska.habitstracker.repository

import android.app.Application
import com.apska.habitstracker.model.Habit
import com.apska.habitstracker.model.HabitPriority
import com.apska.habitstracker.repository.database.HabitDatabase
import com.apska.habitstracker.repository.database.HabitsDao

class Repository(application: Application) {
    private val habitsDao: HabitsDao

    init {
        val database = HabitDatabase.getInstance(application)
        habitsDao = database.habitDatabaseDao
    }

    fun getAllHabits() = habitsDao.getAllHabits()

    fun getAllHabitsSorted(periodSortOrder: Int) = habitsDao.getAllHabitsSorted(periodSortOrder)

    fun getHabit(id: Long) = habitsDao.getHabit(id)

    fun insertHabit(habit: Habit) = habitsDao.insertHabit(habit)

    fun updateHabit(habit: Habit) = habitsDao.updateHabit(habit)

    fun getHeaderFilteredHabits(habitHeader: String, sortOrder: Int) =
        habitsDao.getHeaderFilteredHabits(habitHeader, sortOrder)

    fun getPriorityFilteredHabits(habitPriority: HabitPriority, sortOrder: Int) =
        habitsDao.getPriorityFilteredHabits(habitPriority, sortOrder)

    fun getFilteredSortedHabit(habitHeader: String, habitPriority: HabitPriority, sortOrder: Int) =
        habitsDao.getFilteredSortedHabits(habitHeader, habitPriority, sortOrder)

}