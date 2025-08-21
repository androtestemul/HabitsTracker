package com.apska.habitstracker.presentation.di

import com.apska.habitstracker.presentation.ui.screens.addedithabit.AddEditHabitFragment
import com.apska.habitstracker.presentation.ui.screens.bottomsheet.BottomSheetFragment
import com.apska.habitstracker.presentation.ui.screens.habitpager.HabitPagerFragment
import com.apska.habitstracker.presentation.ui.screens.habitslist.HabitsListFragment
import dagger.Module
import dagger.Subcomponent

@FragmentScope
@Subcomponent
interface FragmentComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(): FragmentComponent
    }

    fun getViewModelFactory(): ViewModelFactory

    fun inject(fragment: HabitPagerFragment)
    fun inject(fragment: HabitsListFragment)
    fun inject(fragment: AddEditHabitFragment)
    fun inject(fragment: BottomSheetFragment)

}

@Module(subcomponents = [FragmentComponent::class])
interface MainModule

