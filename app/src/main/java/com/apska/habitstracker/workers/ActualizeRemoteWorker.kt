package com.apska.habitstracker.workers

import android.content.Context
import android.util.Log
import androidx.work.*
import com.apska.habitstracker.App
import com.apska.habitstracker.WORK_NAME_ACTUALIZE_REMOTE
import com.apska.habitstracker.data.repository.Repository
import com.apska.habitstracker.domain.usecases.GetNotActualHabitsUseCase
import com.apska.habitstracker.domain.usecases.PutHabitToRemoteUseCase
import com.apska.habitstracker.domain.usecases.UpdateHabitUseCase
import kotlinx.coroutines.Dispatchers
import java.util.concurrent.TimeUnit

class ActualizeRemoteWorker(context: Context, workerParameters: WorkerParameters): CoroutineWorker(context, workerParameters) {
    override suspend fun doWork(): Result {
        Log.d(TAG, "doWork: START")

        var needRetry = false

        try {
            val repository = Repository(App())
            val notActualHabits = GetNotActualHabitsUseCase(repository, Dispatchers.IO)
                .getNotActualHabits()

            notActualHabits.forEach { habit ->
                try {
                    PutHabitToRemoteUseCase(repository, Dispatchers.IO)
                        .putHabitToRemote(habit)?.let { uid ->
                            UpdateHabitUseCase(repository, Dispatchers.IO)
                                .updateHabit(habit.copy(uid = uid, isActual = true))
                        } ?: run { needRetry = true }
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