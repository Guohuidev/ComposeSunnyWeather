package com.lhq.sunnyweather.ui.place

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.*
import com.lhq.sunnyweather.SunnyWeatherApplication
import com.lhq.sunnyweather.logic.Repository
import com.lhq.sunnyweather.logic.model.Place
import com.lhq.sunnyweather.ui.weather.WeatherActivity
import kotlinx.coroutines.launch


class PlaceViewModel : ViewModel() {
    val placeList = mutableStateListOf<Place>()

    fun searchPlaces(query: String) {
        viewModelScope.launch {
            val placeResult: Result<List<Place>> = Repository.searchPlaces(query)
            val places: List<Place>? = placeResult.getOrNull()
            if (places != null) {
                placeList.clear()
                placeList.addAll(places)
            } else {
                Toast.makeText(SunnyWeatherApplication.context, placeResult.exceptionOrNull()?.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    /**
     * 跳转到天气页面
     */
    fun jump(context: Context, lng: String, lat: String, placeName: String) {
        val intent = Intent(context, WeatherActivity::class.java).apply {
            putExtra("location_lng", lng)
            putExtra("location_lat", lat)
            putExtra("place_name", placeName)
        }
        (context as Activity).startActivity(intent)
        (context as Activity).finish()
    }
}