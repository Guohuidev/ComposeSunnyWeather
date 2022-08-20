package com.lhq.sunnyweather.logic

import com.lhq.sunnyweather.logic.dao.PlaceDao
import com.lhq.sunnyweather.logic.model.Place
import com.lhq.sunnyweather.logic.model.Weather
import com.lhq.sunnyweather.logic.network.ServiceCreator
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope


object Repository {

    /**
     * 搜索地点
     */
    suspend fun searchPlaces(query: String): Result<List<Place>> {
        return try {
            val placeResponse = ServiceCreator.placeService.searchPlaces(query)
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
     * 刷新天气
     */
    suspend fun refreshWeather(lng: String, lat: String): Result<Weather> {
        return try {
            coroutineScope {
                val deferredRealtime = async {
                    ServiceCreator.weatherService.getRealtimeWeather(lng, lat)
                }
                val deferredDaily = async {
                    ServiceCreator.weatherService.getDailyWeather(lng, lat)
                }
                val realtimeResponse = deferredRealtime.await()
                val dailyResponse = deferredDaily.await()
                if (realtimeResponse.status=="ok" && dailyResponse.status=="ok") {
                    val weather = Weather(realtimeResponse.result.realtime, dailyResponse.result.daily)
                    Result.success(weather)
                } else {
                    Result.failure(RuntimeException("realtime response status is ${realtimeResponse.status}"
                            + "daily response status is ${dailyResponse.status}"))
                }
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

    fun savePlace(place: Place) = PlaceDao.savePlace(place)

    fun getSavedPlace() = PlaceDao.getSavedPlace()

    fun isPlaceSaved() = PlaceDao.isPlaceSaved()

}