package com.apska.habitstracker.ui.screens.habitslist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.apska.habitstracker.R
import com.apska.habitstracker.databinding.HabitListItemBinding
import com.apska.habitstracker.model.Habit


class HabitsAdapter : RecyclerView.Adapter<HabitsAdapter.HabitsViewHolder>() {
    lateinit var onHabitItemClickListener: OnHabitItemClickListener

    var habitsList: ArrayList<Habit> = arrayListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HabitsViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.habit_list_item, parent, false)

        val binding = HabitListItemBinding.bind(view)

        return HabitsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HabitsViewHolder, position: Int) {
        holder.bind(habitsList[position])
    }

    override fun getItemCount() = habitsList.size

    fun addHabit(habit: Habit) {
        habitsList.add(habit)
        this.notifyItemInserted(itemCount)
    }

    fun replaceHabit(habit: Habit, position: Int) {
        habitsList[position] = habit
        this.notifyItemChanged(position)
    }


    inner class HabitsViewHolder(private val binding: HabitListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

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

    interface OnHabitItemClickListener {
        fun onItemClick(habitPosition: Int)
    }
}