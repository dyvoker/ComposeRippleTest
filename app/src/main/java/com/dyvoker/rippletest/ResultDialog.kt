package com.dyvoker.rippletest

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties

@Composable
internal fun ResultDialog(
    result: MutableState<ResultUiModel?>,
) {
    val data = result.value ?: return

    Dialog(
        onDismissRequest = {
            result.value = null
        },
        properties = DialogProperties(),
    ) {
        Column(
            modifier = Modifier
                .clip(RoundedCornerShape(16.dp))
                .background(Color.White)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Text(
                text = "Frame count: ${data.frameCount}",
            )
            Text(
                text = "Frame time summary: ${data.frameTimeMsSummary}",
            )
            Text(
                text = "Average frame time: ${data.averageFrameTimeMs}",
            )
            Text(
                text = "Average FPS: ${data.averageFps}",
            )
            Text(
                text = "Skipped frames percent: ${data.skippedFramesPercent}",
            )
            Button(
                modifier = Modifier.align(Alignment.End),
                onClick = {
                    result.value = null
                }
            ) {
                Text(
                    text = "Close"
                )
            }
        }
    }
}