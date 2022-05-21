package com.apska.habitstracker.data.repository.network

import com.apska.habitstracker.domain.model.Habit
import com.apska.habitstracker.domain.model.PutHabitResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PUT

interface HabitApiService {

    @GET("/api/habit")
    suspend fun getHabits() : Response<List<Habit>>

    @PUT("/api/habit")
    suspend fun putHabit(@Body habit: Habit) : Response<PutHabitResponse>
}