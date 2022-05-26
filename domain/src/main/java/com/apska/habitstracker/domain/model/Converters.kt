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

    @TypeConverter
    fun toDoneDates(doneDates: String) : List<Long> {
        return doneDates.split(DELIMITER_DONE_DATE)
            .filterNot { it.isBlank() }
            .map { it.toLong() }
    }

    @TypeConverter
    fun fromDoneDates(doneDates: List<Long>) : String {
        return doneDates.joinToString(separator = DELIMITER_DONE_DATE)
    }

    companion object {
        private const val DELIMITER_DONE_DATE = "|"
    }
}