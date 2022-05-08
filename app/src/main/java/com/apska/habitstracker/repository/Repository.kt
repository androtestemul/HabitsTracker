package com.apska.habitstracker.repository

import android.app.Application
import android.util.Log
import com.apska.habitstracker.model.Habit
import com.apska.habitstracker.model.HabitPriority
import com.apska.habitstracker.network.HabitApi
import com.apska.habitstracker.repository.database.HabitDatabase
import com.apska.habitstracker.repository.database.HabitsDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class Repository(application: Application) {
    private val habitsDao: HabitsDao

    init {
        val database = HabitDatabase.getInstance(application)
        habitsDao = database.habitDatabaseDao
    }

    fun getAllHabits() = habitsDao.getAllHabits()

    suspend fun getHabitById(id: Long) = withContext(Dispatchers.IO) {
        habitsDao.getHabitById(id)
    }

    suspend fun getNotActualHabits() = withContext(Dispatchers.IO) {
        habitsDao.getNotActualHabits()
    }

    private suspend fun insertAllHabits(habits: List<Habit>) = withContext(Dispatchers.IO) {
        if (habits.isNotEmpty()) {
            habitsDao.insertAllHabits(habits)
        }
    }

    suspend fun insertHabit(habit: Habit) = habitsDao.insertHabit(habit)

    suspend fun updateHabit(habit: Habit) = habitsDao.updateHabit(habit)

    fun getFilteredSortedHabit(habitHeader: String, habitPriority: HabitPriority?, sortOrder: Int) =
        habitsDao.getFilteredSortedHabits(habitHeader, habitPriority, sortOrder)

    private fun getMergedActualHabits(remoteHabits: List<Habit>, databaseHabits: List<Habit>) : List<Habit> {

        if (remoteHabits.isEmpty()) {
            return emptyList()
        }

        val remoteHabitsMap = remoteHabits.associateBy { it.uid } as HashMap

        databaseHabits.forEach { databaseHabit ->
            if (!databaseHabit.isActual) {
                remoteHabitsMap.remove(databaseHabit.uid)
            } else if (databaseHabit.uid.isNotBlank() && databaseHabit.isActual) {
                if (remoteHabitsMap.containsKey(databaseHabit.uid)) {
                    val habitWithId = remoteHabitsMap[databaseHabit.uid]!!.copy(id = databaseHabit.id)
                    remoteHabitsMap[databaseHabit.uid] = habitWithId
                }
            }
        }

        return remoteHabitsMap.values.map { it.copy(isActual = true) }
    }

    suspend fun updateHabitsFromRemote() : Boolean {
        var isUpdated = false

        try {
            val habitResponse = HabitApi.habitApiService.getHabits()

            if (habitResponse.isSuccessful) {
                val habits = habitResponse.body()
                val allHabits = habitsDao.getAllHabitsList()

                habits?.let { remoteHabits ->
                    val actualRemoteHabits = getMergedActualHabits(remoteHabits, allHabits)

                    insertAllHabits(actualRemoteHabits)
                    isUpdated = true
                }
            } else {
                habitResponse.errorBody()?.let {
                    throw Exception("Ошибка! Код: ${habitResponse.code()}, Текст: $it")
                } ?: kotlin.run {
                    throw Exception("Ошибка! Код: ${habitResponse.code()}")
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "updateHabitsFromRemote: ", e)
        }

        return isUpdated

    }

    companion object {
        private val TAG = this::class.java.simpleName
    }
}