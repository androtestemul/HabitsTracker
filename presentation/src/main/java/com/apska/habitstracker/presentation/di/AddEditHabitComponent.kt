package com.apska.habitstracker.presentation.di

import com.apska.habitstracker.presentation.ui.screens.addedithabit.AddEditHabitFragment
import dagger.Subcomponent

@FragmentScope
@Subcomponent
interface AddEditHabitComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create() : AddEditHabitComponent
    }

    fun inject(fragment: AddEditHabitFragment)
}