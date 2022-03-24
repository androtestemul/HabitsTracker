package com.apska.habitstracker.ui.screens.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.apska.habitstracker.R
import com.apska.habitstracker.databinding.ActivityMainBinding
import com.apska.habitstracker.ui.screens.addedithabit.AddEditHabitFragmentArgs


class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController
    private lateinit var drawerLayout: DrawerLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        drawerLayout = binding.navigationDrawerLayout

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.navHostFragment) as NavHostFragment

        navController = navHostFragment.navController

        navHostFragment.navController.addOnDestinationChangedListener {
                _, destination, arguments ->

            title = when (destination.id) {
                R.id.addEditHabitFragment -> {
                    arguments?.let {
                        if (AddEditHabitFragmentArgs.fromBundle(it).habit == null) {
                            getString(R.string.header_add)
                        } else {
                            getString(R.string.header_edit)
                        }
                    } ?: kotlin.run {
                        getString(R.string.header_add)
                    }
                }
                R.id.aboutFragment -> getString(R.string.about_header)
                R.id.habitPagerFragment -> getString(R.string.habit_pager_by_type_header)
                else -> { getString(R.string.app_name) }
            }
        }

        // подключаем Navigation Drawer к Navigation Controller
        NavigationUI.setupWithNavController(binding.navigationView, navController)

        // Подключаем кнопку вызова Navigation Drawer
        NavigationUI.setupActionBarWithNavController(this, navController, drawerLayout)

    }

    override fun onSupportNavigateUp() = NavigationUI.navigateUp(navController, drawerLayout)

}