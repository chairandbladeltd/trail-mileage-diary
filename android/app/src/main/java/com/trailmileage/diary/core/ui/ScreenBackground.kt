package com.trailmileage.diary.core.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.trailmileage.diary.R

@Composable
fun ScreenBackground(
    gradient: List<Color>,
    modifier: Modifier = Modifier,
    textureAlpha: Float = 0.12f,
    content: @Composable BoxScope.() -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(gradient))
    ) {
        Image(
            painter = painterResource(R.drawable.bg_game),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            alpha = textureAlpha,
            modifier = Modifier.fillMaxSize()
        )
        content()
    }
}
