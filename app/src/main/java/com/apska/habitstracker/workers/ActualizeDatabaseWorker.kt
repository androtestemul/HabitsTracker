package com.apska.habitstracker.workers

import android.content.Context
import android.util.Log
import androidx.work.*
import com.apska.habitstracker.App
import com.apska.habitstracker.WORK_NAME_ACTUALIZE_DATABASE
import com.apska.habitstracker.data.repository.Repository
import com.apska.habitstracker.domain.usecases.UpdateHabitsFromRemoteUseCase
import kotlinx.coroutines.Dispatchers
import java.util.concurrent.TimeUnit

class ActualizeDatabaseWorker(ctx: Context, params: WorkerParameters) : CoroutineWorker(ctx, params) {
    override suspend fun doWork(): Result {
        Log.d(TAG, "doWork: START")

        return if (UpdateHabitsFromRemoteUseCase(Repository(App()), Dispatchers.IO)
                .updateHabitsFromRemote()) {
            Result.success()
        } else {
            actualizeDatabase()
            Result.failure()
        }
    }

    companion object {
        private val TAG = this::class.java.simpleName

        fun actualizeDatabase() {
            Log.d(TAG, "newTryActualizeDatabase: START Plan Next Work")

            /*val constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()*/

            val actualizeDatabaseWorkRequest = OneTimeWorkRequestBuilder<ActualizeDatabaseWorker>()
                //.setConstraints(constraints)
                .setInitialDelay(defaultDelay(), TimeUnit.MILLISECONDS)
                .build()

            Log.d(TAG, "newTryActualizeDatabase: enqueue Work!")

            WorkManager.getInstance(App())
                .beginUniqueWork(
                    WORK_NAME_ACTUALIZE_DATABASE,
                    ExistingWorkPolicy.REPLACE,
                    actualizeDatabaseWorkRequest
                )
                .enqueue()
        }
    }
}