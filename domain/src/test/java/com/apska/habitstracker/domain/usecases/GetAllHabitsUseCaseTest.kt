package com.apska.habitstracker.domain.usecases

import com.apska.habitstracker.domain.HabitsRepository
import com.apska.habitstracker.domain.model.Habit
import com.apska.habitstracker.domain.model.HabitPriority
import com.apska.habitstracker.domain.model.HabitType
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.mockito.kotlin.mock

class GetAllHabitsUseCaseTest {

    private val habitsRepository = mock<HabitsRepository>()

    @Test
    fun `test Get All Habits Use Case`() = runBlocking {

        val testHabitsFlow = flowOf(listOf(
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
                true
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
        ))

        Mockito.`when`(habitsRepository.getAllHabits()).thenReturn(testHabitsFlow)

        val getAllHabitsUseCase = GetAllHabitsUseCase(habitsRepository)
        val actualHabits = getAllHabitsUseCase.getAllHabits().toList()

        val expectedFlow = flowOf(listOf(
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
                true
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
        )).toList()

        assertEquals(expectedFlow, actualHabits)

    }
}