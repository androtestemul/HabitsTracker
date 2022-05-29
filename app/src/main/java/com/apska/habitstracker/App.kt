package com.apska.habitstracker

import android.app.Application
import com.apska.habitstracker.data.repository.Repository
import com.apska.habitstracker.data.repository.database.HabitDatabase
import com.apska.habitstracker.data.repository.network.HabitApi
import com.apska.habitstracker.di.*
import com.apska.habitstracker.workers.ActualizeRemoteWorker
import kotlinx.coroutines.Dispatchers

class App: Application() {

    lateinit var appComponent: AppComponent
        private set

    override fun onCreate() {
        super.onCreate()

        appComponent = DaggerAppComponent
            .builder()
            .contextModule(ContextModule(this))
            .appModule(AppModule())
            .domainModule(DomainModule(
                Repository(HabitDatabase.getInstance(applicationContext), HabitApi()),
                Dispatchers.IO)
            )
            .build()

        actualizeRemote()
    }

    fun actualizeRemote() {
        ActualizeRemoteWorker.actualizeRemote()
    }
}