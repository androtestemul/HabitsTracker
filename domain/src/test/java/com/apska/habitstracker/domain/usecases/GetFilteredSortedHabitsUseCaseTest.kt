package com.apska.habitstracker.domain.usecases

import com.apska.habitstracker.domain.HabitsRepository
import com.apska.habitstracker.domain.model.Habit
import com.apska.habitstracker.domain.model.HabitPriority
import com.apska.habitstracker.domain.model.HabitSort
import com.apska.habitstracker.domain.model.HabitType
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.mockito.kotlin.mock

class GetFilteredSortedHabitsUseCaseTest {

    private val habitsRepository = mock<HabitsRepository>()

    @Test
    fun `test Get Filtered Sorted Habits Use Case by Header`() = runBlocking {

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
            )/*,
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
            ),
            Habit(
                3,
                "876fdgfd43",
                "header 3",
                "description 3",
                HabitPriority.HIGH,
                HabitType.NEUTRAL,
                9,
                7,
                3,
                3,
                "14.08.2022 07:04:03",
                false
            )*/
        ))

        Mockito.`when`(habitsRepository.getFilteredSortedHabit("er 1", null, HabitSort.NONE)).thenReturn(testHabitsFlow)

        val getFilteredSortedHabitsUseCase = GetFilteredSortedHabitsUseCase(habitsRepository)
        val actualHabits = getFilteredSortedHabitsUseCase.getFilteredSortedHabit("er 1", null, HabitSort.NONE).toList()

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
            )
        )).toList()

        Assertions.assertEquals(expectedFlow, actualHabits)

    }

    @Test
    fun `test Get Filtered Sorted Habits Use Case by Priority`() = runBlocking {

        val testHabitsFlow = flowOf(listOf(
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

        Mockito.`when`(habitsRepository.getFilteredSortedHabit("", HabitPriority.MIDDLE, HabitSort.NONE)).thenReturn(testHabitsFlow)

        val getFilteredSortedHabitsUseCase = GetFilteredSortedHabitsUseCase(habitsRepository)
        val actualHabits = getFilteredSortedHabitsUseCase.getFilteredSortedHabit("", HabitPriority.MIDDLE, HabitSort.NONE).toList()

        val expectedFlow = flowOf(listOf(
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

        Assertions.assertEquals(expectedFlow, actualHabits)

    }

}