package com.apska.habitstracker.di

import com.apska.habitstracker.domain.HabitsRepository
import com.apska.habitstracker.domain.usecases.*
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineDispatcher

@Module
class DomainModule(private val habitsRepository: HabitsRepository,
                   private val dispatcher: CoroutineDispatcher) {
    @Provides
    fun provideGetAllHabitsUseCase() =
        GetAllHabitsUseCase(habitsRepository)

    @Provides
    fun provideGetFilteredSortedHabitsUseCase() =
        GetFilteredSortedHabitsUseCase(habitsRepository)

    @Provides
    fun provideGetHabitByIdUseCase() =
        GetHabitByIdUseCase(habitsRepository, dispatcher)

    @Provides
    fun provideGetNotActualHabitsUseCase() =
        GetNotActualHabitsUseCase(habitsRepository, dispatcher)

    @Provides
    fun provideInsertHabitUseCase() = InsertHabitUseCase(habitsRepository, dispatcher)

    @Provides
    fun providePutHabitToRemoteUseCase() =
        PutHabitToRemoteUseCase(habitsRepository, dispatcher)

    @Provides
    fun provideUpdateHabitsFromRemoteUseCase() =
        UpdateHabitsFromRemoteUseCase(habitsRepository, dispatcher)

    @Provides
    fun provideUpdateHabitUseCase() =
        UpdateHabitUseCase(habitsRepository, dispatcher)

    @Provides
    fun provideDoneHabitUseCase() =
        DoneHabitUseCase(habitsRepository, dispatcher)
}