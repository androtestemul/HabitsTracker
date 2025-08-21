package com.apska.habitstracker.di

import com.apska.habitstracker.domain.HabitsRepository
import com.apska.habitstracker.domain.usecases.DoneHabitUseCase
import com.apska.habitstracker.domain.usecases.GetAllHabitsUseCase
import com.apska.habitstracker.domain.usecases.GetFilteredSortedHabitsUseCase
import com.apska.habitstracker.domain.usecases.GetHabitByIdUseCase
import com.apska.habitstracker.domain.usecases.GetNotActualHabitsUseCase
import com.apska.habitstracker.domain.usecases.InsertHabitUseCase
import com.apska.habitstracker.domain.usecases.PutHabitToRemoteUseCase
import com.apska.habitstracker.domain.usecases.UpdateHabitUseCase
import com.apska.habitstracker.domain.usecases.UpdateHabitsFromRemoteUseCase
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineDispatcher

@Module
class DomainModule {
    @Provides
    fun provideGetAllHabitsUseCase(habitsRepository: HabitsRepository) =
        GetAllHabitsUseCase(habitsRepository)

    @Provides
    fun provideGetFilteredSortedHabitsUseCase(habitsRepository: HabitsRepository) =
        GetFilteredSortedHabitsUseCase(habitsRepository)

    @Provides
    fun provideGetHabitByIdUseCase(habitsRepository: HabitsRepository, dispatcher: CoroutineDispatcher) =
        GetHabitByIdUseCase(habitsRepository, dispatcher)

    @Provides
    fun provideGetNotActualHabitsUseCase(habitsRepository: HabitsRepository, dispatcher: CoroutineDispatcher) =
        GetNotActualHabitsUseCase(habitsRepository, dispatcher)

    @Provides
    fun provideInsertHabitUseCase(habitsRepository: HabitsRepository, dispatcher: CoroutineDispatcher) =
        InsertHabitUseCase(habitsRepository, dispatcher)

    @Provides
    fun providePutHabitToRemoteUseCase(habitsRepository: HabitsRepository, dispatcher: CoroutineDispatcher) =
        PutHabitToRemoteUseCase(habitsRepository, dispatcher)

    @Provides
    fun provideUpdateHabitsFromRemoteUseCase(habitsRepository: HabitsRepository, dispatcher: CoroutineDispatcher) =
        UpdateHabitsFromRemoteUseCase(habitsRepository, dispatcher)

    @Provides
    fun provideUpdateHabitUseCase(habitsRepository: HabitsRepository, dispatcher: CoroutineDispatcher) =
        UpdateHabitUseCase(habitsRepository, dispatcher)

    @Provides
    fun provideDoneHabitUseCase(habitsRepository: HabitsRepository, dispatcher: CoroutineDispatcher) =
        DoneHabitUseCase(habitsRepository, dispatcher)
}