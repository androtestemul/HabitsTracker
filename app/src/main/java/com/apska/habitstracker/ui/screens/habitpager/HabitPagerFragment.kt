package com.apska.habitstracker.ui.screens.habitpager

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.apska.habitstracker.databinding.FragmentHabitPagerBinding
import com.apska.habitstracker.model.HabitType
import com.google.android.material.tabs.TabLayoutMediator

class HabitPagerFragment : Fragment() {
    private var _binding: FragmentHabitPagerBinding? = null
    private val binding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHabitPagerBinding.inflate(inflater, container, false)

        binding.viewPager.adapter = HabitPagerAdapter(childFragmentManager, lifecycle)

        binding.floatingActionButtonAddHabit.setOnClickListener {
            findNavController().navigate(HabitPagerFragmentDirections
                .actionHabitPagerFragmentToAddEditHabitFragment())
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        TabLayoutMediator(binding.tabLayout, binding.viewPager){ tab, position ->
            tab.text = HabitType.values()[position].getTextValue(requireContext())
        }.attach()
    }
}