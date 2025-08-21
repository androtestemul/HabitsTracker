package com.apska.habitstracker.di

import android.app.Application
import com.apska.habitstracker.data.repository.di.DataModule
import com.apska.habitstracker.presentation.di.FragmentComponent
import com.apska.habitstracker.presentation.di.MainModule
import com.apska.habitstracker.presentation.di.ViewModelFactory
import com.apska.habitstracker.presentation.di.ViewModelModule
import com.apska.habitstracker.presentation.workers.HabitsWorkerFactory
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [
    AppModule::class,
    DataModule::class,
    DomainModule::class,
    ViewModelModule::class,
    MainModule::class
])
interface AppComponent {

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance application: Application): AppComponent
    }

    fun getViewModelFactory(): ViewModelFactory
    fun getHabitsWorkerFactory() : HabitsWorkerFactory
    fun fragmentComponent(): FragmentComponent.Factory
}