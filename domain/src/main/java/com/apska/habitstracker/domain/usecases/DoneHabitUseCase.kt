package com.apska.habitstracker.domain.usecases

import com.apska.habitstracker.domain.HabitsRepository
import com.apska.habitstracker.domain.model.Habit
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class DoneHabitUseCase(private val habitsRepository: HabitsRepository,
                       private val dispatcher: CoroutineDispatcher
) {

    suspend fun doneHabit(habit: Habit) = withContext(dispatcher) {
        habitsRepository.doneHabit(habit)
    }

}