package com.lhq.sunnyweather.ui.weather

import android.widget.Toast
import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lhq.sunnyweather.SunnyWeatherApplication
import com.lhq.sunnyweather.logic.Repository
import com.lhq.sunnyweather.logic.model.Weather
import kotlinx.coroutines.launch

class WeatherViewModel: ViewModel() {
    var locationLng = ""
    var locationLat = ""
    var placeName = ""

    var mutableWeather by mutableStateOf(Weather())

    /**
     * 刷新天气
     */
    fun refreshWeather(lng: String, lat: String) {
        viewModelScope.launch {
            val weatherResult = Repository.refreshWeather(lng, lat)
            val weather = weatherResult.getOrNull()
            if (weather != null) {
                mutableWeather = weather
            } else {
                Toast.makeText(SunnyWeatherApplication.context, weatherResult.exceptionOrNull()?.message, Toast.LENGTH_SHORT).show()
            }
        }
    }
}