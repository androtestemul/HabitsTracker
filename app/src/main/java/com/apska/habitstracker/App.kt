package com.apska.habitstracker

import android.app.Application
import androidx.work.Configuration
import androidx.work.WorkManager
import com.apska.habitstracker.di.AppComponent
import com.apska.habitstracker.di.DaggerAppComponent
import com.apska.habitstracker.presentation.di.AddEditHabitComponent
import com.apska.habitstracker.presentation.di.AppComponentProvider
import com.apska.habitstracker.presentation.di.BottomSheetComponent
import com.apska.habitstracker.presentation.di.HabitPagerComponent
import com.apska.habitstracker.presentation.di.HabitsListComponent
import com.apska.habitstracker.presentation.workers.ActualizeRemoteWorker

class App: Application(), AppComponentProvider {

    lateinit var appComponent: AppComponent
        private set

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

    override fun getAddEditHabitComponent(): AddEditHabitComponent {
        return appComponent.getAddEditHabitComponent().create()
    }

    override fun getHabitPagerComponent(): HabitPagerComponent {
        return appComponent.getHabitPagerComponent().create()
    }

    override fun getHabitsListComponent(): HabitsListComponent {
        return appComponent.getHabitsListComponent().create()
    }

    override fun getBottomSheetComponent(): BottomSheetComponent {
        return appComponent.getBottomSheetComponent().create()
    }
}