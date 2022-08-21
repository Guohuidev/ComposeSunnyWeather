package com.lhq.sunnyweather.ui.weather


import android.widget.Toast
import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lhq.sunnyweather.SunnyWeatherApplication
import com.lhq.sunnyweather.logic.Repository
import com.lhq.sunnyweather.logic.model.Place
import com.lhq.sunnyweather.logic.model.Weather
import kotlinx.coroutines.launch

class WeatherViewModel: ViewModel() {
    lateinit var place: Place

    var isRefreshing = false
    var mutableWeather by mutableStateOf(Weather())

    /**
     * 刷新天气
     */
    fun refreshWeather() {
        isRefreshing = true
        viewModelScope.launch {
            val weatherResult = Repository.refreshWeather(place.location.lng, place.location.lat)
            val weather = weatherResult.getOrNull()
            if (weather != null) {
                mutableWeather = weather
                isRefreshing = false
                Toast.makeText(SunnyWeatherApplication.context, "天气更新成功！", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(SunnyWeatherApplication.context, weatherResult.exceptionOrNull()?.message, Toast.LENGTH_SHORT).show()
            }
        }
    }
}