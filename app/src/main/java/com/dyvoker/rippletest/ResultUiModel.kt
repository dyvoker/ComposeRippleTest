package com.dyvoker.rippletest

data class ResultUiModel(
    val frameCount: Int,
    val frameTimeMsSummary: Float,
    val averageFrameTimeMs: Float,
    val averageFps: Float,
    val skippedFramesPercent: Float,
)
