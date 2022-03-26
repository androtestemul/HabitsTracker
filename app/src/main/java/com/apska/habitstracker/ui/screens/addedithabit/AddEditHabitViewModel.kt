package com.apska.habitstracker.ui.screens.addedithabit

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.apska.habitstracker.model.Habit
import com.apska.habitstracker.model.HabitPriority
import com.apska.habitstracker.repository.HabitStorage
import com.apska.habitstracker.model.HabitType
import com.apska.habitstracker.ui.view.colorview.ColorView

class AddEditHabitViewModel : ViewModel() {
    private val _habits = MutableLiveData<ArrayList<Habit>>()
    val habits: LiveData<ArrayList<Habit>>
        get() = _habits

    init {
        _habits.value = HabitStorage.getAllHabits()
    }

    fun getHabit(id: Int) = HabitStorage.getHabit(id)

    fun addHabit(habit: Habit) {
        HabitStorage.addHabit(habit)
    }

    fun updateHabit(habit: Habit) {
        HabitStorage.updateHabit(habit)
    }

    var selectedPriority: HabitPriority? = null
    var selectedType: HabitType? = null
    var selectedColorFromPicker = ColorView.DEFAULT_COLOR
}