package com.apska.habitstracker.domain.usecases

import com.apska.habitstracker.domain.HabitsRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class UpdateHabitsFromRemoteUseCase(private val habitsRepository: HabitsRepository,
                                    private val dispatcher: CoroutineDispatcher
) {

    suspend fun updateHabitsFromRemote() = withContext(dispatcher) {
        habitsRepository.updateHabitsFromRemote()
    }

}