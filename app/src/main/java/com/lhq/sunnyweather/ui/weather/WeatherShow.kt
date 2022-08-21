package com.lhq.sunnyweather.ui.weather

import android.content.Context
import android.view.inputmethod.InputMethodManager
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Scaffold
import androidx.compose.material.ScaffoldState
import androidx.compose.material.Text
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.lhq.sunnyweather.R
import com.lhq.sunnyweather.logic.model.DailyResponse
import com.lhq.sunnyweather.logic.model.RealtimeResponse
import com.lhq.sunnyweather.logic.model.getSky
import com.lhq.sunnyweather.ui.place.PlaceViewModel
import com.lhq.sunnyweather.ui.place.ShowPlace
import com.lhq.sunnyweather.ui.theme.Gray1
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*



@ExperimentalComposeUiApi
@Composable
fun WeatherUi(weatherViewModel: WeatherViewModel, placeViewModel: PlaceViewModel) {
    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()
    val keyboardController = LocalSoftwareKeyboardController.current

    Scaffold(
        drawerContent = {
            ShowPlace(placeViewModel) {
                weatherViewModel.place = it
                weatherViewModel.refreshWeather()
                keyboardController?.hide()
                scope.launch {
                    scaffoldState.drawerState.close()
                }
            }
        },
        content = {
            WeatherMain(weatherViewModel, scaffoldState, scope)
        },
        drawerGesturesEnabled = scaffoldState.drawerState.isOpen,
        scaffoldState = scaffoldState,
    )
}


/**
 * 天气页面，包含当前天气、预告天气、生活指数三个部分
 */
@Composable
fun WeatherMain(weatherViewModel: WeatherViewModel, scaffoldState: ScaffoldState, scope: CoroutineScope) {
    val realtime = weatherViewModel.mutableWeather.realtime
    val daily = weatherViewModel.mutableWeather.daily
    val placeName = weatherViewModel.place.name
    val isRefreshing = weatherViewModel.isRefreshing

    if (daily != null && realtime != null && placeName.isNotEmpty()) {
        SwipeRefresh(state = rememberSwipeRefreshState(isRefreshing),
            onRefresh = { weatherViewModel.refreshWeather() }) {
            LazyColumn(Modifier.fillMaxWidth().background(Gray1), content = {
                item {
                    WeatherRealtime(realtime, placeName, scaffoldState, scope)
                }
                item {
                    WeatherForecast(daily)
                }
                item {
                    LifeIndex(daily.lifeIndex)
                }
            })
        }
    }
}

/**
 * 当前天气
 */
@Composable
fun WeatherRealtime(
    weatherRealtime: RealtimeResponse.Realtime,
    placeName: String,
    scaffoldState: ScaffoldState,
    scope: CoroutineScope
) {
    val sky = getSky(weatherRealtime.skycon)

    Box(Modifier.height(550.dp).fillMaxWidth()) {
        // 背景图
        Image(painter = painterResource(sky.bg), contentDescription = null, modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.FillBounds)
        Box(Modifier.padding(top = 35.dp).fillMaxWidth()) {
            // 切换城市icon
            Image(painter = painterResource(R.drawable.ic_home), contentDescription = null,
                modifier = Modifier.padding(start = 15.dp).size(30.dp).clickable {
                    scope.launch {
                        scaffoldState.drawerState.open()
                    }
                })
            // 位置文本
            Text(text = placeName, color = Color.White, fontSize = 22.sp, modifier = Modifier.align(Alignment.Center))
        }
        Column(Modifier.fillMaxWidth().align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally) {
            // 气温
            Text(text = "${weatherRealtime.temperature.toInt()}℃", color = Color.White, fontSize = 70.sp)
            Row(Modifier.padding(top = 12.dp).wrapContentSize()) {
                // 天气情况
                Text(text = sky.info, color = Color.White, fontSize = 18.sp)
                // 分隔符
                Text(text = "|", color = Color.White, fontSize = 18.sp, modifier = Modifier.padding(horizontal = 10.dp))
                // 空气指数
                Text(text = "空气指数 ${weatherRealtime.airQuality.aqi.chn.toInt()}", color = Color.White, fontSize = 18.sp)
            }
        }
    }
}

