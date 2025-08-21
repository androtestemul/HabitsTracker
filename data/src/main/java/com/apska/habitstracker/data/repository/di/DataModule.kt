package com.apska.habitstracker.data.repository.di

import android.content.Context
import androidx.room.Room
import com.apska.habitstracker.data.repository.Repository
import com.apska.habitstracker.data.repository.database.HabitDatabase
import com.apska.habitstracker.data.repository.network.HabitApi
import com.apska.habitstracker.domain.HabitsRepository
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DataModule {

    @Provides
    fun provideHabitsRepository(database : HabitDatabase, habitApi: HabitApi) : HabitsRepository {
        return Repository(database, habitApi)
    }

    @Singleton
    @Provides
    fun provideHabitsDatabase(context: Context) : HabitDatabase {
        return Room.databaseBuilder(
                context.applicationContext,
                HabitDatabase::class.java,
                "habits_database"
            )
            .fallbackToDestructiveMigration()
            .build()
    }

    @Singleton
    @Provides
    fun providesHabitsApi() : HabitApi {
        return HabitApi()
    }
}