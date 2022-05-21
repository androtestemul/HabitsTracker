package com.apska.habitstracker.domain.usecases

import com.apska.habitstracker.domain.HabitsRepository
import com.apska.habitstracker.domain.model.Habit
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class UpdateHabitUseCase(private val habitsRepository: HabitsRepository,
                         private val dispatcher: CoroutineDispatcher
) {

    suspend fun updateHabit(habit: Habit) = withContext(dispatcher) {
        habitsRepository.updateHabit(habit)
    }

}