package com.apska.habitstracker.ui.screens.habitpager

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.apska.habitstracker.R
import com.apska.habitstracker.databinding.FragmentHabitPagerBinding
import com.apska.habitstracker.model.HabitType
import com.apska.habitstracker.ui.screens.addedithabit.FormError
import com.apska.habitstracker.ui.screens.addedithabit.ProcessResult
import com.google.android.material.tabs.TabLayoutMediator

class HabitPagerFragment : Fragment() {
    private var _binding: FragmentHabitPagerBinding? = null
    private val binding
        get() = _binding!!

    private val habitPagerViewModel : HabitPagerViewModel by activityViewModels()

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

        habitPagerViewModel.processResult.observe(viewLifecycleOwner) { processResult ->
            processResult ?: return@observe

            when (processResult) {
                is ProcessResult.LOADED -> setProgressVisibility(false)
                is ProcessResult.ERROR -> {
                    setProgressVisibility(false)

                    val errorDialog = AlertDialog.Builder(requireContext(), R.style.AlertDialogApp)
                        .setMessage(processResult.message ?: getString(R.string.error_has_occurred))
                        .setTitle(R.string.dialog_header)
                        .create()

                    errorDialog.show()
                }
                is ProcessResult.PROCESSING -> setProgressVisibility(true)
                else -> {}
            }
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        TabLayoutMediator(binding.tabLayout, binding.viewPager){ tab, position ->
            tab.text = HabitType.values()[position].getTextValue(requireContext())
        }.attach()
    }

    private fun setProgressVisibility(show: Boolean) {
        if (show){
            binding.progressOverlay.visibility = View.VISIBLE
        } else {
            binding.progressOverlay.visibility = View.GONE
        }
    }
}