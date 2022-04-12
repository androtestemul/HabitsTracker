package com.apska.habitstracker.repository.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.apska.habitstracker.model.Habit
import com.apska.habitstracker.model.HabitPriority
import com.apska.habitstracker.repository.HabitSort.Companion.NONE
import com.apska.habitstracker.repository.HabitSort.Companion.SORT_ASC
import com.apska.habitstracker.repository.HabitSort.Companion.SORT_DESC

@Dao
interface HabitsDao {



    @Query("SELECT * FROM habits ORDER BY id ASC ")
    fun getAllHabits() : LiveData<List<Habit>>

    @Query("SELECT * FROM habits ORDER BY " +
            "CASE WHEN :periodSortOrder = $SORT_ASC THEN period END ASC, " +
            "CASE WHEN :periodSortOrder = $SORT_DESC THEN period END DESC, " +
            "CASE WHEN :periodSortOrder = $NONE THEN id END ASC ")
    fun getAllHabitsSorted(periodSortOrder: Int) : LiveData<List<Habit>>

    @Query("SELECT * FROM habits WHERE id = :id LIMIT 1")
    fun getHabit(id: Long) : Habit?

    @Insert
    fun insertHabit(habit: Habit)

    @Update
    fun updateHabit(habit: Habit)

    @Query("SELECT * FROM habits WHERE header LIKE :habitHeader ORDER BY " +
            "CASE WHEN :periodSortOrder = 0 THEN period END ASC, " +
            "CASE WHEN :periodSortOrder = 1 THEN period END DESC, " +
            "CASE WHEN :periodSortOrder = 2 THEN id END ASC ")
    fun getHeaderFilteredHabits(habitHeader: String, periodSortOrder: Int) : LiveData<List<Habit>>

    @Query("SELECT * FROM habits WHERE priority = :habitPriority ORDER BY " +
            "CASE WHEN :periodSortOrder = 0 THEN period END ASC, " +
            "CASE WHEN :periodSortOrder = 1 THEN period END DESC, " +
            "CASE WHEN :periodSortOrder = 2 THEN id END ASC ")
    fun getPriorityFilteredHabits(habitPriority: HabitPriority, periodSortOrder: Int) : LiveData<List<Habit>>

    @Query("SELECT * FROM habits WHERE header LIKE :habitHeader AND priority = :habitPriority ORDER BY " +
            "CASE WHEN :periodSortOrder = 0 THEN period END ASC, " +
            "CASE WHEN :periodSortOrder = 1 THEN period END DESC, " +
            "CASE WHEN :periodSortOrder = 2 THEN id END ASC "
    )
    fun getFilteredSortedHabits(
        habitHeader: String,
        habitPriority: HabitPriority,
        periodSortOrder: Int) : LiveData<List<Habit>>
}