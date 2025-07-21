package com.apska.habitstracker

import android.app.Application
import androidx.work.Configuration
import androidx.work.WorkManager
import com.apska.habitstracker.di.AppComponent
import com.apska.habitstracker.di.AppModule
import com.apska.habitstracker.di.ContextModule
import com.apska.habitstracker.di.DaggerAppComponent
import com.apska.habitstracker.di.DomainModule
import com.apska.habitstracker.workers.ActualizeRemoteWorker

class App: Application() {

    lateinit var appComponent: AppComponent
        private set

    override fun onCreate() {
        super.onCreate()

        appComponent = DaggerAppComponent
            .builder()
            .contextModule(ContextModule(this))
            .appModule(AppModule())
            .domainModule(DomainModule())
            .build()

        val myWorkerFactory = appComponent.getHabitsWorkerFactory()

        val config = Configuration.Builder()
            .setWorkerFactory(myWorkerFactory)
            .build()

        WorkManager.initialize(this, config)

        actualizeRemote()
    }

    fun actualizeRemote() {
        ActualizeRemoteWorker.actualizeRemote()
    }
}