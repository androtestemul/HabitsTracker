package com.apska.habitstracker.ui.screens.habitslist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.apska.habitstracker.App
import com.apska.habitstracker.databinding.FragmentHabitsListBinding
import com.apska.habitstracker.domain.model.Habit
import com.apska.habitstracker.domain.model.HabitType
import com.apska.habitstracker.ui.screens.habitpager.HabitPagerFragmentDirections
import com.apska.habitstracker.ui.screens.habitpager.HabitPagerViewModel
import com.apska.habitstracker.ui.screens.habitpager.HabitPagerViewModelFactory

class HabitsListFragment : Fragment() {

    companion object {
        private const val KEY_HABIT_TYPE = "habit_type"

        fun newInstance(habitType: HabitType) : HabitsListFragment {
            val fragment = HabitsListFragment()
            fragment.arguments = bundleOf(KEY_HABIT_TYPE to habitType.ordinal)

            return fragment
        }
    }

    private var _binding: FragmentHabitsListBinding? = null
    private val binding
        get() = _binding!!

    private lateinit var habitsAdapter: HabitsAdapter
    private var habitType: HabitType? = null

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
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentHabitsListBinding.inflate(inflater, container, false)

        arguments?.let {
            habitType = HabitType.values()[it.getInt(KEY_HABIT_TYPE)]
        }

        val recyclerView = binding.habitsRecyclerView

        (recyclerView.layoutManager as LinearLayoutManager)
            .orientation = LinearLayoutManager.VERTICAL

        habitsAdapter = HabitsAdapter(
            object : HabitsAdapter.OnHabitItemClickListener {
                override fun onItemClick(habitPosition: Int) {
                    habitsAdapter.habitsList[habitPosition].id?.let {
                        habitPagerViewModel.onHabitClicked(it)
                    }
                }
            },
            object : HabitsAdapter.OnDoneClickListener {
                override fun onItemClick(habitPosition: Int) {
                    habitsAdapter.habitsList[habitPosition].id?.let {
                        habitPagerViewModel.onDoneHabit(it)
                    }
                }
            }
        )

        recyclerView.adapter = habitsAdapter

        habitPagerViewModel.habits.observe(viewLifecycleOwner) { habitList ->
            habitList?.let {
                habitsAdapter.habitsList = it.filter { habit ->
                    habit.type == habitType
                } as ArrayList<Habit>

                setEmptyListVisibility()
            }
        }

        habitPagerViewModel.navigateToHabit.observe(viewLifecycleOwner) { viewModelEvent ->
            viewModelEvent.getValue()?.let { habitId ->
                findNavController().navigate(HabitPagerFragmentDirections
                    .actionHabitPagerFragmentToAddEditHabitFragment(habitId))
            }
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setEmptyListVisibility() {
        if (habitsAdapter.itemCount != 0 && binding.emptyListTextView.visibility != View.GONE) {
            binding.emptyListTextView.visibility = View.GONE
        } else if (habitsAdapter.itemCount == 0 && binding.emptyListTextView.visibility != View.VISIBLE) {
            binding.emptyListTextView.visibility = View.VISIBLE
        }
    }
}