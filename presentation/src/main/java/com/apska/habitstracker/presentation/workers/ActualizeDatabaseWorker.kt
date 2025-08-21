package com.apska.habitstracker.presentation.workers

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.apska.habitstracker.presentation.WORK_NAME_ACTUALIZE_DATABASE
import com.apska.habitstracker.domain.usecases.UpdateHabitsFromRemoteUseCase
import java.util.concurrent.TimeUnit

class ActualizeDatabaseWorker(
    ctx: Context,
    params: WorkerParameters,
    private val updateHabitsFromRemoteUseCase: UpdateHabitsFromRemoteUseCase
) : CoroutineWorker(ctx, params) {
    override suspend fun doWork(): Result {
        Log.d(TAG, "doWork: START")

        return if (updateHabitsFromRemoteUseCase.updateHabitsFromRemote()) {
            Result.success()
        } else {
            actualizeDatabase(applicationContext)
            Result.failure()
        }
    }

    companion object {
        private val TAG = this::class.java.simpleName

        fun actualizeDatabase(context: Context) {
            Log.d(TAG, "newTryActualizeDatabase: START Plan Next Work")

            /*val constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()*/

            val actualizeDatabaseWorkRequest = OneTimeWorkRequestBuilder<ActualizeDatabaseWorker>()
                //.setConstraints(constraints)
                .setInitialDelay(defaultDelay(), TimeUnit.MILLISECONDS)
                .build()

            Log.d(TAG, "newTryActualizeDatabase: enqueue Work!")

            WorkManager.getInstance(context)
                .beginUniqueWork(
                    WORK_NAME_ACTUALIZE_DATABASE,
                    ExistingWorkPolicy.REPLACE,
                    actualizeDatabaseWorkRequest
                )
                .enqueue()
        }
    }
}