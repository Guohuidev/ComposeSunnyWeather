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
            ShowPlace(viewModel.placeList, viewModel::searchPlaces, this::jump)
        }

        if (viewModel.isPlaceSaved()) {
            val place = viewModel.getSavedPlace()
            val intent = Intent(this, WeatherActivity::class.java).apply {
                putExtra("location_lng", place.location.lng)
                putExtra("location_lat", place.location.lat)
                putExtra("place_name", place.name)
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
            putExtra("location_lng", place.location.lng)
            putExtra("location_lat", place.location.lat)
            putExtra("place_name", place.name)
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