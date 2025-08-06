package com.example.weatherappxml.roomDb

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface CityDao {
    @Query("SELECT * FROM cities LIMIT 1")
    suspend fun getSelectedCity(): CityEntity?

    @Insert
    suspend fun insertCity(city: CityEntity)

    @Query("DELETE FROM cities")
    suspend fun clearCities()
}