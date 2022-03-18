package com.apska.habitstracker.ui.screens.habitslist

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.apska.habitstracker.databinding.ActivityHabitsListBinding
import com.apska.habitstracker.model.Habit
import com.apska.habitstracker.ui.screens.addedithabit.AddEditHabitActivity

class HabitsListActivity : AppCompatActivity() {

    companion object {
        private const val KEY_HABIT_LIST = "habit_list"
        private const val KEY_CLICKED_HABIT_POSITION = "clicked_habit_position"
    }

    private lateinit var binding: ActivityHabitsListBinding
    private lateinit var habitsAdapter: HabitsAdapter
    private var clickedHabitPosition = -1

    private val resultAddLauncher = registerForActivityResult(ActivityResultContracts
        .StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            result.data?.let { intent ->
                val habit = intent.getParcelableExtra<Habit>(AddEditHabitActivity.EXTRA_HABIT)

                habit?.let {
                    habitsAdapter.addHabit(it)
                    setEmptyListVisibility()
                }
            }
        }
    }

    private val resultEditLauncher = registerForActivityResult(ActivityResultContracts
        .StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            result.data?.let { intent ->
                val habit = intent.getParcelableExtra<Habit>(AddEditHabitActivity.EXTRA_HABIT)

                habit?.let {
                    habitsAdapter.replaceHabit(it, clickedHabitPosition)
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHabitsListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val recyclerView = binding.habitsRecyclerView

        (recyclerView.layoutManager as LinearLayoutManager)
            .orientation = LinearLayoutManager.VERTICAL

        habitsAdapter = HabitsAdapter(object : HabitsAdapter.OnHabitItemClickListener {
            override fun onItemClick(habitPosition: Int) {
                clickedHabitPosition = habitPosition

                resultEditLauncher.launch(AddEditHabitActivity
                    .getIntent(this@HabitsListActivity, habitsAdapter.habitsList[habitPosition]))
            }
        })

        recyclerView.adapter = habitsAdapter

        binding.floatingActionButtonAddHabit.setOnClickListener {
            resultAddLauncher.launch(AddEditHabitActivity.getIntent(this))
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putParcelableArrayList(KEY_HABIT_LIST, habitsAdapter.habitsList)
        outState.putInt(KEY_CLICKED_HABIT_POSITION, clickedHabitPosition)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)

        savedInstanceState.getParcelableArrayList<Habit>(KEY_HABIT_LIST)?.let {
            habitsAdapter.habitsList = it
        }

        clickedHabitPosition = savedInstanceState.getInt(KEY_CLICKED_HABIT_POSITION)
    }

    override fun onResume() {
        super.onResume()
        setEmptyListVisibility()
    }

    private fun setEmptyListVisibility() {
        if (habitsAdapter.itemCount != 0 && binding.emptyListTextView.visibility != View.GONE) {
            binding.emptyListTextView.visibility = View.GONE
        } else if (habitsAdapter.itemCount == 0 && binding.emptyListTextView.visibility != View.VISIBLE) {
            binding.emptyListTextView.visibility = View.VISIBLE
        }
    }
}