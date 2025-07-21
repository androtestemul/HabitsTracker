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
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

@Database(entities = [Habit::class], version = 6, exportSchema = false)
@TypeConverters(Converters::class)
abstract class HabitDatabase : RoomDatabase() {

    abstract val habitDatabaseDao: HabitsDao

    /*companion object {
        @Volatile
        private var instance: HabitDatabase? = null
        private val mutex = Mutex()

        *//*Double-Checked Locking Example*//*

        @OptIn(InternalCoroutinesApi::class)
        fun getInstance(context: Context) : HabitDatabase {
            if (instance == null) {
                synchronized(this) {
                    if (instance == null) {
                        instance = Room.databaseBuilder(
                            context.applicationContext,
                            HabitDatabase::class.java,
                            "habits_database"
                        )
                            .fallbackToDestructiveMigration(false)
                            .build()
                    }
                }
            }
            return instance!!
        }

        suspend fun getInstanceSuspend(context: Context) : HabitDatabase {
            instance?.let { return it }

            return mutex.withLock {
                instance ?: Room.databaseBuilder(
                    context.applicationContext,
                    HabitDatabase::class.java,
                    "habits_database"
                )
                    .fallbackToDestructiveMigration(false)
                    .build()
                    .also { instance = it }
            }
        }
    }*/
}