package com.apska.habitstracker.data.repository.network

import com.apska.habitstracker.domain.model.Habit
import com.google.gson.GsonBuilder
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.reflect.Type

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

        val emptyConverterFactory = object : Converter.Factory() {
            fun converterFactory() = this
            override fun responseBodyConverter(
                type: Type,
                annotations: Array<out Annotation>,
                retrofit: Retrofit
            ) = object : Converter<ResponseBody, Any?> {
                val nextResponseBodyConverter = retrofit.nextResponseBodyConverter<Any?>(converterFactory(), type, annotations)
                override fun convert(value: ResponseBody) =
                    if (value.contentLength() != 0L)
                        nextResponseBodyConverter.convert(value)
                    else
                        null
            }
        }

        return Retrofit.Builder()
            .client(okHttpClient)
            .addConverterFactory(emptyConverterFactory)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .baseUrl(BASE_URL)
            .build()

    }

    val habitApiService: HabitApiService by lazy {
        getRetrofit().create(HabitApiService::class.java)
    }
}