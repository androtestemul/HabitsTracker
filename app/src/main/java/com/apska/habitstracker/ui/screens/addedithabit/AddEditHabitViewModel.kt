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

class AddEditHabitViewModel(application: Application) : AndroidViewModel(application) {

    val repository by lazy { Repository(application) }

    fun getHabit(id: Long) = repository.getHabit(id)

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
        getValidatedHabit(id, header, description, executeCount, period)?.let {
            repository.insertHabit(it)
        }
    }

    private fun updateHabit(id: Long, header: String, description: String, executeCount: String, period: String) {
        getValidatedHabit(id, header, description, executeCount, period)?.let {
            repository.updateHabit(it)
        }
    }

    var selectedPriority: HabitPriority? = null
    var selectedType: HabitType? = null
    var selectedColorFromPicker = ColorView.DEFAULT_COLOR

    private val _validateResult = MutableLiveData<ValidateResult>()
    val validateResult: LiveData<ValidateResult>
        get() = _validateResult

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
}