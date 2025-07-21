package com.apska.habitstracker.di

import com.apska.habitstracker.domain.usecases.DoneHabitUseCase
import com.apska.habitstracker.domain.usecases.GetAllHabitsUseCase
import com.apska.habitstracker.domain.usecases.GetFilteredSortedHabitsUseCase
import com.apska.habitstracker.domain.usecases.GetHabitByIdUseCase
import com.apska.habitstracker.domain.usecases.UpdateHabitsFromRemoteUseCase
import com.apska.habitstracker.ui.screens.habitpager.HabitPagerViewModelFactory
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.Dispatchers

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

    @Provides
    fun provideCoroutineDispatcherIo() = Dispatchers.IO
}