/**
 * 预报天气
 */
@Composable
fun WeatherForecast(daily: DailyResponse.Daily) {
    Column(Modifier.padding(12.dp).fillMaxWidth().clip(RoundedCornerShape(8.dp)).background(Color.White)) {
        Text(text = "预报", color = Color.Black, fontSize = 20.sp,
            modifier = Modifier.padding(start = 15.dp, top = 20.dp, bottom = 15.dp))
        Column(Modifier.fillMaxWidth()) {
            for (i in daily.skycon.indices) {
                val skycon = daily.skycon[i]
                val temperature = daily.temperature[i]

                Row(Modifier.padding(vertical = 15.dp, horizontal = 15.dp).fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically) {
                    val sky = getSky(skycon.value)
                    // 日期
                    val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                    Text(text = simpleDateFormat.format(skycon.date), fontSize = 15.sp, color = Color.Gray,
                        modifier = Modifier.weight(4f))
                    // 天气icon
                    Image(painter = painterResource(sky.icon), contentDescription = null,
                        modifier = Modifier.padding(end = 20.dp).size(20.dp))
                    // 天气情况
                    Text(text = sky.info, textAlign = TextAlign.Center, fontSize = 15.sp, color = Color.Gray,
                        modifier = Modifier.weight(3f))
                    // 气温范围
                    val temperatureText = "${temperature.min.toInt()} ~ ${temperature.max.toInt()} ℃"
                    Text(text = temperatureText,  textAlign = TextAlign.End, fontSize = 15.sp, color = Color.Gray,
                        modifier = Modifier.weight(3f))
                }
            }
        }
    }
}

/**
 * 生活指数
 */
@Composable
fun LifeIndex(lifeIndex: DailyResponse.LifeIndex) {
    Column(Modifier.padding(start = 12.dp, end = 12.dp, bottom = 12.dp).fillMaxWidth()
        .clip(RoundedCornerShape(8.dp)).background(Color.White)) {
        Text(text = "生活指数", color = Color.Black, fontSize = 20.sp,
            modifier = Modifier.padding(start = 15.dp, top = 20.dp, bottom = 10.dp))
        Column(Modifier.fillMaxWidth()) {
            Row(Modifier.padding(vertical = 15.dp).fillMaxWidth()) {
                LifeIndexItem(Modifier.weight(1f), "感冒", lifeIndex.coldRisk[0].desc, R.drawable.ic_coldrisk)
                LifeIndexItem(Modifier.weight(1f), "穿衣", lifeIndex.dressing[0].desc, R.drawable.ic_dressing)
            }
            Row(Modifier.padding(vertical = 15.dp).fillMaxWidth()) {
                LifeIndexItem(Modifier.weight(1f), "实时紫外线", lifeIndex.ultraviolet[0].desc, R.drawable.ic_ultraviolet)
                LifeIndexItem(Modifier.weight(1f), "洗车", lifeIndex.carWashing[0].desc, R.drawable.ic_carwashing)
            }
        }
    }
}


@Composable
fun LifeIndexItem(modifier: Modifier, title: String, desc: String, imgSrc: Int) {
    Row(modifier.padding(start = 40.dp), verticalAlignment = Alignment.CenterVertically) {
        Image(painter = painterResource(imgSrc), contentDescription = null,
            modifier = Modifier.size(30.dp))
        Column(Modifier.padding(start = 20.dp)) {
            Text(text = title, fontSize = 15.sp, color = Color.Gray, modifier = Modifier.padding(vertical = 6.dp))
            Text(text = desc, fontSize = 17.sp, color = Color.Black)
        }
    }
}
