package com.dyvoker.rippletest

import android.os.Bundle
import android.util.Log
import android.view.Choreographer
import android.view.Choreographer.FrameCallback
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.dyvoker.rippletest.ui.theme.RippleTestTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


private const val itemsCount = 100

class MainActivity : ComponentActivity() {

    private val testResult = mutableStateOf<ResultUiModel?>(null)
    private var frameTimeNanosSummary = 0L
    private var frameCount = 0
    private var lastFrameTime = 0L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Start monitoring frame rate
        Choreographer.getInstance().postFrameCallback(frameCallback)

        setContent {
            RippleTestTheme {
                Column(
                    modifier = Modifier.fillMaxSize(),
                ) {
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
                                Item()
                            }
                        }
                    }
                    // Button to reproduce identical test for all scrolls.
                    TextButton(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = {
                            scope.launch {
                                runAutoTestAllModes(
                                    lazyState = lazyState,
                                    resetFrames = ::resetFrames,
                                    showResults = ::showResults,
                                )
                            }
                        }
                    ) {
                        Text(text = "Run full test")
                    }
                }

                ResultDialog(
                    result = testResult,
                )
            }
        }
    }

    private fun resetFrames() {
        frameCount = 0
        frameTimeNanosSummary = 0L
        lastFrameTime = System.nanoTime()
    }

    private fun showResults() {
        val frameTimeMsSummary = frameTimeNanosSummary / 1_000_000F
        val averageFrameTimeMs = frameTimeMsSummary / frameCount.toFloat()
        val averageFps = frameCount.toFloat() / (frameTimeMsSummary / 1_000F)

        val mustBeFrames = frameTimeMsSummary / 16.6F // 16.6 ms is 60 fps.
        val skippedFrames = mustBeFrames - frameCount
        val skippedFramesPercent = skippedFrames / mustBeFrames * 100F

        testResult.value = ResultUiModel(
            frameCount = frameCount,
            frameTimeMsSummary = frameTimeMsSummary,
            averageFrameTimeMs = averageFrameTimeMs,
            averageFps = averageFps,
            skippedFramesPercent = skippedFramesPercent,
        )

        Log.d("TestResult", "Frame count: $frameCount")
        Log.d("TestResult", "Frame time summary (ms): $frameTimeMsSummary")
        Log.d("TestResult", "Average frame time: ${String.format("%.2f", averageFrameTimeMs)} ms")
        Log.d("TestResult", "Average FPS: ${String.format("%.2f", averageFps)}")
        Log.d("TestResult", "Skipped Frames (%): ${String.format("%.0f", skippedFramesPercent)}")
    }

    private val frameCallback = object : FrameCallback {
        override fun doFrame(frameTimeNanos: Long) {
            val elapsedNanos = frameTimeNanos - lastFrameTime
            frameTimeNanosSummary += elapsedNanos
            frameCount++
            lastFrameTime = frameTimeNanos
            Choreographer.getInstance().postFrameCallback(this)
        }
    }
}

private suspend fun runAutoTestAllModes(
    lazyState: LazyListState,
    resetFrames: () -> Unit,
    showResults: () -> Unit,
) {
    // Reset.
    lazyState.scrollToItem(0)

    resetFrames()
    makeScroll(lazyState)
    showResults()
}

private suspend fun makeScroll(lazyState: LazyListState) {
    delay(200L)
    lazyState.animateScrollToItem(itemsCount)
    delay(200L)
    lazyState.animateScrollToItem(0)
    delay(400L)
}

/**
 * Some simple layout with clickable (seven).
 */
@Composable
private fun Item() {
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
                .clickable {},
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