package com.apska.habitstracker.domain

import com.apska.habitstracker.domain.model.Habit
import com.apska.habitstracker.domain.model.HabitPriority
import com.apska.habitstracker.domain.model.PutHabitResponse
import kotlinx.coroutines.flow.Flow

interface HabitsRepository {

    fun getAllHabits() : Flow<List<Habit>>

    suspend fun getHabitById(id: Long) : Habit?

    suspend fun getNotActualHabits() : List<Habit>

    suspend fun insertHabit(habit: Habit)

    suspend fun updateHabit(habit: Habit)

    fun getFilteredSortedHabit(
        habitHeader: String,
        habitPriority: HabitPriority?,
        sortOrder: Int
    ) : Flow<List<Habit>>

    suspend fun updateHabitsFromRemote() : Boolean

    suspend fun putHabitToRemote(habit: Habit) : String?
}