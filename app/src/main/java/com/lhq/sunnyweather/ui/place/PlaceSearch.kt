package com.lhq.sunnyweather.ui.place

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.BaselineShift
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lhq.sunnyweather.R
import com.lhq.sunnyweather.logic.model.Place
import com.lhq.sunnyweather.ui.theme.Gray1

@Composable
fun ShowPlace(
    places: List<Place>,
    search: (query: String) -> Unit,
    jump: (place: Place) -> Unit
) {
    var textValue: String by remember {
        mutableStateOf("")
    }

    Box(Modifier.fillMaxSize().background(Gray1)) {
        if (places.isEmpty()) {
            Image(painter = painterResource(id = R.drawable.bg_place), contentDescription = null,
                modifier = Modifier.fillMaxWidth().wrapContentHeight().align(Alignment.BottomCenter))
        }
        Column(Modifier.fillMaxSize()) {
            Box(Modifier.fillMaxWidth().height(60.dp).background(MaterialTheme.colors.primary)) {
                Box(Modifier.padding(10.dp).fillMaxSize().clip(RoundedCornerShape(30.dp)).background(Color.White),
                    contentAlignment = Alignment.CenterStart) {
                    BasicTextField(
                        value = textValue,
                        modifier = Modifier.padding(start = 20.dp).fillMaxWidth(),
                        textStyle = TextStyle(fontSize = 18.sp, textAlign = TextAlign.Start),
                        onValueChange = {
                            textValue = it
                            search(it)
                        }
                    )
                }
            }

            if (places.isNotEmpty()) {
                LazyColumn(
                    modifier = Modifier.fillMaxSize().padding(vertical = 10.dp),
                    content = {
                        items(places) {
                            val context = LocalContext.current
                            Box(Modifier.fillMaxWidth().wrapContentHeight().padding(10.dp).clip(RoundedCornerShape(10.dp))
                                .background(Color.White).clickable { jump(it) }) {
                                Column(Modifier.fillMaxWidth().wrapContentHeight().padding(18.dp).align(Alignment.Center)) {
                                    Text(text = it.name, modifier = Modifier.wrapContentSize(), fontSize = 20.sp)
                                    Text(text = it.address, modifier = Modifier.wrapContentSize().padding(top = 10.dp), fontSize = 14.sp)
                                }
                            }
                        }
                    }
                )
            }
        }
    }

}


















