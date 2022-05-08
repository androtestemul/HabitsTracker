package com.apska.habitstracker.model

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import java.lang.reflect.Type

class HabitJsonDeserializer : JsonDeserializer<Habit> {
    override fun deserialize(
        json: JsonElement,
        typeOfT: Type?,
        context: JsonDeserializationContext?,
    ) = Habit(
        uid = json.asJsonObject.get("uid").asString,
        header = json.asJsonObject.get("title").asString,
        description = json.asJsonObject.get("description").asString,
        priority = HabitPriority.values()[json.asJsonObject.get("priority").asInt],
        type = HabitType.values()[json.asJsonObject.get("type").asInt],
        executeCount = json.asJsonObject.get("count").asInt,
        period = json.asJsonObject.get("frequency").asString,
        color = json.asJsonObject.get("color").asInt,
        lastModified = json.asJsonObject.get("date").asLong
    )
}