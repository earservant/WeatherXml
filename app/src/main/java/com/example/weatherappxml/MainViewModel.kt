package com.example.weatherappxml

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.weatherappxml.Constants.API_KEY
import com.example.weatherappxml.adapters.ForecastDay
import com.example.weatherappxml.adapters.WeatherModel
import com.example.weatherappxml.roomDb.AppDatabase
import com.example.weatherappxml.roomDb.CityEntity
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val _uiState: MutableLiveData<MainUiState> = MutableLiveData<MainUiState>(MainUiState.Loading)
    val uiState get() = _uiState as LiveData<MainUiState>

    private val cityDao = AppDatabase.getDatabase(application).cityDao()

    val liveDataList = MutableLiveData<List<WeatherModel>>()
    val liveDataSelectedDay = MutableLiveData<ForecastDay>()

    // Метод для сохранения выбранного города
    fun saveCity(cityName: String) {
        viewModelScope.launch {
            cityDao.clearCities() // Удалить предыдущий город
            cityDao.insertCity(CityEntity(cityName = cityName))
        }
    }

    // Метод для загрузки сохранённого города
    fun getSavedCity(): LiveData<String?> {
        val savedCity = MutableLiveData<String?>()
        viewModelScope.launch {
            val city = cityDao.getSelectedCity()
            savedCity.postValue(city?.cityName)
        }
        return savedCity
    }

    // Получение погоды для сохранённого или выбранного города
    fun fetchWeather(city: String) {
        viewModelScope.launch {
            try {
                val response = RetrofitInstance.api.getWeather(API_KEY, city)

                _uiState.postValue(
                    MainUiState.Data(
                        response
                    )
                )
            } catch (e: Exception) {
                _uiState.postValue(
                    MainUiState.Error(
                        e.message ?: "unknown error"
                    )
                )
                e.printStackTrace()
            }
        }
    }
}