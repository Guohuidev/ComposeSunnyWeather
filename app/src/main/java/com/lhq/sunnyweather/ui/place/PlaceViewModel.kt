package com.lhq.sunnyweather.ui.place

import android.widget.Toast
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lhq.sunnyweather.SunnyWeatherApplication
import com.lhq.sunnyweather.logic.Repository
import com.lhq.sunnyweather.logic.model.Place
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

    fun savePlace(place: Place) = Repository.savePlace(place)

    fun getSavedPlace() = Repository.getSavedPlace()

    fun isPlaceSaved() = Repository.isPlaceSaved()

}