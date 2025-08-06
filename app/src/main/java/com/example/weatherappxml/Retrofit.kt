package com.example.weatherappxml

import com.example.weatherappxml.Constants.BASE_URL
import com.example.weatherappxml.Retrofit.WeatherApiService
import com.example.weatherappxml.adapters.WeatherModel
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import kotlin.jvm.java

object RetrofitInstance {
    val api: WeatherApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(WeatherApiService::class.java)
    }
}

class Retrofit {
    interface WeatherApiService {
        @GET("forecast.json")
        suspend fun getWeather(
            @Query("key") apiKey: String,
            @Query("q") location: String,
            @Query("days") days: Int = 7,
            @Query("lang") lang: String = "ru"
        ): WeatherModel
    }
}