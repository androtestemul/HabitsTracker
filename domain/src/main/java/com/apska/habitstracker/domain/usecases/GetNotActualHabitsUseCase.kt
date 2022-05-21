package com.apska.habitstracker.domain.usecases

import com.apska.habitstracker.domain.HabitsRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class GetNotActualHabitsUseCase(private val habitsRepository: HabitsRepository,
                                private val dispatcher: CoroutineDispatcher
) {

    suspend fun getNotActualHabits() = withContext(dispatcher) {
        habitsRepository.getNotActualHabits()
    }

}