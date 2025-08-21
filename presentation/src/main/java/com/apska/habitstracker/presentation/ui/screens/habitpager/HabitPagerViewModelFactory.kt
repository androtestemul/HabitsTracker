package com.apska.habitstracker.presentation.ui.screens.habitpager

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.apska.habitstracker.domain.usecases.*
import javax.inject.Inject

class HabitPagerViewModelFactory @Inject constructor(
    private val context: Context,
    private val getAllHabitsUseCase: GetAllHabitsUseCase,
    private val getFilteredSortedHabitsUseCase: GetFilteredSortedHabitsUseCase,
    private val updateHabitsFromRemoteUseCase: UpdateHabitsFromRemoteUseCase,
    private val getHabitByIdUseCase: GetHabitByIdUseCase,
    private val doneHabitUseCase: DoneHabitUseCase
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return HabitPagerViewModel(
            context,
            getAllHabitsUseCase,
            getFilteredSortedHabitsUseCase,
            updateHabitsFromRemoteUseCase,
            getHabitByIdUseCase,
            doneHabitUseCase
        ) as T
    }

}