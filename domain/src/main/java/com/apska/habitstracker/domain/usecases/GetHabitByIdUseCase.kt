package com.apska.habitstracker.domain.usecases

import com.apska.habitstracker.domain.HabitsRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class GetHabitByIdUseCase(private val habitsRepository: HabitsRepository,
                          private val dispatcher: CoroutineDispatcher
) {

    suspend fun getHabitById(id: Long) = withContext(dispatcher) {
        habitsRepository.getHabitById(id)
    }

}