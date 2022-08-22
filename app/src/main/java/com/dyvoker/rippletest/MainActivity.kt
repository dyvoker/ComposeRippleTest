package com.dyvoker.rippletest

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.dyvoker.rippletest.ui.theme.RippleTestTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RippleTestTheme {
                Column(
                    modifier = Modifier.fillMaxSize(),
                ) {
                    val useClickable = remember { mutableStateOf(false) }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        Switch(
                            modifier = Modifier.padding(8.dp),
                            checked = useClickable.value,
                            onCheckedChange = { useClickable.value = !useClickable.value },
                        )
                        Text(
                            modifier = Modifier
                                .padding(8.dp)
                                .align(Alignment.CenterVertically),
                            text = "Use clickable"
                        )
                    }
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                    ) {
                        repeat(100) {
                            item {
                                if (useClickable.value) {
                                    ItemWithRipple()
                                } else {
                                    ItemWithoutRipple()
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ItemWithoutRipple() {
    Column(
        modifier = Modifier
            .padding(4.dp)
            .clip(MaterialTheme.shapes.medium)
            .background(Color.LightGray)
            .fillMaxWidth(),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp),
        ) {
            repeat(4) {
                Box(
                    modifier = Modifier
                        .padding(8.dp)
                        .height(48.dp)
                        .weight(0.25f)
                        .clip(MaterialTheme.shapes.small)
                        .background(Color.Gray),
                )
            }
        }
        Box(
            modifier = Modifier
                .padding(8.dp)
                .height(48.dp)
                .fillMaxWidth()
                .clip(MaterialTheme.shapes.medium)
                .background(Color.Gray),
        )
    }
}

@Composable
fun ItemWithRipple() {
    Column(
        modifier = Modifier
            .padding(4.dp)
            .clip(MaterialTheme.shapes.medium)
            .background(Color.LightGray)
            .fillMaxWidth()
            .clickable {},
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable {}
                .padding(4.dp),
        ) {
            repeat(4) {
                Box(
                    modifier = Modifier
                        .padding(8.dp)
                        .height(48.dp)
                        .weight(0.25f)
                        .clip(MaterialTheme.shapes.small)
                        .background(Color.Gray)
                        .clickable {},
                )
            }
        }
        Box(
            modifier = Modifier
                .padding(8.dp)
                .height(48.dp)
                .fillMaxWidth()
                .clip(MaterialTheme.shapes.medium)
                .background(Color.Gray)
                .clickable {},
        )
    }
}

