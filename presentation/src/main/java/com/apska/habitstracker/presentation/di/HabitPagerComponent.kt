package com.apska.habitstracker.presentation.di

import com.apska.habitstracker.presentation.ui.screens.habitpager.HabitPagerFragment
import dagger.Subcomponent

@FragmentScope
@Subcomponent
interface HabitPagerComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create() : HabitPagerComponent
    }

    fun inject(fragment: HabitPagerFragment)
}