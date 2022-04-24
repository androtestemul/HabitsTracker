package com.apska.habitstracker.ui.screens.addedithabit

sealed class ProcessResult {
    object SAVED : ProcessResult()
    object LOADED : ProcessResult()
    class ERROR(val message: String?, val formErrorType: FormError): ProcessResult()
    object PROCESSING : ProcessResult()
}