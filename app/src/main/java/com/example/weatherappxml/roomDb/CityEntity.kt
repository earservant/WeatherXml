package com.example.weatherappxml.roomDb

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cities")
data class CityEntity(
    @PrimaryKey val id: Int = 0,
    val cityName: String
)
