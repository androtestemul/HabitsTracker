package com.apska.habitstracker.presentation.di

interface AppComponentProvider {
    fun getAddEditHabitComponent(): AddEditHabitComponent
    fun getHabitPagerComponent(): HabitPagerComponent
    fun getHabitsListComponent(): HabitsListComponent
    fun getBottomSheetComponent(): BottomSheetComponent
}