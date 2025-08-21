package com.apska.habitstracker.presentation.workers

import android.content.Context
import androidx.work.ListenableWorker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import com.apska.habitstracker.domain.usecases.GetNotActualHabitsUseCase
import com.apska.habitstracker.domain.usecases.PutHabitToRemoteUseCase
import com.apska.habitstracker.domain.usecases.UpdateHabitUseCase
import com.apska.habitstracker.domain.usecases.UpdateHabitsFromRemoteUseCase
import javax.inject.Inject

class HabitsWorkerFactory @Inject constructor(
    private val getNotActualHabitsUseCase: GetNotActualHabitsUseCase,
    private val putHabitToRemoteUseCase: PutHabitToRemoteUseCase,
    private val updateHabitUseCase: UpdateHabitUseCase,
    private val updateHabitsFromRemoteUseCase: UpdateHabitsFromRemoteUseCase
) : WorkerFactory() {
    override fun createWorker(
        appContext: Context,
        workerClassName: String,
        workerParameters: WorkerParameters,
    ): ListenableWorker? {
        return when (workerClassName) {
            ActualizeRemoteWorker::class.java.name -> {
                ActualizeRemoteWorker(
                    appContext,
                    workerParameters,
                    getNotActualHabitsUseCase,
                    putHabitToRemoteUseCase,
                    updateHabitUseCase
                )
            }
            ActualizeDatabaseWorker::class.java.name -> {
                ActualizeDatabaseWorker(
                    appContext,
                    workerParameters,
                    updateHabitsFromRemoteUseCase
                )
            }
            else -> null
        }
    }
}