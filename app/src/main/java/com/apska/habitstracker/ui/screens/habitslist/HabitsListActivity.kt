package com.apska.habitstracker.ui.screens.habitslist

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.apska.habitstracker.databinding.ActivityHabitsListBinding

class HabitsListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHabitsListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHabitsListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val recyclerView = binding.habitsRecyclerView

        (recyclerView.layoutManager as LinearLayoutManager)
            .orientation = LinearLayoutManager.VERTICAL

        val habitsAdapter = HabitsAdapter()
        recyclerView.adapter = habitsAdapter

        if (habitsAdapter.itemCount == 0) {
            binding.emptyListTextView.visibility = View.VISIBLE
        }

        binding.floatingActionButtonAddHabit.setOnClickListener {

        }
    }
}