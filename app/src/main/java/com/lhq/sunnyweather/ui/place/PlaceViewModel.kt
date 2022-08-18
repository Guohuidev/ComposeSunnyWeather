package com.lhq.sunnyweather.ui.place

import android.widget.Toast
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.*
import com.lhq.sunnyweather.SunnyWeatherApplication
import com.lhq.sunnyweather.logic.Repository
import com.lhq.sunnyweather.logic.model.Place
import kotlinx.coroutines.launch


class PlaceViewModel : ViewModel() {
    val placeList = mutableStateListOf<Place>()

    fun searchPlaces(query: String) {
        viewModelScope.launch {
            val places = Repository.searchPlaces(query)
            if (places != null) {
                placeList.clear()
                placeList.addAll(places)
                println("place = ${placeList.toString()}")
            } else {
                Toast.makeText(SunnyWeatherApplication.context, "error!", Toast.LENGTH_SHORT).show()
            }
        }
    }
}