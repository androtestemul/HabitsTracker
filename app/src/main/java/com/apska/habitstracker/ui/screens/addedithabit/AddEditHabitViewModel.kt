package com.apska.habitstracker.ui.screens.addedithabit

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.apska.habitstracker.App
import com.apska.habitstracker.model.Habit
import com.apska.habitstracker.model.HabitPriority
import com.apska.habitstracker.model.HabitType
import com.apska.habitstracker.network.HabitApi
import com.apska.habitstracker.repository.Repository
import com.apska.habitstracker.ui.view.colorview.ColorView
import kotlinx.coroutines.*

class AddEditHabitViewModel(application: Application) : AndroidViewModel(application) {

    val repository by lazy { Repository(application) }

    var uid: String = ""
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

    fun getHabit(id: Long) {
        viewModelScope.launch {
            try {
                _processResult.value = ProcessResult.PROCESSING

                delay(2000L)

                val habit = repository.getHabitById(id)

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

                    withContext(Dispatchers.IO) {
                        var isInserted = false

                        try {
                            val response = HabitApi.habitApiService.putHabit(habit)

                            if (response.isSuccessful) {
                                val putHabitResponse = response.body()

                                putHabitResponse?.let {
                                    repository.insertHabit(habit.copy(uid = putHabitResponse.uid, isActual = true))
                                    isInserted = true
                                }
                            } else {
                                response.errorBody()?.let {
                                    throw Exception("Ошибка! Код: ${response.code()}, Текст: $it")
                                } ?: kotlin.run {
                                    throw Exception("Ошибка! Код: ${response.code()}")
                                }
                            }
                        } catch (e: Exception) {
                            Log.e(TAG, "INSERT ERROR!", e)
                        }

                        if (!isInserted) {
                            repository.insertHabit(habit.copy(isActual = false))
                            App().actualizeRemote()
                        }
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

                    withContext(Dispatchers.IO) {
                        var isUpdated = false

                        try {
                            val response = HabitApi.habitApiService.putHabit(habit)

                            if (response.isSuccessful) {
                                val putHabitResponse = response.body()

                                putHabitResponse?.let {
                                    repository.updateHabit(habit.copy(uid = putHabitResponse.uid, isActual = true))
                                    isUpdated = true
                                }
                            } else {
                                response.errorBody()?.let {
                                    throw Exception("Ошибка! Код: ${response.code()}, Текст: $it")
                                } ?: kotlin.run {
                                    throw Exception("Ошибка! Код: ${response.code()}")
                                }
                            }
                        } catch (e: Exception) {
                            Log.e(TAG, "UPDATE ERROR!", e)
                        }

                        if (!isUpdated) {
                            repository.updateHabit(habit.copy(isActual = false))
                            App().actualizeRemote()
                        }
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
            period = period,
            color = selectedColorFromPicker
        )
    }

    companion object {
        private val TAG = this::class.java.simpleName
    }
}