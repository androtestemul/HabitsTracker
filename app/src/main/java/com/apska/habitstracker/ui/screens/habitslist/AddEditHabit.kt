package com.apska.habitstracker.ui.screens.habitslist

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.apska.habitstracker.R

class AddEditHabit : AppCompatActivity() {

    companion object {
        val EXTRA
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_edit_habit)
    }
}