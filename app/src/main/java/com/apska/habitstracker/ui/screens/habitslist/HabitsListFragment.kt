package com.apska.habitstracker.ui.screens.habitslist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.apska.habitstracker.R
import com.apska.habitstracker.databinding.FragmentHabitsListBinding
import com.apska.habitstracker.model.Habit
import com.apska.habitstracker.ui.screens.addedithabit.AddEditHabitFragment
import com.apska.habitstracker.ui.screens.habitpager.HabitPagerFragmentDirections

class HabitsListFragment : Fragment() {

    companion object {
        private const val KEY_HABIT_LIST = "habit_list"
        private const val KEY_CLICKED_HABIT_POSITION = "clicked_habit_position"

        fun newInstance(habitsList: ArrayList<Habit>) : HabitsListFragment {
            val fragment = HabitsListFragment()
            fragment.arguments = bundleOf(KEY_HABIT_LIST to habitsList)

            return fragment
        }
    }

    private var _binding: FragmentHabitsListBinding? = null
    private val binding
        get() = _binding!!

    private lateinit var recyclerView: RecyclerView
    private lateinit var habitsAdapter: HabitsAdapter
    private var clickedHabitPosition = -1

    private var isForViewPager = false
    private var habitsListForViewPager: ArrayList<Habit>? = null
    private var onHabitItemClickListener: HabitsAdapter.OnHabitItemClickListener? = null

    private val onHabitEditClickListener = object : HabitsAdapter.OnHabitEditClickListener {
        override fun onEditClick(habitPosition: Int) {
            clickedHabitPosition = habitPosition

            findNavController().currentDestination?.let { destination ->
                val habit = habitsAdapter.habitsList[habitPosition]

                val direction = when (destination.id) {
                    R.id.habitPagerFragment -> HabitPagerFragmentDirections
                        .actionHabitPagerFragmentToAddEditHabitFragment(habit)
                    R.id.habitsListFragment -> HabitsListFragmentDirections
                        .actionHabitsListFragmentToAddEditHabitFragment(habit)
                    else -> throw Exception("Not supported destination for Edit Habit. Destination: $destination")
                }

                findNavController().navigate(direction)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentHabitsListBinding.inflate(inflater, container, false)

        arguments?.let {
            habitsListForViewPager = it.getParcelableArrayList(KEY_HABIT_LIST)

            if (habitsListForViewPager != null) {
                isForViewPager = true
            }
        }

        recyclerView = binding.habitsRecyclerView

        (recyclerView.layoutManager as LinearLayoutManager)
            .orientation = LinearLayoutManager.VERTICAL

        if (!::habitsAdapter.isInitialized) {

            if (!isForViewPager) {
                onHabitItemClickListener = object : HabitsAdapter.OnHabitItemClickListener {
                    override fun onItemClick(habitPosition: Int) {
                        clickedHabitPosition = habitPosition

                        findNavController().navigate(HabitsListFragmentDirections
                            .actionHabitsListFragmentToHabitPagerFragment(
                                habitsAdapter.habitsList.toTypedArray(),
                                habitsAdapter.habitsList[habitPosition].type.ordinal
                            ))

                    }
                }
            }

            habitsAdapter = HabitsAdapter(onHabitItemClickListener, onHabitEditClickListener)

            if (isForViewPager) {
                habitsAdapter.habitsList = habitsListForViewPager ?: arrayListOf()
            }
        }

        recyclerView.adapter = habitsAdapter

        if (savedInstanceState != null) {
            savedInstanceState.getParcelableArrayList<Habit>(KEY_HABIT_LIST)?.let {
                habitsAdapter.habitsList = it
            }

            clickedHabitPosition = savedInstanceState.getInt(KEY_CLICKED_HABIT_POSITION)
        }

        parentFragmentManager.setFragmentResultListener(AddEditHabitFragment.REQUEST_KEY_ADD_HABIT,
            viewLifecycleOwner) { _, data ->

            val habit = data.getParcelable<Habit>(AddEditHabitFragment.EXTRA_HABIT)

            habit?.let {
                habitsAdapter.addHabit(it)
            }
        }

        //parentFragmentManager.setFragmentResultListener(AddEditHabitFragment.REQUEST_KEY_EDIT_HABIT,
        childFragmentManager.setFragmentResultListener(AddEditHabitFragment.REQUEST_KEY_EDIT_HABIT,
            viewLifecycleOwner) { _, data ->

            val habit = data.getParcelable<Habit>(AddEditHabitFragment.EXTRA_HABIT)

            habit?.let {
                habitsAdapter.replaceHabit(it, clickedHabitPosition)
            }
        }

        if (isForViewPager) {
            binding.floatingActionButtonAddHabit.visibility = View.GONE
        } else {
            binding.floatingActionButtonAddHabit.setOnClickListener {
                findNavController().navigate(HabitsListFragmentDirections
                    .actionHabitsListFragmentToAddEditHabitFragment(null))
            }
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