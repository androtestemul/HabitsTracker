package com.apska.habitstracker.domain.model

import androidx.room.TypeConverter

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