package com.apska.habitstracker.di

import android.app.Application
import com.apska.habitstracker.data.repository.di.DataModule
import com.apska.habitstracker.presentation.di.AddEditHabitComponent
import com.apska.habitstracker.presentation.di.BottomSheetComponent
import com.apska.habitstracker.presentation.di.HabitPagerComponent
import com.apska.habitstracker.presentation.di.HabitsListComponent
import com.apska.habitstracker.presentation.di.SubcomponentsModule
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
    SubcomponentsModule::class
])
interface AppComponent {

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance application: Application): AppComponent
    }

    fun getViewModelFactory(): ViewModelFactory
    fun getHabitsWorkerFactory() : HabitsWorkerFactory
    fun getAddEditHabitComponent(): AddEditHabitComponent.Factory
    fun getHabitPagerComponent(): HabitPagerComponent.Factory
    fun getHabitsListComponent(): HabitsListComponent.Factory
    fun getBottomSheetComponent(): BottomSheetComponent.Factory
}