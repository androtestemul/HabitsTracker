package com.apska.habitstracker.ui.screens.habitpager

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.apska.habitstracker.model.Habit
import com.apska.habitstracker.repository.HabitStorage

class HabitPagerViewModel: ViewModel() {

    private val _habits = MutableLiveData<ArrayList<Habit>>()
    val habits: LiveData<ArrayList<Habit>>
        get() = _habits

    init {
        _habits.value = HabitStorage.getAllHabits()
    }

    private val _navigateToHabit = MutableLiveData<Int?>()
    val navigateToHabit
        get() = _navigateToHabit

    fun onHabitClicked(habitId: Int) {
        _navigateToHabit.value = habitId
    }

    fun onHabitNavigated() {
        _navigateToHabit.value = null
    }
}