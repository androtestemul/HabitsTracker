package com.apska.habitstracker.ui.screens.addedithabit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.apska.habitstracker.domain.usecases.GetHabitByIdUseCase
import com.apska.habitstracker.domain.usecases.InsertHabitUseCase
import com.apska.habitstracker.domain.usecases.PutHabitToRemoteUseCase
import com.apska.habitstracker.domain.usecases.UpdateHabitUseCase

class AddEditHabitViewModelFactory(
    private val insertHabitUseCase : InsertHabitUseCase,
    private val updateHabitUseCase : UpdateHabitUseCase,
    private val getHabitByIdUseCase : GetHabitByIdUseCase,
    private val putHabitToRemoteUseCase : PutHabitToRemoteUseCase
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return AddEditHabitViewModel(
            insertHabitUseCase,
            updateHabitUseCase,
            getHabitByIdUseCase,
            putHabitToRemoteUseCase
        ) as T
    }
}