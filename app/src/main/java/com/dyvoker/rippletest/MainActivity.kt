package com.dyvoker.rippletest

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.dyvoker.rippletest.ui.theme.RippleTestTheme
import kotlinx.coroutines.launch

private const val itemsCount = 100

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RippleTestTheme {
                Column(
                    modifier = Modifier.fillMaxSize(),
                ) {
                    // Mode of showing clickable/ripple.
                    val rippleMode = remember {
                        mutableStateOf(RippleMode.DEFAULT_CLICKABLE)
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        RadioRow(
                            text = "No clickable",
                            isSelected = rippleMode.value == RippleMode.NO_CLICKABLE,
                            onClick = { rippleMode.value = RippleMode.NO_CLICKABLE },
                        )
                        RadioRow(
                            text = "Default clickable",
                            isSelected = rippleMode.value == RippleMode.DEFAULT_CLICKABLE,
                            onClick = { rippleMode.value = RippleMode.DEFAULT_CLICKABLE },
                        )
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        RadioRow(
                            text = "No indication",
                            isSelected = rippleMode.value == RippleMode.NO_INDICATION,
                            onClick = { rippleMode.value = RippleMode.NO_INDICATION },
                        )
                    }
                    // List with lags.
                    val lazyState = rememberLazyListState()
                    val scope = rememberCoroutineScope()
                    LazyColumn(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxSize(),
                        state = lazyState,
                    ) {
                        repeat(itemsCount) {
                            item {
                                Item(
                                    rippleMode = rippleMode,
                                )
                            }
                        }
                    }
                    // Button to reproduce identical scroll to bottom and up.
                    TextButton(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = {
                            scope.launch {
                                if (lazyState.firstVisibleItemIndex == 0) {
                                    lazyState.animateScrollToItem(itemsCount)
                                } else {
                                    lazyState.animateScrollToItem(0)
                                }
                            }
                        }
                    ) {
                        Text(text = "Scroll")
                    }
                }
            }
        }
    }
}

/**
 * Some simple layout with clickable (seven).
 */
@Composable
private fun Item(
    rippleMode: State<RippleMode>,
) {
    Column(
        modifier = Modifier
            .padding(4.dp)
            .clip(MaterialTheme.shapes.medium)
            .background(Color.LightGray)
            .fillMaxWidth()
            .clickableByMode(rippleMode.value),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickableByMode(rippleMode.value),
        ) {
            repeat(4) {
                Box(
                    modifier = Modifier
                        .padding(8.dp)
                        .height(48.dp)
                        .weight(0.25f)
                        .clip(MaterialTheme.shapes.small)
                        .background(Color.Gray)
                        .clickableByMode(rippleMode.value),
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
                .clickableByMode(rippleMode.value),
        )
    }
}

@Stable
private fun Modifier.clickableByMode(rippleMode: RippleMode) = composed {
    when (rippleMode) {
        RippleMode.NO_CLICKABLE -> {
            this
        }
        RippleMode.DEFAULT_CLICKABLE -> {
            clickable {}
        }
        RippleMode.NO_INDICATION -> {
            clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = NoIndication,
                onClick = {},
            )
        }
    }
}

@Composable
private fun RowScope.RadioRow(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .weight(0.5f)
            .clickable(onClick = onClick),
    ) {
        RadioButton(
            modifier = Modifier.padding(8.dp),
            selected = isSelected,
            onClick = null,
        )
        Text(
            modifier = Modifier
                .padding(8.dp)
                .align(Alignment.CenterVertically),
            text = text,
        )
    }
}

