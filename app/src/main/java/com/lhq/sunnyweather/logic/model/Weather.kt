package com.lhq.sunnyweather.logic.model

class Weather(
    val realtime: RealtimeResponse.Realtime? = null,
    val daily: DailyResponse.Daily? = null
)