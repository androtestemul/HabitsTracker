package com.apska.habitstracker.ui.screens.habitslist

import androidx.recyclerview.widget.RecyclerView
import com.apska.habitstracker.databinding.HabitListItemBinding
import com.apska.habitstracker.model.Habit

class HabitsViewHolder(
    private val binding: HabitListItemBinding,
    private val onHabitItemClickListener: HabitsAdapter.OnHabitItemClickListener
) : RecyclerView.ViewHolder(binding.root) {

    init {
        binding.root.setOnClickListener {
            if (adapterPosition != RecyclerView.NO_POSITION) {
                onHabitItemClickListener.onItemClick(adapterPosition)
            }
        }
    }

    fun bind(habit: Habit) {
        binding.apply {
            headerTextView.text = habit.header
            descriptionTextView.text = habit.description
            priorityTextView.text = habit.priority.getTextValue(this.root.context)
            typeTextView.text = habit.type.getTextValue(this.root.context)
            periodTextView.text = habit.period

            colorView.canvasBackgroundColor = habit.color
        }
    }
}