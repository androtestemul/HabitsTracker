package com.apska.habitstracker.ui.screens.habitpager

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.apska.habitstracker.model.Habit

import com.apska.habitstracker.model.HabitType
import com.apska.habitstracker.ui.screens.habitslist.HabitsListFragment

class HabitPagerAdapter(
    private val habitsList: ArrayList<Habit>,
    fm: FragmentManager,
    lifecycle: Lifecycle
) : FragmentStateAdapter(fm, lifecycle) {

    override fun getItemCount() = HabitType.values().size

    override fun createFragment(position: Int): Fragment {
        return HabitsListFragment.newInstance(habitsList.filter { habit ->
            habit.type.ordinal == position
        } as ArrayList<Habit>)
    }

}