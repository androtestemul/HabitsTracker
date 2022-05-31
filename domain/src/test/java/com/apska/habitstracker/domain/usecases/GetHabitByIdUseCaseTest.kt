package com.apska.habitstracker.domain.usecases

import com.apska.habitstracker.domain.HabitsRepository
import com.apska.habitstracker.domain.model.Habit
import com.apska.habitstracker.domain.model.HabitPriority
import com.apska.habitstracker.domain.model.HabitType
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestCoroutineDispatcher
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.mockito.kotlin.mock


class GetHabitByIdUseCaseTest {

    private val habitsRepository = mock<HabitsRepository>()

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `test Get Habit By Id Use Case`() = runBlocking {

        val testHabit = Habit(
            HABIT_ID,
            HABIT_UID,
            HABIT_HEADER,
            HABIT_DESCRIPTION,
            HABIT_PRIORITY,
            HABIT_TYPE,
            HABIT_EXECUTE_COUNT,
            HABIT_PERIOD,
            HABIT_COLOR,
            HABIT_LAST_MODIFIED,
            HABIT_LAST_MODIFIED_DATE_TIME,
            HABIT_IS_ACTUAL
        )

        Mockito.`when`(habitsRepository.getHabitById(1)).thenReturn(testHabit)


        val getHabitByIdUseCase = GetHabitByIdUseCase(habitsRepository, TestCoroutineDispatcher())
        val actualHabit = getHabitByIdUseCase.getHabitById(1)

        val expectedHabit = testHabit.copy()

        assertEquals(expectedHabit, actualHabit)

    }

    companion object {
        private const val HABIT_ID = 1L
        private const val HABIT_UID = "uid"
        private const val HABIT_HEADER = "header"
        private const val HABIT_DESCRIPTION = "description"
        private val HABIT_PRIORITY = HabitPriority.LOW
        private val HABIT_TYPE = HabitType.BAD
        private const val HABIT_EXECUTE_COUNT = 5
        private const val HABIT_PERIOD = 7
        private const val HABIT_COLOR = 1
        private const val HABIT_LAST_MODIFIED = 2L
        private const val HABIT_LAST_MODIFIED_DATE_TIME = "10.05.2022 12:35:40"
        private const val HABIT_IS_ACTUAL = true
    }
}