package com.apska.habitstracker.presentation.di

import com.apska.habitstracker.presentation.ui.screens.habitslist.HabitsListFragment
import dagger.Subcomponent

@FragmentScope
@Subcomponent
interface HabitsListComponent {
    @Subcomponent.Factory
    interface Factory {
        fun create() : HabitsListComponent
    }

    fun inject(fragment: HabitsListFragment)
}