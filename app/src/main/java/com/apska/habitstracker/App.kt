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

        actualizeRemote()
    }

    fun actualizeRemote() {
        ActualizeRemoteWorker.actualizeRemote()
    }
}