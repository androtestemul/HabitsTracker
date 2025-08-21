package com.apska.habitstracker.presentation.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.apska.habitstracker.presentation.ui.screens.addedithabit.AddEditHabitViewModel
import com.apska.habitstracker.presentation.ui.screens.habitpager.HabitPagerViewModel
import dagger.Binds
import dagger.MapKey
import dagger.Module
import dagger.multibindings.IntoMap
import kotlin.reflect.KClass

@Module
abstract class ViewModelModule {

    @Binds
    abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(HabitPagerViewModel::class)
    abstract fun bindHabitPagerViewModel(viewModel: HabitPagerViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(AddEditHabitViewModel::class)
    abstract fun bindAddEditHabitViewModel(viewModel: AddEditHabitViewModel): ViewModel

}

@Target(
    AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY_GETTER, AnnotationTarget.PROPERTY_SETTER
)
@Retention(AnnotationRetention.RUNTIME)
@MapKey
annotation class ViewModelKey(val value: KClass<out ViewModel>)