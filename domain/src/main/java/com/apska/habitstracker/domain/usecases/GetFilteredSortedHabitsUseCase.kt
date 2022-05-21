package com.apska.habitstracker.domain.usecases

import com.apska.habitstracker.domain.HabitsRepository
import com.apska.habitstracker.domain.model.HabitPriority
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class GetFilteredSortedHabitsUseCase(private val habitsRepository: HabitsRepository) {

    fun getFilteredSortedHabit(
        habitHeader: String,
        habitPriority: HabitPriority?,
        sortOrder: Int
    ) = habitsRepository.getFilteredSortedHabit(habitHeader, habitPriority, sortOrder)

}