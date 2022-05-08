package com.apska.habitstracker.network

import com.apska.habitstracker.model.Habit
import com.apska.habitstracker.model.HabitJsonDeserializer
import com.apska.habitstracker.model.HabitJsonSerializer
import com.google.gson.GsonBuilder
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object HabitApi {
    private const val BASE_URL = "https://droid-test-server.doubletapp.ru/"
    private const val AUTH_TOKEN = "cdce755e-c8ad-445b-a96b-7bb6c9d07c0c"

    private fun getRetrofit() : Retrofit {
        val httpLoggingInterceptor = HttpLoggingInterceptor()
        httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

        val headersInterceptor = Interceptor { chain ->
            val originalRequest = chain.request()
            val builder = originalRequest.newBuilder().header(
                "Authorization",
                AUTH_TOKEN
            )
            val newRequest = builder.build()
            chain.proceed(newRequest)
        }

        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(headersInterceptor)
            .addInterceptor(httpLoggingInterceptor)
            .build()

        val gson = GsonBuilder()
            .registerTypeAdapter(Habit::class.java, HabitJsonSerializer())
            .registerTypeAdapter(Habit::class.java, HabitJsonDeserializer())
            .create()

        return Retrofit.Builder()
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .baseUrl(BASE_URL)
            .build()

    }

    val habitApiService: HabitApiService by lazy {
        getRetrofit().create(HabitApiService::class.java)
    }
}