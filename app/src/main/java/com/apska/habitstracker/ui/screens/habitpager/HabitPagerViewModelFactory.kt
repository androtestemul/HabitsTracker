package com.apska.habitstracker.ui.screens.habitpager

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.apska.habitstracker.domain.usecases.GetAllHabitsUseCase
import com.apska.habitstracker.domain.usecases.GetFilteredSortedHabitsUseCase
import com.apska.habitstracker.domain.usecases.UpdateHabitsFromRemoteUseCase

class HabitPagerViewModelFactory(
    private val getAllHabitsUseCase: GetAllHabitsUseCase,
    private val getFilteredSortedHabitsUseCase: GetFilteredSortedHabitsUseCase,
    private val updateHabitsFromRemoteUseCase: UpdateHabitsFromRemoteUseCase
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return HabitPagerViewModel(
            getAllHabitsUseCase,
            getFilteredSortedHabitsUseCase,
            updateHabitsFromRemoteUseCase
        ) as T
    }

}