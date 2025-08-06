package com.example.weatherappxml.adapters

import com.google.gson.annotations.SerializedName

data class WeatherModel(
    @SerializedName("location") val location: Location,
    @SerializedName("current") val current: Current,
    @SerializedName("forecast") val forecast: Forecast?
)

data class Location(
    @SerializedName("name") val name: String,
    @SerializedName("localtime") val localtime: String
)

data class Current(
    @SerializedName("temp_c") val tempC: Double,
    @SerializedName("condition") val condition: Condition,
    @SerializedName("last_updated") val lastUpdated: String
)

data class Condition(
    @SerializedName("text") val text: String,
    @SerializedName("icon") val icon: String,
)

data class Forecast(
    @SerializedName("forecastday") val forecastDays: List<ForecastDay>
)

data class ForecastDay(
    @SerializedName("date") val date: String,
    @SerializedName("day") val day: Day,
    @SerializedName("hour") val hour: List<HourlyWeather>
)

data class HourlyWeather(
    @SerializedName("time") val time: String,
    @SerializedName("temp_c") val tempC: String,
    @SerializedName("condition") val condition: Condition,
)

data class Day(
    @SerializedName("maxtemp_c") val maxTempC: Double,
    @SerializedName("mintemp_c") val minTempC: Double,
    @SerializedName("condition") val condition: Condition
)

