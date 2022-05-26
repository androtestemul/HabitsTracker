package com.apska.habitstracker.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.apska.extentions.getCurrentDate
import com.apska.extentions.getDateDefaultFormatted

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
    val period: Int,
    val color: Int,

    val lastModified: Long = getCurrentDate(),
    val lastModifiedDateTime: String = getDateDefaultFormatted(lastModified),
    val isActual: Boolean = false,

    @TypeConverters(Converters::class)
    val done_dates: List<Long> = ArrayList()
)