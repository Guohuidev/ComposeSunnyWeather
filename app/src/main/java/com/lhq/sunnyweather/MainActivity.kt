package com.lhq.sunnyweather

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModelProvider
import com.lhq.sunnyweather.logic.model.Place
import com.lhq.sunnyweather.ui.place.PlaceViewModel
import com.lhq.sunnyweather.ui.place.ShowPlace
import com.lhq.sunnyweather.ui.theme.ComposeSunnyWeatherTheme
import com.lhq.sunnyweather.ui.weather.WeatherActivity

class MainActivity : ComponentActivity() {
    private val viewModel by lazy {
        ViewModelProvider(this).get(PlaceViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ShowPlace(viewModel, this::jump)
        }

        // 如果本地已经有位置信息，则直接跳转天气页面
        if (viewModel.isPlaceSaved()) {
            val place = viewModel.getSavedPlace()
            val intent = Intent(this, WeatherActivity::class.java).apply {
                putExtra("place", place)
            }
            startActivity(intent)
            finish()
        }
    }

    /**
     * 跳转到天气页面
     */
    private fun jump(place: Place) {
        val intent = Intent(this, WeatherActivity::class.java).apply {
            putExtra("place", place)
        }
        viewModel.savePlace(place)
        startActivity(intent)
        finish()
    }
}



@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    ComposeSunnyWeatherTheme {
        Greeting("Android")
    }
}