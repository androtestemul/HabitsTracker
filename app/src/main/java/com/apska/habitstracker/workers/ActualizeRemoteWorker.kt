package com.apska.habitstracker.workers

import android.content.Context
import android.util.Log
import androidx.work.*
import com.apska.habitstracker.App
import com.apska.habitstracker.WORK_NAME_ACTUALIZE_REMOTE
import com.apska.habitstracker.network.HabitApi
import com.apska.habitstracker.repository.Repository
import java.util.concurrent.TimeUnit

class ActualizeRemoteWorker(context: Context, workerParameters: WorkerParameters): CoroutineWorker(context, workerParameters) {
    override suspend fun doWork(): Result {
        Log.d(TAG, "doWork: START")

        var needRetry = false

        try {
            val repository = Repository(App())
            val notActualHabits = repository.getNotActualHabits()

            notActualHabits.forEach { habit ->
                try {
                    val response = HabitApi.habitApiService.putHabit(habit)

                    if (response.isSuccessful) {
                        val putHabitResponse = response.body()

                        putHabitResponse?.let {
                            repository.updateHabit(habit.copy(uid = putHabitResponse.uid, isActual = true))
                        }
                    } else {
                        response.errorBody()?.let {
                            throw Exception("Ошибка! Код: ${response.code()}, Текст: ${response.message()}")
                        } ?: kotlin.run {
                            throw Exception("Ошибка! Код: ${response.code()}")
                        }
                    }
                } catch (e: Exception) {
                    Log.e(TAG, "ActualizeRemote ERROR!", e)
                    needRetry = true
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "ActualizeRemote ERROR!", e)
            needRetry = true
        }

        return if (!needRetry) {
            Result.success()
        } else {
            actualizeRemote()
            Result.failure()
        }
    }

    companion object {
        private val TAG = this::class.java.simpleName

        fun actualizeRemote() {
            Log.d(TAG, "newTryActualizeRemote: START Plan Next Work")

            /*val constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()*/

            val actualizeRemoteWorkRequest = OneTimeWorkRequestBuilder<ActualizeRemoteWorker>()
                //.setConstraints(constraints)
                .setInitialDelay(defaultDelay(), TimeUnit.MILLISECONDS)
                .build()

            Log.d(TAG, "newTryActualizeRemote: enqueue Work!")

            WorkManager.getInstance(App())
                .beginUniqueWork(
                    WORK_NAME_ACTUALIZE_REMOTE,
                    ExistingWorkPolicy.REPLACE,
                    actualizeRemoteWorkRequest
                )
                .enqueue()
        }
    }
}