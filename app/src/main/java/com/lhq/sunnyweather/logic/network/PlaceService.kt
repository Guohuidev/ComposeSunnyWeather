package com.lhq.sunnyweather.logic.network

import com.lhq.sunnyweather.SunnyWeatherApplication
import com.lhq.sunnyweather.logic.model.PlaceResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface PlaceService {

    /**
     * 查询地址
     */
    @GET("v2/place?token=${SunnyWeatherApplication.TOKEN}&lang=zh_CN")
    suspend fun searchPlaces(@Query("query") query: String): PlaceResponse

}