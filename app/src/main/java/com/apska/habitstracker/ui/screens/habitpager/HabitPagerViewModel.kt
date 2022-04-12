package com.apska.habitstracker.ui.screens.habitpager

import android.app.Application
import androidx.lifecycle.*
import com.apska.habitstracker.model.Habit
import com.apska.habitstracker.model.HabitPriority
import com.apska.habitstracker.repository.HabitFilterFields
import com.apska.habitstracker.repository.HabitSort
import com.apska.habitstracker.repository.Repository
import com.apska.habitstracker.ui.screens.ViewModelEvent
import kotlin.collections.HashMap

class HabitPagerViewModel(application: Application): AndroidViewModel(application) {

    val repository by lazy { Repository(application) }

    var searchHeader : String = ""
        set(value) {
            field = value
            if (value.isNotBlank()) {
                addFieldToFilter(HabitFilterFields.HEADER, value)
            } else {
                removeFieldFromFilter(HabitFilterFields.HEADER)
            }
        }

    var selectedPriority : HabitPriority? = null
        set(value) {
            field = value

            if (value != null) {
                addFieldToFilter(HabitFilterFields.PRIORITY, value)
            } else {
                removeFieldFromFilter(HabitFilterFields.PRIORITY)
            }
        }

    private val _habits = repository.getAllHabits()
    private var _searchHabits: LiveData<List<Habit>>

    val habits = MediatorLiveData<List<Habit>>()

    private val habitFilterFields = MutableLiveData<HashMap<HabitFilterFields, Any>>()

    init {
        _searchHabits = Transformations.switchMap(habitFilterFields) { fieldsMap ->
            var header: String? = null
            var priority: HabitPriority? = null
            var periodSortOrder = HabitSort.NONE

            fieldsMap.forEach { (habitFilterField, fieldValue) ->
                when (habitFilterField) {
                    HabitFilterFields.HEADER -> header = "%$fieldValue%"
                    HabitFilterFields.PRIORITY -> priority = fieldValue as HabitPriority
                    HabitFilterFields.PERIOD -> periodSortOrder = fieldValue as Int
                }
            }

            if (header != null && priority == null) {
                repository.getHeaderFilteredHabits(header!!, periodSortOrder)
            } else if (header == null && priority != null) {
                repository.getPriorityFilteredHabits(priority!!, periodSortOrder)
            } else if (header != null && priority != null) {
                repository.getFilteredSortedHabit(header!!, priority!!, periodSortOrder)
            } else {
                repository.getAllHabitsSorted(periodSortOrder)
            }

        }

        habits.addSource(_habits) {
            habits.value = it
        }

        habits.addSource(_searchHabits) {
            habits.value = it
        }
    }

    private fun addFieldToFilter(habitField: HabitFilterFields, value: Any) {
        val fieldsMap = habitFilterFields.value ?: HashMap()

        if (fieldsMap.contains(habitField)) {
            fieldsMap[habitField] = value
        } else {
            fieldsMap[habitField] = value
        }

        habitFilterFields.value = fieldsMap
    }

    private fun removeFieldFromFilter(habitField: HabitFilterFields) {
        val fieldsMap = habitFilterFields.value

        fieldsMap?.let {
            it.remove(habitField)

            habitFilterFields.value = it
        }
    }

    private var _currentSortDirection = MutableLiveData(HabitSort.NONE)
    val currentSortDirection: LiveData<Int>
        get() = _currentSortDirection

    fun sortHabitByPeriod() {
        if (_currentSortDirection.value == HabitSort.NONE || _currentSortDirection.value == HabitSort.SORT_DESC) {
            _currentSortDirection.value = HabitSort.SORT_ASC
        } else if (_currentSortDirection.value == HabitSort.SORT_ASC) {
            _currentSortDirection.value = HabitSort.SORT_DESC
        }

        _currentSortDirection.value?.let {
            addFieldToFilter(HabitFilterFields.PERIOD, it)
        }
    }

    private val _navigateToHabit = MutableLiveData<ViewModelEvent<Long>>()
    val navigateToHabit
        get() = _navigateToHabit

    fun onHabitClicked(habitId: Long) {
        _navigateToHabit.value = ViewModelEvent(habitId)
    }

}