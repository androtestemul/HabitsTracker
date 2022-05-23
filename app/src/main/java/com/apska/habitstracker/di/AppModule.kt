package com.apska.habitstracker.di

import android.app.Application
import android.content.Context
import com.apska.habitstracker.data.repository.database.HabitDatabase
import com.apska.habitstracker.data.repository.database.HabitsDao
import com.apska.habitstracker.domain.usecases.GetAllHabitsUseCase
import com.apska.habitstracker.domain.usecases.GetFilteredSortedHabitsUseCase
import com.apska.habitstracker.domain.usecases.UpdateHabitsFromRemoteUseCase
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
        updateHabitsFromRemoteUseCase: UpdateHabitsFromRemoteUseCase
    ) = HabitPagerViewModelFactory(
        getAllHabitsUseCase,
        getFilteredSortedHabitsUseCase,
        updateHabitsFromRemoteUseCase
    )

}