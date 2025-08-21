package com.apska.habitstracker.presentation.di

import com.apska.habitstracker.presentation.ui.screens.bottomsheet.BottomSheetFragment
import dagger.Subcomponent

@FragmentScope
@Subcomponent
interface BottomSheetComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create() : BottomSheetComponent
    }

    fun inject(fragment: BottomSheetFragment)
}