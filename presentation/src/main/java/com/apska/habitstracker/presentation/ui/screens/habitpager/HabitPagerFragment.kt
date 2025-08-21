package com.apska.habitstracker.presentation.ui.screens.habitpager

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.apska.habitstracker.domain.model.HabitType
import com.apska.habitstracker.presentation.R
import com.apska.habitstracker.presentation.databinding.FragmentHabitPagerBinding
import com.apska.habitstracker.presentation.di.AppComponentProvider
import com.apska.habitstracker.presentation.di.HabitPagerComponent
import com.apska.habitstracker.presentation.di.ViewModelFactory
import com.apska.habitstracker.presentation.getTextValue
import com.apska.habitstracker.presentation.ui.screens.addedithabit.ProcessResult
import com.google.android.material.tabs.TabLayoutMediator
import javax.inject.Inject

class HabitPagerFragment : Fragment() {
    private var _binding: FragmentHabitPagerBinding? = null
    private val binding
        get() = _binding!!

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private val habitPagerViewModel : HabitPagerViewModel by viewModels { viewModelFactory }

    private lateinit var habitPagerComponent: HabitPagerComponent

    override fun onAttach(context: Context) {
        super.onAttach(context)

        val appComponentProvider = requireActivity().application as AppComponentProvider
        habitPagerComponent = appComponentProvider.getHabitPagerComponent()

        habitPagerComponent.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHabitPagerBinding.inflate(inflater, container, false)

        binding.viewPager.adapter = HabitPagerAdapter(childFragmentManager, lifecycle)

        binding.floatingActionButtonAddHabit.setOnClickListener {
            findNavController().navigate(
                HabitPagerFragmentDirections
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
            tab.text = HabitType.entries.get(position).getTextValue(requireContext())
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