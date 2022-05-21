package com.apska.habitstracker.ui.screens.habitslist

import android.annotation.SuppressLint
import androidx.recyclerview.widget.RecyclerView
import com.apska.habitstracker.databinding.HabitListItemBinding
import com.apska.habitstracker.domain.model.Habit
import com.apska.habitstracker.getTextValue

class HabitsViewHolder(
    private val binding: HabitListItemBinding,
    private val onHabitItemClickListener: HabitsAdapter.OnHabitItemClickListener?
) : RecyclerView.ViewHolder(binding.root) {

    init {
        binding.root.setOnClickListener {
            if (adapterPosition != RecyclerView.NO_POSITION) {
                onHabitItemClickListener?.onItemClick(adapterPosition)
            }
        }
    }

    @SuppressLint("SetTextI18n")
    fun bind(habit: Habit) {
        binding.apply {
            headerTextView.text = habit.header
            descriptionTextView.text = habit.description
            priorityTextView.text = habit.priority.getTextValue(this.root.context)
            typeTextView.text = habit.type.getTextValue(this.root.context)
            periodTextView.text = "${habit.executeCount} : ${habit.period}"

            colorView.canvasBackgroundColor = habit.color
        }
    }
}