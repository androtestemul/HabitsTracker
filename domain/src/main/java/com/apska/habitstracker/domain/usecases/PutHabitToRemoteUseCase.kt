package com.apska.habitstracker.domain.usecases

import com.apska.habitstracker.domain.HabitsRepository
import com.apska.habitstracker.domain.model.Habit
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class PutHabitToRemoteUseCase(private val habitsRepository: HabitsRepository,
                              private val dispatcher: CoroutineDispatcher
) {

    suspend fun putHabitToRemote(habit: Habit) = withContext(dispatcher) {
        habitsRepository.putHabitToRemote(habit)
    }

}