package com.apska.habitstracker.ui.screens.addedithabit

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.apska.habitstracker.model.Habit
import com.apska.habitstracker.model.HabitPriority
import com.apska.habitstracker.model.HabitType
import com.apska.habitstracker.repository.Repository
import com.apska.habitstracker.ui.view.colorview.ColorView
import kotlinx.coroutines.*

class AddEditHabitViewModel(application: Application) : AndroidViewModel(application) {

    val repository by lazy { Repository(application) }

    var selectedPriority: HabitPriority? = null
    var selectedType: HabitType? = null
    var selectedColorFromPicker = ColorView.DEFAULT_COLOR

    private val _habit = MutableLiveData<Habit>()
    val habit: LiveData<Habit>
        get() = _habit

    private val _validateResult = MutableLiveData<ValidateResult>()
    val validateResult: LiveData<ValidateResult>
        get() = _validateResult

    private val _processResult = MutableLiveData<ProcessResult>()
    val processResult: LiveData<ProcessResult>
        get() = _processResult

    private val viewModelJob = Job()
    private val uiScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    fun getHabit(id: Long) {
        uiScope.launch {
            try {
                var habit: Habit?

                _processResult.value = ProcessResult.PROCESSING

                delay(2000L)

                withContext(Dispatchers.IO) {
                    habit = repository.getHabit(id)
                }

                habit?.let { it ->
                    selectedPriority = it.priority
                    selectedColorFromPicker = it.color

                    _habit.value = it
                }

                _processResult.value = ProcessResult.LOADED
            } catch (e: Exception) {
                _processResult.value = ProcessResult.ERROR(e.message, FormError.LOAD)
            }
        }
    }

    fun saveHabit(id: Long, header: String, description: String, executeCount: String, period: String) {
        if (id == -1L) {
            addHabit(
                header = header,
                description = description,
                executeCount = executeCount,
                period = period
            )
        } else {
            updateHabit(
                id = id,
                header = header,
                description = description,
                executeCount = executeCount,
                period = period
            )
        }
    }

    private fun addHabit(id: Long? = null, header: String, description: String, executeCount: String, period: String) {
        getValidatedHabit(id, header, description, executeCount, period)?.let { habit ->
            uiScope.launch {
                try {
                    _processResult.value = ProcessResult.PROCESSING

                    delay(2000L)

                    withContext(Dispatchers.IO) {
                        repository.insertHabit(habit)
                    }

                    _processResult.value = ProcessResult.SAVED
                } catch (e: Exception) {
                    _processResult.value = ProcessResult.ERROR(e.message, FormError.SAVE)
                }
            }
        }
    }

    private fun updateHabit(id: Long, header: String, description: String, executeCount: String, period: String) {
        getValidatedHabit(id, header, description, executeCount, period)?.let { habit ->
            uiScope.launch {
                try {
                    _processResult.value = ProcessResult.PROCESSING

                    delay(2000L)

                    withContext(Dispatchers.IO) {
                        repository.updateHabit(habit)
                    }

                    _processResult.value = ProcessResult.SAVED
                } catch (e: Exception) {
                    _processResult.value = ProcessResult.ERROR(e.message, FormError.SAVE)
                }
            }
        }
    }

    private fun validateHabit(
        header: String,
        description: String,
        executeCount: String,
        period: String,
    ): Boolean {
        var isValid = true

        val validatedFields = HashMap<ValidatedFields, ValidationErrorTypes>()

        if (header.isBlank()) {
            validatedFields[ValidatedFields.HEADER] = ValidationErrorTypes.EMPTY
            isValid = false
        }

        if (description.isBlank()) {
            validatedFields[ValidatedFields.DESCRIPTION] = ValidationErrorTypes.EMPTY
            isValid = false
        }

        if (executeCount.isBlank()) {
            validatedFields[ValidatedFields.EXECUTE_COUNT] = ValidationErrorTypes.EMPTY
            isValid = false
        }

        if (period.isBlank()) {
            validatedFields[ValidatedFields.PERIOD] = ValidationErrorTypes.EMPTY
            isValid = false
        }

        if (selectedPriority == null) {
            validatedFields[ValidatedFields.PRIORITY] = ValidationErrorTypes.EMPTY
            isValid = false
        }

        if (selectedType == null) {
            validatedFields[ValidatedFields.TYPE] = ValidationErrorTypes.EMPTY
            isValid = false
        }

        _validateResult.value = ValidateResult(isValid, validatedFields)

        return isValid
    }

    private fun getValidatedHabit(
        id: Long?,
        header: String,
        description: String,
        executeCount: String,
        period: String,
    ) = if (!validateHabit(header, description, executeCount, period)) {
        null
    } else {
        Habit(
            id = id,
            header = header,
            description = description,
            priority = selectedPriority!!,
            type = selectedType!!,
            executeCount = executeCount.toInt(),
            period = period,
            color = selectedColorFromPicker
        )
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}