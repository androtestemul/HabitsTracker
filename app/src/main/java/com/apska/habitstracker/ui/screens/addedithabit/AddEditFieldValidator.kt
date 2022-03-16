package com.apska.habitstracker.ui.screens.addedithabit

import android.text.Editable
import android.text.TextWatcher
import com.apska.habitstracker.ui.screens.FieldValidator


class AddEditFieldValidator(private val validator: FieldValidator) : TextWatcher {
    private var prevText : String = ""


    override fun afterTextChanged(s: Editable?) {
        if (prevText == s.toString()) {
            return
        }

        prevText = s.toString()

        validator.validate()
    }
    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

}