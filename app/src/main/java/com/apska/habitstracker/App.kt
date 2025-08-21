package com.apska.habitstracker

import android.app.Application
import androidx.work.Configuration
import androidx.work.WorkManager
import com.apska.habitstracker.di.AppComponent
import com.apska.habitstracker.di.DaggerAppComponent
import com.apska.habitstracker.presentation.di.AppComponentProvider
import com.apska.habitstracker.presentation.di.FragmentComponent
import com.apska.habitstracker.presentation.workers.ActualizeRemoteWorker

class App: Application(), AppComponentProvider {

    lateinit var appComponent: AppComponent
        private set

    override fun getFragmentComponent(): FragmentComponent {
        return appComponent.fragmentComponent().create()
    }

    override fun onCreate() {
        super.onCreate()

        appComponent = DaggerAppComponent.factory()
            .create(this)

        val myWorkerFactory = appComponent.getHabitsWorkerFactory()

        val config = Configuration.Builder()
            .setWorkerFactory(myWorkerFactory)
            .build()

        WorkManager.initialize(this, config)

        actualizeRemote()
    }

    fun actualizeRemote() {
        ActualizeRemoteWorker.Companion.actualizeRemote(this)
    }
}