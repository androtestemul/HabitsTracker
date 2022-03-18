package com.apska.habitstracker.ui.screens.habitslist

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.apska.habitstracker.R
import com.apska.habitstracker.databinding.FragmentHabitsListBinding
import com.apska.habitstracker.model.Habit
import com.apska.habitstracker.ui.screens.addedithabit.AddEditHabitFragment

class HabitsListFragment : Fragment() {

    companion object {
        private const val KEY_HABIT_LIST = "habit_list"
        private const val KEY_CLICKED_HABIT_POSITION = "clicked_habit_position"
    }

    private var _binding: FragmentHabitsListBinding? = null
    private val binding
        get() = _binding!!

    private lateinit var habitsAdapter: HabitsAdapter
    private var clickedHabitPosition = -1

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentHabitsListBinding.inflate(inflater, container, false)

        val recyclerView = binding.habitsRecyclerView

        (recyclerView.layoutManager as LinearLayoutManager)
            .orientation = LinearLayoutManager.VERTICAL

        habitsAdapter = HabitsAdapter(object : HabitsAdapter.OnHabitItemClickListener {
            override fun onItemClick(habitPosition: Int) {
                clickedHabitPosition = habitPosition

                findNavController().navigate(HabitsListFragmentDirections
                    .actionHabitsListFragmentToAddEditHabitFragment(habitsAdapter.habitsList[habitPosition]))

            }
        })
        recyclerView.adapter = habitsAdapter

        if (savedInstanceState != null) {
            savedInstanceState.getParcelableArrayList<Habit>(KEY_HABIT_LIST)?.let {
                habitsAdapter.habitsList = it
            }

            clickedHabitPosition = savedInstanceState.getInt(KEY_CLICKED_HABIT_POSITION)
        }



        parentFragmentManager.setFragmentResultListener(AddEditHabitFragment.REQUEST_KEY_ADD_HABIT,
            viewLifecycleOwner){ _, data ->

            val habit = data.getParcelable<Habit>(AddEditHabitFragment.EXTRA_HABIT)

                    habit?.let {
                        habitsAdapter.addHabit(it)
                        setEmptyListVisibility()
                    }
                }

        parentFragmentManager.setFragmentResultListener(AddEditHabitFragment.REQUEST_KEY_EDIT_HABIT,
            viewLifecycleOwner){ _, data ->

            val habit = data.getParcelable<Habit>(AddEditHabitFragment.EXTRA_HABIT)

                    habit?.let {
                        habitsAdapter.replaceHabit(it, clickedHabitPosition)
                    }
                }

        binding.floatingActionButtonAddHabit.setOnClickListener {
            findNavController().navigate(HabitsListFragmentDirections
                .actionHabitsListFragmentToAddEditHabitFragment(null))
        }

        return binding.root
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putParcelableArrayList(KEY_HABIT_LIST, habitsAdapter.habitsList)
        outState.putInt(KEY_CLICKED_HABIT_POSITION, clickedHabitPosition)
    }

    /*override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)

        savedInstanceState.getParcelableArrayList<Habit>(KEY_HABIT_LIST)?.let {
            habitsAdapter.habitsList = it
        }

        clickedHabitPosition = savedInstanceState.getInt(KEY_CLICKED_HABIT_POSITION)
    }*/

    override fun onResume() {
        super.onResume()
        setEmptyListVisibility()
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