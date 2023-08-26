package com.dyvoker.rippletest

import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource

@Composable
internal fun KotlinIcon(
    modifier: Modifier,
) {
    Icon(
        modifier = modifier,
        painter = painterResource(R.drawable.ic_kotlin),
        tint = Color.Unspecified,
        contentDescription = "The best language in the world",
    )
}