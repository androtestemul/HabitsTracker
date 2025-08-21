package com.apska.habitstracker.presentation.ui.screens.addedithabit

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.apska.habitstracker.domain.usecases.GetHabitByIdUseCase
import com.apska.habitstracker.domain.usecases.InsertHabitUseCase
import com.apska.habitstracker.domain.usecases.PutHabitToRemoteUseCase
import com.apska.habitstracker.domain.usecases.UpdateHabitUseCase

class AddEditHabitViewModelFactory(
    private val context: Context,
    private val insertHabitUseCase : InsertHabitUseCase,
    private val updateHabitUseCase : UpdateHabitUseCase,
    private val getHabitByIdUseCase : GetHabitByIdUseCase,
    private val putHabitToRemoteUseCase : PutHabitToRemoteUseCase
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return AddEditHabitViewModel(
            context,
            insertHabitUseCase,
            updateHabitUseCase,
            getHabitByIdUseCase,
            putHabitToRemoteUseCase
        ) as T
    }
}