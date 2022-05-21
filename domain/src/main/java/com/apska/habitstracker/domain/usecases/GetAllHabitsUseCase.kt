package com.apska.habitstracker.domain.usecases

import com.apska.habitstracker.domain.HabitsRepository

class GetAllHabitsUseCase(private val habitsRepository: HabitsRepository) {

    fun getAllHabits() = habitsRepository.getAllHabits()

}