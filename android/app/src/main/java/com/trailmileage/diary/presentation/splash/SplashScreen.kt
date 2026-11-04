package com.trailmileage.diary.presentation.splash

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.scaleIn
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.trailmileage.diary.R
import com.trailmileage.diary.core.di.ServiceLocator
import com.trailmileage.diary.core.theme.Dusk
import com.trailmileage.diary.core.theme.OliveDeep
import com.trailmileage.diary.core.theme.OliveLight
import com.trailmileage.diary.core.theme.Parchment
import com.trailmileage.diary.core.theme.RuleLine
import com.trailmileage.diary.core.theme.TrailOrange
import com.trailmileage.diary.core.ui.ScreenBackground

@Composable
fun SplashRoute(onFinished: () -> Unit) {
    val splashViewModel: SplashViewModel = viewModel(factory = ServiceLocator.viewModelFactory)
    val finished by splashViewModel.finished.collectAsStateWithLifecycle()
    LaunchedEffect(finished) {
        if (finished) onFinished()
    }
    SplashScreen()
}

@Composable
private fun SplashScreen() {
    var revealed by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) { revealed = true }

    val loop = rememberInfiniteTransition(label = "splash")
    val ring by loop.animateFloat(
        initialValue = 0.96f,
        targetValue = 1.06f,
        animationSpec = infiniteRepeatable(
            animation = tween(2600, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "ring"
    )
    val drift by loop.animateFloat(
        initialValue = 0f,
        targetValue = 24f,
        animationSpec = infiniteRepeatable(
            animation = tween(5200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "drift"
    )

    ScreenBackground(
        gradient = listOf(Dusk, OliveDeep, Color(0xFF26241C)),
        textureAlpha = 0.14f
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(2.dp)
                .align(Alignment.TopCenter)
                .offset(x = drift.dp, y = 150.dp)
                .background(RuleLine.copy(alpha = 0.12f))
        )
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding()
                .padding(horizontal = 28.dp)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Box(
                    modifier = Modifier
                        .size(180.dp)
                        .clip(CircleShape)
                        .background(OliveDeep.copy(alpha = 0.55f))
                )
                Canvas(modifier = Modifier.size(180.dp)) {
                    val base = size.minDimension / 2f
                    drawCircle(
                        color = OliveLight.copy(alpha = 0.45f),
                        radius = base * ring,
                        style = Stroke(width = 1.5.dp.toPx())
                    )
                    drawCircle(
                        color = TrailOrange.copy(alpha = 0.30f),
                        radius = base * ring * 0.82f,
                        style = Stroke(width = 1.dp.toPx())
                    )
                }
                LogoReveal(revealed = revealed)
            }
            Spacer(Modifier.height(34.dp))
            Text(
                text = stringResource(R.string.splash_title),
                color = Parchment,
                fontFamily = FontFamily.Serif,
                fontWeight = FontWeight.Black,
                fontSize = 34.sp,
                lineHeight = 38.sp,
                letterSpacing = 1.4.sp,
                textAlign = TextAlign.Center
            )
            Spacer(Modifier.height(14.dp))
            Box(
                modifier = Modifier
                    .width(56.dp)
                    .height(1.dp)
                    .background(OliveLight)
            )
            Spacer(Modifier.height(14.dp))
            Text(
                text = stringResource(R.string.splash_subtitle),
                color = RuleLine.copy(alpha = 0.78f),
                style = MaterialTheme.typography.labelSmall,
                fontSize = 12.sp,
                letterSpacing = 3.0.sp,
                textAlign = TextAlign.Center
            )
            Spacer(Modifier.height(36.dp))
            LinearProgressIndicator(
                color = TrailOrange,
                trackColor = RuleLine.copy(alpha = 0.22f),
                modifier = Modifier
                    .width(168.dp)
                    .height(4.dp)
                    .clip(RoundedCornerShape(2.dp))
            )
            Spacer(Modifier.height(14.dp))
            Text(
                text = stringResource(R.string.splash_hint),
                color = RuleLine.copy(alpha = 0.6f),
                style = MaterialTheme.typography.labelSmall,
                fontSize = 11.sp,
                letterSpacing = 2.2.sp
            )
        }
    }
}

@Composable
private fun LogoReveal(revealed: Boolean) {
    AnimatedVisibility(
        visible = revealed,
        enter = scaleIn(
            initialScale = 0.82f,
            animationSpec = tween(520, easing = FastOutSlowInEasing)
        ) + fadeIn(tween(420))
    ) {
        Image(
            painter = painterResource(R.drawable.logo),
            contentDescription = stringResource(R.string.cd_logo),
            contentScale = ContentScale.Fit,
            modifier = Modifier.size(136.dp)
        )
    }
}
