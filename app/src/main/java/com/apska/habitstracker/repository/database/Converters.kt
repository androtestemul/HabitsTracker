package com.apska.habitstracker.repository.database

import androidx.room.TypeConverter
import com.apska.habitstracker.model.HabitPriority
import com.apska.habitstracker.model.HabitType

class Converters {

    @TypeConverter
    fun toHabitPriority(habitPriorityOrdinal: Int) = enumValues<HabitPriority>()[habitPriorityOrdinal]

    @TypeConverter
    fun fromHabitPriority(habitPriority: HabitPriority?) = habitPriority?.ordinal

    @TypeConverter
    fun toHabitType(habitTypeOrdinal: Int) = enumValues<HabitType>()[habitTypeOrdinal]

    @TypeConverter
    fun fromHabitPriority(habitType: HabitType) = habitType.ordinal

}