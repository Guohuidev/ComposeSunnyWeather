package com.lhq.sunnyweather.logic

import androidx.lifecycle.LiveData
import com.lhq.sunnyweather.logic.model.Place
import com.lhq.sunnyweather.logic.model.PlaceResponse
import com.lhq.sunnyweather.logic.network.ServiceCreator
import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext

import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import java.lang.RuntimeException


object Repository {

    suspend fun searchPlaces(query: String): Result<List<Place>> {
        return try {
            val placeResponse: PlaceResponse = ServiceCreator.placeService.searchPlaces(query)
            if (placeResponse.status == "ok") {
                Result.success(placeResponse.places)
            } else {
                Result.failure(RuntimeException("未能查询到地点！"))
            }
        } catch (e: Exception) {
            Result.failure(e)
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