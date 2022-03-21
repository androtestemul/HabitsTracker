package com.apska.habitstracker.ui.screens.habitpager

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.apska.habitstracker.databinding.FragmentHabitPagerBinding

class HabitPagerFragment : Fragment() {
    private var _binding: FragmentHabitPagerBinding? = null
    private val binding
        get() = _binding!!

    private val args: HabitPagerFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHabitPagerBinding.inflate(inflater, container, false)

        val habitsList = args.habitsList

        val habitPagerAdapter = HabitPagerAdapter(
            habitsList.toCollection(arrayListOf()),
            childFragmentManager,
            lifecycle
        )

        binding.viewPager.apply {
            adapter = habitPagerAdapter
            setCurrentItem(args.defaultType, false)
        }

        return binding.root
    }
}