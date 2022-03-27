package com.apska.habitstracker.ui.screens

class ViewModelEvent<out T>(private val value: T) {

    private var isHandled = false

    fun getValue() : T? {
        return if (isHandled) {
             null
        } else {
            isHandled = true
            value
        }
    }
}