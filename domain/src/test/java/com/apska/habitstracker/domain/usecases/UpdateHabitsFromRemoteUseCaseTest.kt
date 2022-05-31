package com.apska.habitstracker.domain.usecases

import com.apska.habitstracker.domain.HabitsRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestCoroutineDispatcher
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.mockito.kotlin.mock

class UpdateHabitsFromRemoteUseCaseTest {

    private val habitsRepository = mock<HabitsRepository>()

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `test Done Habit Use Case`() = runBlocking {

        Mockito.`when`(habitsRepository.updateHabitsFromRemote()).thenReturn(true)

        val updateHabitsFromRemoteUseCase = UpdateHabitsFromRemoteUseCase(habitsRepository, TestCoroutineDispatcher())
        val actualResult = updateHabitsFromRemoteUseCase.updateHabitsFromRemote()

        val expectedResult = true

        assertEquals(expectedResult, actualResult)

    }

}