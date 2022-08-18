package com.lhq.sunnyweather.logic

import androidx.lifecycle.LiveData
import com.lhq.sunnyweather.logic.model.Place
import com.lhq.sunnyweather.logic.model.PlaceResponse
import com.lhq.sunnyweather.logic.network.ServiceCreator
import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext

import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope


object Repository {

    suspend fun searchPlaces(query: String): List<Place>? {
//        return fire(Dispatchers.Main) {
//            val placeResponse: PlaceResponse = ServiceCreator.placeService.searchPlaces(query)
//            if (placeResponse.status == "ok") {
//                val places = placeResponse.places
//                Result.success(places)
//            } else {
//                Result.failure(RuntimeException("response status is${placeResponse.status}"))
//            }
//        }
        val placeResponse: PlaceResponse = ServiceCreator.placeService.searchPlaces(query)
        return if (placeResponse.status == "ok") {
            placeResponse.places
        } else {
            null
        }
    }


    /**
     * 统一异常处理入口
     */
//    private fun <T> fire(context: CoroutineContext, block: suspend () -> Result<T>): LiveData<Result<T>> {
//        return liveData(context) {
//            val result: Result<T> = try {
//                block()
//            } catch (e: Exception) {
//                Result.failure<T>(e)
//            }
//            emit(result)
//        }
//    }

}