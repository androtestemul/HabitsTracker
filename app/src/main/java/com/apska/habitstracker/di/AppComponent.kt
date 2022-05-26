package com.apska.habitstracker.di

import com.apska.habitstracker.domain.usecases.*
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [ContextModule::class, AppModule::class, DataModule::class, DomainModule::class])
interface AppComponent {

    //fun getHabitPagerViewModelFactory() : HabitPagerViewModelFactory

    fun getAllHabitsUseCase() : GetAllHabitsUseCase
    fun getFilteredSortedHabitsUseCase() : GetFilteredSortedHabitsUseCase
    fun getUpdateHabitsFromRemoteUseCase() : UpdateHabitsFromRemoteUseCase

    fun getHabitByIdUseCase() : GetHabitByIdUseCase
    fun getPutHabitToRemoteUseCase() : PutHabitToRemoteUseCase
    fun getInsertHabitUseCase() : InsertHabitUseCase
    fun getUpdateHabitUseCase() : UpdateHabitUseCase
    fun getNotActualHabitsUseCase() : GetNotActualHabitsUseCase

    fun getDoneHabitUseCase() : DoneHabitUseCase
}