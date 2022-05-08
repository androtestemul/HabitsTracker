package com.apska.habitstracker

import android.app.Application
import com.apska.habitstracker.network.HabitApi
import com.apska.habitstracker.workers.ActualizeRemoteWorker
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class App: Application() {

    @OptIn(DelicateCoroutinesApi::class)
    override fun onCreate() {
        super.onCreate()

        /*GlobalScope.launch {
            val habitResponse = HabitApi.habitApiService.getHabits()

            if (habitResponse.isSuccessful) {
                val habits = habitResponse.body()

                val remoteHabitsMap = habits?.associateBy { it.uid } as HashMap

                val notActual = remoteHabitsMap.values.map { it.copy(isActual = true) }

                log.d("","main: $notActual")
            }

        }*/



        actualizeRemote()
    }

    fun actualizeRemote() {
        ActualizeRemoteWorker.actualizeRemote()
    }
}