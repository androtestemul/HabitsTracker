package com.apska.habitstracker.di

import android.app.Application
import com.apska.habitstracker.data.repository.Repository
import com.apska.habitstracker.domain.HabitsRepository
import dagger.Module
import dagger.Provides

@Module
class DataModule(private val application: Application) {

    @Provides
    fun provideHabitsRepository() : HabitsRepository {
        return Repository(application)
    }
}