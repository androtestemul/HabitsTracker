package com.apska.habitstracker.ui.screens.habitslist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.apska.habitstracker.R
import com.apska.habitstracker.databinding.HabitListItemBinding
import com.apska.habitstracker.model.Habit

class HabitsViewHolder(private val binding: HabitListItemBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(habit: Habit) {
        binding.apply {
            headerTextView.text = habit.header
            priorityTextView.text = habit.priority.name
            typeTextView.text = habit.type.name
            periodTextView.text = habit.period
        }
    }

    companion object {
        fun create(parent: ViewGroup): HabitsViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.habit_list_item, parent, false)

            val binding = HabitListItemBinding.bind(view)

            return HabitsViewHolder(binding)
        }
    }
}