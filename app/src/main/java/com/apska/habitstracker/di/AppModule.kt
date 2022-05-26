package com.apska.habitstracker.di

import android.app.Application
import android.content.Context
import com.apska.habitstracker.data.repository.database.HabitDatabase
import com.apska.habitstracker.data.repository.database.HabitsDao
import com.apska.habitstracker.domain.usecases.*
import com.apska.habitstracker.ui.screens.habitpager.HabitPagerViewModelFactory
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule {

    @Provides
    fun provideHabitPagerViewModelFactory(
        getAllHabitsUseCase: GetAllHabitsUseCase,
        getFilteredSortedHabitsUseCase: GetFilteredSortedHabitsUseCase,
        updateHabitsFromRemoteUseCase: UpdateHabitsFromRemoteUseCase,
        getHabitByIdUseCase: GetHabitByIdUseCase,
        doneHabitUseCase: DoneHabitUseCase
    ) = HabitPagerViewModelFactory(
        getAllHabitsUseCase,
        getFilteredSortedHabitsUseCase,
        updateHabitsFromRemoteUseCase,
        getHabitByIdUseCase,
        doneHabitUseCase
    )

}