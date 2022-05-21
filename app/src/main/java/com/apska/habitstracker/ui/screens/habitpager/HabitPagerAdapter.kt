package com.apska.habitstracker.ui.screens.habitpager

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.apska.habitstracker.domain.model.HabitType
import com.apska.habitstracker.ui.screens.habitslist.HabitsListFragment

class HabitPagerAdapter(
    fm: FragmentManager,
    lifecycle: Lifecycle
) : FragmentStateAdapter(fm, lifecycle) {

    override fun getItemCount() = HabitType.values().size

    override fun createFragment(position: Int): Fragment {
        return HabitsListFragment.newInstance(HabitType.values()[position])
    }

}