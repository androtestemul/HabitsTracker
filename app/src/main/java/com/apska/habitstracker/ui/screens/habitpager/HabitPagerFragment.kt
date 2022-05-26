package com.apska.habitstracker.ui.screens.habitpager

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.apska.habitstracker.App
import com.apska.habitstracker.R
import com.apska.habitstracker.databinding.FragmentHabitPagerBinding
import com.apska.habitstracker.domain.model.HabitType
import com.apska.habitstracker.getTextValue
import com.apska.habitstracker.ui.screens.addedithabit.ProcessResult
import com.google.android.material.tabs.TabLayoutMediator

class HabitPagerFragment : Fragment() {
    private var _binding: FragmentHabitPagerBinding? = null
    private val binding
        get() = _binding!!

    private val habitPagerViewModel : HabitPagerViewModel by activityViewModels {
        HabitPagerViewModelFactory(
            (requireActivity().application as App).appComponent.getAllHabitsUseCase(),
            (requireActivity().application as App).appComponent.getFilteredSortedHabitsUseCase(),
            (requireActivity().application as App).appComponent.getUpdateHabitsFromRemoteUseCase(),
            (requireActivity().application as App).appComponent.getHabitByIdUseCase(),
            (requireActivity().application as App).appComponent.getDoneHabitUseCase()
        )
    }

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
                is ProcessResult.DoneHabit -> {
                    setProgressVisibility(false)

                    if (processResult.habitType == HabitType.GOOD) {
                        if (processResult.canDoCount > 0) {
                            //Стоит выполнить еще %s раз
                            val message =
                                getString(
                                    R.string.worth_doing,
                                    resources.getQuantityString(
                                        R.plurals.times,
                                        processResult.canDoCount,
                                        processResult.canDoCount)
                                )

                            Toast.makeText(
                                context,
                                message,
                                Toast.LENGTH_SHORT
                            ).show()
                        } else if (processResult.canDoCount < 0) {
                            //You are breathtaking!
                            Toast.makeText(context, R.string.you_are_breathtaking, Toast.LENGTH_SHORT).show()
                        }
                    } else if (processResult.habitType == HabitType.BAD) {
                        if (processResult.canDoCount > 0) {
                            //Можете выполнить еще %s раз
                            val message =
                                getString(
                                    R.string.сan_do,
                                    resources.getQuantityString(
                                        R.plurals.times,
                                        processResult.canDoCount,
                                        processResult.canDoCount)
                                )

                            Toast.makeText(
                                context,
                                message,
                                Toast.LENGTH_SHORT
                            ).show()
                        } else if (processResult.canDoCount < 0) {
                            //Хватит это делать
                            Toast.makeText(context, R.string.stop_doing_it, Toast.LENGTH_SHORT).show()
                        }
                    }
                }
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