package com.apska.habitstracker.presentation.workers

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.apska.habitstracker.domain.usecases.GetNotActualHabitsUseCase
import com.apska.habitstracker.domain.usecases.PutHabitToRemoteUseCase
import com.apska.habitstracker.domain.usecases.UpdateHabitUseCase
import com.apska.habitstracker.presentation.WORK_NAME_ACTUALIZE_REMOTE
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class ActualizeRemoteWorker @Inject constructor(
    context: Context,
    workerParameters: WorkerParameters,
    private val getNotActualHabitsUseCase: GetNotActualHabitsUseCase,
    private val putHabitToRemoteUseCase: PutHabitToRemoteUseCase,
    private val updateHabitUseCase: UpdateHabitUseCase,
): CoroutineWorker(context, workerParameters) {
    override suspend fun doWork(): Result {
        Log.d(TAG, "doWork: START")

        var needRetry = false

        try {
            val notActualHabits = getNotActualHabitsUseCase.getNotActualHabits()

            notActualHabits.forEach { habit ->
                try {
                    putHabitToRemoteUseCase.putHabitToRemote(habit)?.let { uid ->
                        updateHabitUseCase.updateHabit(habit.copy(uid = uid, isActual = true))
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
            actualizeRemote(applicationContext)
            Result.failure()
        }
    }

    companion object {
        private val TAG = this::class.java.simpleName

        fun actualizeRemote(context: Context) {
            Log.d(TAG, "newTryActualizeRemote: START Plan Next Work")

            /*val constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()*/

            val actualizeRemoteWorkRequest = OneTimeWorkRequestBuilder<ActualizeRemoteWorker>()
                //.setConstraints(constraints)
                .setInitialDelay(defaultDelay(), TimeUnit.MILLISECONDS)
                .build()

            Log.d(TAG, "newTryActualizeRemote: enqueue Work!")

            WorkManager.getInstance(context)
                .beginUniqueWork(
                    WORK_NAME_ACTUALIZE_REMOTE,
                    ExistingWorkPolicy.REPLACE,
                    actualizeRemoteWorkRequest
                )
                .enqueue()
        }
    }
}