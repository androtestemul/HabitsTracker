package com.apska.habitstracker.ui.screens.habitslist

import android.text.Editable
import android.text.TextWatcher
import com.apska.habitstracker.ui.screens.FieldValidator


class AddEditFieldValidator(private val validator: FieldValidator) : TextWatcher {

        override fun afterTextChanged(s: Editable?) {}
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            validator.validate()
        }

}