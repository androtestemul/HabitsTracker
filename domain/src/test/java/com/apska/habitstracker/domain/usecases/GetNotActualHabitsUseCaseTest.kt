package com.apska.habitstracker.domain.usecases

import com.apska.habitstracker.domain.HabitsRepository
import com.apska.habitstracker.domain.model.Habit
import com.apska.habitstracker.domain.model.HabitPriority
import com.apska.habitstracker.domain.model.HabitType
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestCoroutineDispatcher
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.mockito.kotlin.mock

class GetNotActualHabitsUseCaseTest {
    private val habitsRepository = mock<HabitsRepository>()

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `test Get Not Actual Habits Use Case`() = runBlocking {

        val testHabitsFlow = listOf(
            Habit(
                1,
                "12awedasda1231s",
                "header 1",
                "description 1",
                HabitPriority.LOW,
                HabitType.BAD,
                5,
                7,
                1,
                1,
                "10.05.2022 12:35:40",
                false
            ),
            Habit(
                2,
                "543543433",
                "header 2",
                "description 2",
                HabitPriority.MIDDLE,
                HabitType.GOOD,
                3,
                6,
                2,
                2,
                "12.04.2022 17:50:03",
                false
            )
        )

        Mockito.`when`(habitsRepository.getNotActualHabits()).thenReturn(testHabitsFlow)

        val getNotActualHabitsUseCase = GetNotActualHabitsUseCase(habitsRepository, TestCoroutineDispatcher())
        val actualHabits = getNotActualHabitsUseCase.getNotActualHabits()

        val expectedFlow = listOf(
            Habit(
                1,
                "12awedasda1231s",
                "header 1",
                "description 1",
                HabitPriority.LOW,
                HabitType.BAD,
                5,
                7,
                1,
                1,
                "10.05.2022 12:35:40",
                false
            ),
            Habit(
                2,
                "543543433",
                "header 2",
                "description 2",
                HabitPriority.MIDDLE,
                HabitType.GOOD,
                3,
                6,
                2,
                2,
                "12.04.2022 17:50:03",
                false
            )
        )

        Assertions.assertEquals(expectedFlow, actualHabits)

    }
}