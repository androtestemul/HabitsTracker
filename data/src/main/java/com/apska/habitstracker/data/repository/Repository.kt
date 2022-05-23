package com.apska.habitstracker.data.repository

import android.content.Context
import android.util.Log
import com.apska.habitstracker.data.repository.database.HabitDatabase
import com.apska.habitstracker.data.repository.database.HabitsDao
import com.apska.habitstracker.data.repository.network.HabitApi
import com.apska.habitstracker.domain.HabitsRepository
import com.apska.habitstracker.domain.model.Habit
import com.apska.habitstracker.domain.model.HabitPriority

class Repository(context: Context) : HabitsRepository {
    private val habitsDao: HabitsDao

    init {
        val database = HabitDatabase.getInstance(context)
        habitsDao = database.habitDatabaseDao
    }

    override fun getAllHabits() = habitsDao.getAllHabits()

    override suspend fun getHabitById(id: Long) = habitsDao.getHabitById(id)

    override suspend fun getNotActualHabits() = habitsDao.getNotActualHabits()

    override suspend fun insertHabit(habit: Habit) = habitsDao.insertHabit(habit)

    override suspend fun updateHabit(habit: Habit) = habitsDao.updateHabit(habit)

    override fun getFilteredSortedHabit(habitHeader: String, habitPriority: HabitPriority?, sortOrder: Int) =
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

    override suspend fun updateHabitsFromRemote() : Boolean {
        var isUpdated = false

        try {
            val habitResponse = HabitApi.habitApiService.getHabits()

            if (habitResponse.isSuccessful) {
                val habits = habitResponse.body()
                val allHabits = habitsDao.getAllHabitsList()

                habits?.let { remoteHabits ->
                    val actualRemoteHabits = getMergedActualHabits(remoteHabits, allHabits)

                    if (actualRemoteHabits.isNotEmpty()) {
                        habitsDao.insertAllHabits(actualRemoteHabits)
                    }

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

    override suspend fun putHabitToRemote(habit: Habit): String? {
        val response = HabitApi.habitApiService.putHabit(habit)

        return if (response.isSuccessful) {
            val putHabitResponse = response.body()

            putHabitResponse?.let {
                putHabitResponse.uid
            }
        } else {
            null
        }
    }

    companion object {
        private val TAG = this::class.java.simpleName
    }
}