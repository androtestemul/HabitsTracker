package com.apska.habitstracker.ui.screens.habitslist

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.apska.habitstracker.model.Habit

class HabitsAdapter : RecyclerView.Adapter<HabitsViewHolder>() {
    val habitsList: ArrayList<Habit> = arrayListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HabitsViewHolder {
        return HabitsViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: HabitsViewHolder, position: Int) {
        holder.bind(habitsList[position])
    }

    override fun getItemCount() = habitsList.size
}