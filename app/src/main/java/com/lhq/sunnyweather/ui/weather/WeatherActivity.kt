package com.lhq.sunnyweather.ui.weather

import android.app.Activity
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModelProvider
import com.lhq.sunnyweather.logic.model.Place
import com.lhq.sunnyweather.ui.place.PlaceViewModel
import com.lhq.sunnyweather.ui.weather.ui.theme.ComposeSunnyWeatherTheme

class WeatherActivity : ComponentActivity() {

    private val weatherViewModel by lazy {
        ViewModelProvider(this).get(WeatherViewModel::class.java)
    }

    private val placeViewModel by lazy {
        ViewModelProvider(this).get(PlaceViewModel::class.java)
    }

    @ExperimentalComposeUiApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        transparentStatusBar()
        setContent {
            WeatherUi(weatherViewModel, placeViewModel)
        }

        // 从上一个activity拿到place对象
        val place = intent.getSerializableExtra("place") as Place
        weatherViewModel.place = place
        weatherViewModel.refreshWeather()
    }
}


/**
 * 设置透明状态栏
 */
fun Activity.transparentStatusBar() {
    transparentStatusBar(window)
}

private fun transparentStatusBar(window: Window) {
    window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
    window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
    val option = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
    val vis = window.decorView.systemUiVisibility
    window.decorView.systemUiVisibility = option or vis
    window.statusBarColor = android.graphics.Color.TRANSPARENT
}

