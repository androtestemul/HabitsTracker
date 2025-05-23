package com.apska.habitstracker.data.repository.database

import androidx.room.*
import com.apska.habitstracker.domain.model.Habit
import com.apska.habitstracker.domain.model.HabitPriority
import com.apska.habitstracker.domain.model.HabitSort.Companion.NONE
import com.apska.habitstracker.domain.model.HabitSort.Companion.SORT_ASC
import com.apska.habitstracker.domain.model.HabitSort.Companion.SORT_DESC
import kotlinx.coroutines.flow.Flow

@Dao
interface HabitsDao {

    @Query("SELECT * FROM habits ORDER BY id ASC ")
    fun getAllHabits() : Flow<List<Habit>>

    @Query("SELECT * FROM habits WHERE id = :id LIMIT 1")
    suspend fun getHabitById(id: Long) : Habit?

    @Query("SELECT * FROM habits")
    suspend fun getAllHabitsList() : List<Habit>

    @Query("SELECT * FROM habits WHERE isActual = 0")
    suspend fun getNotActualHabits() : List<Habit>

    @Insert
    suspend fun insertHabit(habit: Habit)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllHabits(habits: List<Habit>)

    @Update
    suspend fun updateHabit(habit: Habit)

    @Query("DELETE FROM habits WHERE ((uid IS NULL) OR (uid <> \"\")) AND isActual = 1")
    suspend fun deleteAllActualHabits()

    @Query("SELECT * " +
            "FROM habits " +
            "WHERE " +
                "CASE WHEN :habitHeader <> \"\" THEN header LIKE :habitHeader ELSE 1=1 END " +
                " AND " +
                "CASE WHEN :habitPriority IN (0,1,2) THEN priority = :habitPriority ELSE 1=1 END " +
            "ORDER BY " +
                "CASE WHEN :periodSortOrder = $SORT_ASC THEN period END ASC, " +
                "CASE WHEN :periodSortOrder = $SORT_DESC THEN period END DESC, " +
            "CASE WHEN :periodSortOrder = $NONE THEN uid END ASC"
    )
    fun getFilteredSortedHabits(
        habitHeader: String,
        habitPriority: HabitPriority?,
        periodSortOrder: Int) : Flow<List<Habit>>
}