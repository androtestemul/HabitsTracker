package com.apska.habitstracker.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.apska.habitstracker.repository.database.Converters
import java.util.*

@Entity(tableName = "habits")
data class Habit(
    @PrimaryKey(autoGenerate = true)
    val id: Long? = null,
    val uid: String,
    val header: String,
    val description: String,

    @TypeConverters(Converters::class)
    val priority: HabitPriority,

    @TypeConverters(Converters::class)
    val type: HabitType,

    val executeCount: Int,
    val period: String,
    val color: Int,

    val lastModified: Long = Calendar.getInstance().timeInMillis,
    val isActual: Boolean = false
)