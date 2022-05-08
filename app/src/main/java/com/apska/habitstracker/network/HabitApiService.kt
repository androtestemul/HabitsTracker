package com.apska.habitstracker.network

import com.apska.habitstracker.model.Habit
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