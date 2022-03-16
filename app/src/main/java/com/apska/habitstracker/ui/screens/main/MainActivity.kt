package com.apska.habitstracker.ui.screens.main

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.apska.habitstracker.R
import com.apska.habitstracker.databinding.ActivityMainBinding
import com.apska.habitstracker.model.Habit
import com.apska.habitstracker.ui.screens.HasTitle
import com.apska.habitstracker.ui.screens.addedithabit.AddEditHabitFragment
import com.apska.habitstracker.ui.screens.habitslist.HabitsListFragment


class MainActivity : AppCompatActivity() {

    //private lateinit var navController: NavController

    private val currentFragment: Fragment
        get() = supportFragmentManager.findFragmentById(R.id.navHostFragment)!!

    private val fragmentListener = object : FragmentManager.FragmentLifecycleCallbacks() {
        override fun onFragmentViewCreated(
            fm: FragmentManager,
            f: Fragment,
            v: View,
            savedInstanceState: Bundle?,
        ) {
            super.onFragmentViewCreated(fm, f, v, savedInstanceState)
            updateUi()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportFragmentManager.registerFragmentLifecycleCallbacks(fragmentListener, false)

        val navHostFragment = supportFragmentManager.findFragmentById(
            R.id.navHostFragment
        ) as NavHostFragment

        navHostFragment.navController
            .addOnDestinationChangedListener {
                controller, destination, arguments ->

            if (destination.id)
        })

        navController = navHostFragment.navController


    }

    override fun onDestroy() {
        super.onDestroy()
        supportFragmentManager.unregisterFragmentLifecycleCallbacks(fragmentListener)
    }

    private fun updateUi() {
        val fragment = currentFragment

        title = if (fragment is HasTitle) {
            getString(fragment.getTitle())
        } else {
            getString(R.string.app_name)
        }
    }
}