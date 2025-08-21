package com.apska.habitstracker.presentation.ui.screens.addedithabit

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apska.extentions.getCurrentDate
import com.apska.habitstracker.domain.model.Habit
import com.apska.habitstracker.domain.model.HabitPriority
import com.apska.habitstracker.domain.model.HabitType
import com.apska.habitstracker.domain.usecases.GetHabitByIdUseCase
import com.apska.habitstracker.domain.usecases.InsertHabitUseCase
import com.apska.habitstracker.domain.usecases.PutHabitToRemoteUseCase
import com.apska.habitstracker.domain.usecases.UpdateHabitUseCase
import com.apska.habitstracker.presentation.ui.view.colorview.ColorView
import com.apska.habitstracker.presentation.workers.ActualizeRemoteWorker
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

class AddEditHabitViewModel @Inject constructor(
    private val context : Context,
    private val insertHabitUseCase : InsertHabitUseCase,
    private val updateHabitUseCase : UpdateHabitUseCase,
    private val getHabitByIdUseCase : GetHabitByIdUseCase,
    private val putHabitToRemoteUseCase : PutHabitToRemoteUseCase
) : ViewModel() {

    private var uid: String = ""
    var selectedPriority: HabitPriority? = null
    var selectedType: HabitType? = null
    var selectedColorFromPicker = ColorView.Companion.DEFAULT_COLOR

    private val _habit = MutableLiveData<Habit>()
    val habit: LiveData<Habit>
        get() = _habit

    private val _validateResult = MutableLiveData<ValidateResult>()
    val validateResult: LiveData<ValidateResult>
        get() = _validateResult

    private val _processResult = MutableLiveData<ProcessResult>()
    val processResult: LiveData<ProcessResult>
        get() = _processResult

    fun getHabit(id: Long) {
        viewModelScope.launch {
            try {
                _processResult.value = ProcessResult.PROCESSING

                delay(2000L)

                val habit = getHabitByIdUseCase.getHabitById(id)

                habit?.let { it ->
                    uid = it.uid
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
            viewModelScope.launch {
                try {
                    _processResult.value = ProcessResult.PROCESSING

                    delay(2000L)

                    var isInserted = false

                    try {
                        putHabitToRemoteUseCase
                            .putHabitToRemote(habit)?.let { uid ->
                                insertHabitUseCase.insertHabit(habit.copy(uid = uid, isActual = true))
                                isInserted = true
                            }
                    } catch (e: Exception) {
                        Log.e(TAG, "INSERT ERROR!", e)
                    }

                    if (!isInserted) {
                        insertHabitUseCase.insertHabit(habit.copy(isActual = false))
                        ActualizeRemoteWorker.Companion.actualizeRemote(context)
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
            viewModelScope.launch {
                try {
                    _processResult.value = ProcessResult.PROCESSING

                    delay(2000L)

                    var isUpdated = false

                    try {
                        putHabitToRemoteUseCase
                            .putHabitToRemote(habit)?.let { uid ->
                                updateHabitUseCase.updateHabit(habit.copy(uid = uid, isActual = true))
                                isUpdated = true
                            }
                    } catch (e: Exception) {
                        Log.e(TAG, "UPDATE ERROR!", e)
                    }

                    if (!isUpdated) {
                        updateHabitUseCase.updateHabit(habit.copy(isActual = false))
                        ActualizeRemoteWorker.Companion.actualizeRemote(context)
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
            uid = uid,
            header = header,
            description = description,
            priority = selectedPriority!!,
            type = selectedType!!,
            executeCount = executeCount.toInt(),
            period = period.toInt(),
            color = selectedColorFromPicker,
            lastModified = getCurrentDate()
        )
    }

    companion object {
        private val TAG = this::class.java.simpleName
    }
}