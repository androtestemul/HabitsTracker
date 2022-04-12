package com.apska.habitstracker.ui.screens.addedithabit

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.apska.habitstracker.model.Habit
import com.apska.habitstracker.model.HabitPriority
import com.apska.habitstracker.model.HabitType
import com.apska.habitstracker.repository.Repository
import com.apska.habitstracker.ui.view.colorview.ColorView

class AddEditHabitViewModel(application: Application) : AndroidViewModel(application) {

    val repository by lazy { Repository(application) }

    fun getHabit(id: Long) = repository.getHabit(id)

    fun addHabit(habit: Habit) {
        repository.insertHabit(habit)
    }

    fun updateHabit(habit: Habit) {
        repository.updateHabit(habit)
    }

    var selectedPriority: HabitPriority? = null
    var selectedType: HabitType? = null
    var selectedColorFromPicker = ColorView.DEFAULT_COLOR
}