package com.apska.habitstracker.presentation.ui.screens.base

import android.content.Context
import androidx.fragment.app.Fragment
import com.apska.habitstracker.presentation.di.AppComponentProvider
import com.apska.habitstracker.presentation.di.FragmentComponent

abstract class BaseFragment : Fragment() {

    protected lateinit var fragmentComponent: FragmentComponent

    override fun onAttach(context: Context) {
        super.onAttach(context)

        // Получаем FragmentComponent из AppComponent
        val appComponentProvider = requireActivity().application as AppComponentProvider
        fragmentComponent = appComponentProvider.getFragmentComponent()

        // Выполняем инжекцию
        injectFragment()
    }

    abstract fun injectFragment()
}