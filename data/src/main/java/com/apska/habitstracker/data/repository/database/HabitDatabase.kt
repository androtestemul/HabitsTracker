package com.apska.habitstracker.data.repository.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.apska.habitstracker.domain.model.Converters
import com.apska.habitstracker.domain.model.Habit
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.internal.synchronized

@Database(entities = [Habit::class], version = 5, exportSchema = false)
@TypeConverters(Converters::class)
abstract class HabitDatabase : RoomDatabase() {

    abstract val habitDatabaseDao: HabitsDao

    companion object {
        @Volatile
        private var INSTANCE: HabitDatabase? = null


        @OptIn(InternalCoroutinesApi::class)
        fun getInstance(context: Context) : HabitDatabase {
            synchronized(this) {
                var instance = INSTANCE

                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        HabitDatabase::class.java,
                        "habits_database"
                    )
                        .fallbackToDestructiveMigration()
                        .build()

                    INSTANCE = instance
                }

                return instance
            }
        }
    }
}