package com.apska.habitstracker.ui.screens.habitpager

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.apska.habitstracker.domain.usecases.*

/*getAllHabitsUseCase: GetAllHabitsUseCase,
private val getFilteredSortedHabitsUseCase: GetFilteredSortedHabitsUseCase,
private val updateHabitsFromRemoteUseCase: UpdateHabitsFromRemoteUseCase,
private val getHabitByIdUseCase: GetHabitByIdUseCase,
private val doneHabitUseCase: DoneHabitUseCase,*/

class HabitPagerViewModelFactory(
    private val getAllHabitsUseCase: GetAllHabitsUseCase,
    private val getFilteredSortedHabitsUseCase: GetFilteredSortedHabitsUseCase,
    private val updateHabitsFromRemoteUseCase: UpdateHabitsFromRemoteUseCase,
    private val getHabitByIdUseCase: GetHabitByIdUseCase,
    private val doneHabitUseCase: DoneHabitUseCase
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return HabitPagerViewModel(
            getAllHabitsUseCase,
            getFilteredSortedHabitsUseCase,
            updateHabitsFromRemoteUseCase,
            getHabitByIdUseCase,
            doneHabitUseCase
        ) as T
    }

}