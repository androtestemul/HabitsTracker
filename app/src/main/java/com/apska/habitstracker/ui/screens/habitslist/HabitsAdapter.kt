package com.apska.habitstracker.ui.screens.habitslist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.apska.habitstracker.R
import com.apska.habitstracker.databinding.HabitListItemBinding
import com.apska.habitstracker.model.Habit


class HabitsAdapter(private val onHabitItemClickListener: OnHabitItemClickListener) : RecyclerView.Adapter<HabitsViewHolder>() {

    var habitsList: ArrayList<Habit> = arrayListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HabitsViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.habit_list_item, parent, false)

        val binding = HabitListItemBinding.bind(view)

        return HabitsViewHolder(binding, onHabitItemClickListener)
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

    interface OnHabitItemClickListener {
        fun onItemClick(habitPosition: Int)
    }
}