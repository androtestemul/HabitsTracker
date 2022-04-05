package com.apska.habitstracker.ui.screens.habitpager

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.apska.habitstracker.model.Habit
import com.apska.habitstracker.model.HabitPriority
import com.apska.habitstracker.repository.HabitStorage
import com.apska.habitstracker.ui.screens.ViewModelEvent

class HabitPagerViewModel: ViewModel() {

    var searchHeader : String = ""
        set(value) {
            field = value
            applyFilter(value, selectedPriority)
        }

    var selectedPriority : HabitPriority? = null
        set(value) {
            field = value
            applyFilter(searchHeader, value)
        }

    private val _habits = MutableLiveData<ArrayList<Habit>>()
    val habits: LiveData<ArrayList<Habit>>
        get() = _habits

    init {
        _habits.value = HabitStorage.getAllHabits()
    }

    private val _navigateToHabit = MutableLiveData<ViewModelEvent<Int>>()
    val navigateToHabit
        get() = _navigateToHabit

    fun onHabitClicked(habitId: Int) {
        _navigateToHabit.value = ViewModelEvent(habitId)
    }


    private fun applyFilter(habitHeader: String? = null, habitPriority: HabitPriority? = null) {
        _habits.value = HabitStorage.getFilteredHabits(
            habitHeader = habitHeader,
            habitPriority = habitPriority
        )
    }

}