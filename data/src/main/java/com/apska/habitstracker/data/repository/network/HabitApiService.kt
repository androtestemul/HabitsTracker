package com.apska.habitstracker.data.repository.network

import com.apska.habitstracker.domain.model.Habit
import com.apska.habitstracker.domain.model.PutHabitResponse
import com.apska.habitstracker.domain.model.RequestDoneHabit
import com.apska.habitstracker.domain.model.ResponseDoneHabit
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT

interface HabitApiService {

    @GET("/api/habit")
    suspend fun getHabits() : Response<List<Habit>>

    @PUT("/api/habit")
    suspend fun putHabit(@Body habit: Habit) : Response<PutHabitResponse>

    @POST("/api/habit_done")
    suspend fun doneHabit(@Body requestDoneHabit: RequestDoneHabit) : Response<ResponseDoneHabit>?

}