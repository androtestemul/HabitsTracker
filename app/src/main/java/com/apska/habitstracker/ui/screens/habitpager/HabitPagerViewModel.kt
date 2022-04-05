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
import java.util.*
import kotlin.collections.ArrayList

class HabitPagerViewModel: ViewModel() {

    companion object {
        const val SORT_ASC = "ASC"
        const val SORT_DESC = "DESC"
    }

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

    private val _currentSortDirection = MutableLiveData<String>()
    val currentSortDirection: LiveData<String>
        get() = _currentSortDirection

    fun sortHabitByPeriod() {
        val list = _habits.value ?: arrayListOf()

        if (_currentSortDirection.value == SORT_DESC || _currentSortDirection.value == null) {
            list.sortBy { it.period }
            _currentSortDirection.value = SORT_ASC
        } else if (_currentSortDirection.value == SORT_ASC) {
            list.sortByDescending { it.period }
            _currentSortDirection.value = SORT_DESC
        }

        _habits.value = list
    }

}