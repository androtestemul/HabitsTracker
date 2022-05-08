package com.apska.habitstracker.ui.screens.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.apska.habitstracker.R
import com.apska.habitstracker.URL_AVATAR
import com.apska.habitstracker.databinding.ActivityMainBinding
import com.apska.habitstracker.ui.screens.addedithabit.AddEditHabitFragmentArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop


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
                        if (AddEditHabitFragmentArgs.fromBundle(it).habitId == -1L) {
                            getString(R.string.header_add)
                        } else {
                            getString(R.string.header_edit)
                        }
                    } ?: kotlin.run {
                        getString(R.string.header_add)
                    }
                }
                R.id.aboutFragment -> getString(R.string.about_header)
                else -> { getString(R.string.app_name) }
            }
        }

        // подключаем Navigation Drawer к Navigation Controller
        NavigationUI.setupWithNavController(binding.navigationView, navController)

        // Подключаем кнопку вызова Navigation Drawer
        NavigationUI.setupActionBarWithNavController(this, navController, drawerLayout)

        Glide.with(this)
            .load(URL_AVATAR)
            .placeholder(R.drawable.ic_user_placeholder)
            .error(R.drawable.ic_baseline_terrain)
            //.centerCrop()
            .transform(CircleCrop())
            .into(binding.navigationView.getHeaderView(0)
                .findViewById(R.id.userAvatar))

    }

    override fun onSupportNavigateUp() = NavigationUI.navigateUp(navController, drawerLayout)

}