package com.apska.habitstracker.data.repository.network

import com.apska.habitstracker.domain.model.Habit
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import java.lang.reflect.Type

class HabitJsonSerializer : JsonSerializer<Habit> {
    override fun serialize(
        habit: Habit,
        typeOfSrc: Type,
        context: JsonSerializationContext,
    ): JsonElement = JsonObject().apply {

        addProperty("color", habit.color)
        addProperty("count", habit.executeCount)
        addProperty("date", habit.lastModified)
        addProperty("description", habit.description)
        addProperty("frequency", habit.period)
        addProperty("priority", habit.priority.ordinal)
        addProperty("title", habit.header)
        addProperty("type", habit.type.ordinal)
        addProperty("done_dates", habit.done_dates.toString())

        if (habit.uid.isNotBlank()) {
            addProperty("uid", habit.uid)
        }
    }

}