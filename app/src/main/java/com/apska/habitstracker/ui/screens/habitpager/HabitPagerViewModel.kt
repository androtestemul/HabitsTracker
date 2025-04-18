package com.apska.habitstracker.ui.screens.habitpager

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import com.apska.extentions.getCurrentDate
import com.apska.habitstracker.data.repository.HabitFilterFields
import com.apska.habitstracker.domain.model.HabitSort
import com.apska.habitstracker.domain.model.Habit
import com.apska.habitstracker.domain.model.HabitPriority
import com.apska.habitstracker.domain.usecases.*
import com.apska.habitstracker.ui.screens.ViewModelEvent
import com.apska.habitstracker.ui.screens.addedithabit.FormError
import com.apska.habitstracker.ui.screens.addedithabit.ProcessResult
import com.apska.habitstracker.workers.ActualizeDatabaseWorker
import kotlinx.coroutines.launch

class HabitPagerViewModel(
    getAllHabitsUseCase: GetAllHabitsUseCase,
    private val getFilteredSortedHabitsUseCase: GetFilteredSortedHabitsUseCase,
    private val updateHabitsFromRemoteUseCase: UpdateHabitsFromRemoteUseCase,
    private val getHabitByIdUseCase: GetHabitByIdUseCase,
    private val doneHabitUseCase: DoneHabitUseCase
): ViewModel() {

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

    private val _navigateToHabit = MutableLiveData<ViewModelEvent<Long>>()
    val navigateToHabit
        get() = _navigateToHabit

    private val _processResult = MutableLiveData<ProcessResult>()
    val processResult: LiveData<ProcessResult>
        get() = _processResult

    private val _habits = getAllHabitsUseCase.getAllHabits().asLiveData()

    private var _searchHabits: LiveData<List<Habit>>

    val habits = MediatorLiveData<List<Habit>>()

    private val habitFilterFields = MutableLiveData<HashMap<HabitFilterFields, Any>>()

    init {
        updateHabitsFromRemote()



        _searchHabits = habitFilterFields.switchMap { fieldsMap ->
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

            getFilteredSortedHabitsUseCase.getFilteredSortedHabit(
                header ?: "",
                priority,
                periodSortOrder
            ).asLiveData()
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
            if (it.containsKey(habitField)) {
                it.remove(habitField)

                habitFilterFields.value = it
            }
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

    fun resetSortAndFilter() {
        val fieldsMap = habitFilterFields.value

        fieldsMap?.let {
            it.clear()
            habitFilterFields.value = it
        }

        _currentSortDirection.value = HabitSort.NONE
        selectedPriority = null
    }

    fun onHabitClicked(habitId: Long) {
        _navigateToHabit.value = ViewModelEvent(habitId)
    }

    private fun updateHabitsFromRemote() {
        viewModelScope.launch {
            _processResult.value = ProcessResult.PROCESSING

            Log.d(TAG, "updateHabitsFromRemote: HabitApi.habitApiService.getHabits()")

            if (updateHabitsFromRemoteUseCase.updateHabitsFromRemote()) {
                _processResult.value = ProcessResult.LOADED
            } else {
                _processResult.value = ProcessResult.ERROR(null, FormError.LOAD)
                ActualizeDatabaseWorker.actualizeDatabase()
            }
        }
    }

    fun onDoneHabit(habitId: Long) {
        viewModelScope.launch {
            _processResult.value = ProcessResult.PROCESSING

            try {
                val habit = getHabitByIdUseCase.getHabitById(habitId)

                habit?.let {
                    (habit.done_dates as ArrayList).add(getCurrentDate())

                    if (doneHabitUseCase.doneHabit(habit)) {

                        _processResult.value = ProcessResult.DoneHabit(
                            habit.type,
                            habit.executeCount - habit.done_dates.size
                        )
                    } else {
                        _processResult.value = ProcessResult.ERROR(null, FormError.SAVE)
                    }
                } ?: run {
                    _processResult.value = ProcessResult.ERROR(null, FormError.SAVE)
                }


            } catch (e: Exception) {
                Log.e(TAG, "onDoneHabit: ", e)

                _processResult.value = ProcessResult.ERROR(null, FormError.SAVE)
            }
        }
    }

    companion object {
        private val TAG = this::class.java.simpleName
    }
}