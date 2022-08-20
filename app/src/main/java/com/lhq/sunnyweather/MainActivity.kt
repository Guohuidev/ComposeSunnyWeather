package com.lhq.sunnyweather

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModelProvider
import com.lhq.sunnyweather.ui.place.PlaceViewModel
import com.lhq.sunnyweather.ui.place.ShowPlace
import com.lhq.sunnyweather.ui.theme.ComposeSunnyWeatherTheme
import com.lhq.sunnyweather.ui.weather.WeatherUi

class MainActivity : ComponentActivity() {
    private val viewModel by lazy {
        ViewModelProvider(this).get(PlaceViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ShowPlace(viewModel.placeList, viewModel::searchPlaces, viewModel::jump)
        }
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