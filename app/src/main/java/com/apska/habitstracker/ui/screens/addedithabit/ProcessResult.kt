package com.apska.habitstracker.ui.screens.addedithabit

import com.apska.habitstracker.domain.model.HabitType

sealed class ProcessResult {
    object SAVED : ProcessResult()
    object LOADED : ProcessResult()
    class DoneHabit(val habitType: HabitType, val canDoCount: Int) : ProcessResult()
    class ERROR(val message: String?, val formErrorType: FormError): ProcessResult()
    object PROCESSING : ProcessResult()
}