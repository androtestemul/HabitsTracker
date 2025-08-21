package com.apska.habitstracker.presentation.di

import dagger.Module

@Module(
    subcomponents = [
        AddEditHabitComponent::class,
        BottomSheetComponent::class,
        HabitPagerComponent::class,
        HabitsListComponent::class
    ]
)
object SubcomponentsModule

