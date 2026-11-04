package com.trailmileage.diary.core.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.trailmileage.diary.R
import com.trailmileage.diary.core.theme.InkMuted
import com.trailmileage.diary.core.theme.ParchmentLight
import com.trailmileage.diary.core.theme.RuleLine
import com.trailmileage.diary.core.theme.TrailOrange
import com.trailmileage.diary.domain.model.Weather

fun weatherIconRes(weather: Weather): Int = when (weather) {
    Weather.SUN -> R.drawable.ic_weather_sun
    Weather.CLOUD -> R.drawable.ic_weather_cloud
    Weather.RAIN -> R.drawable.ic_weather_rain
    Weather.WIND -> R.drawable.ic_weather_wind
    Weather.SNOW -> R.drawable.ic_weather_snow
}

@Composable
fun WeatherChip(
    label: String,
    iconRes: Int,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    stateLabel: String = ""
) {
    Surface(
        shape = RoundedCornerShape(14.dp),
        color = if (selected) TrailOrange.copy(alpha = 0.12f) else ParchmentLight,
        border = BorderStroke(if (selected) 2.dp else 1.dp, if (selected) TrailOrange else RuleLine),
        modifier = modifier
            .widthIn(min = 56.dp)
            .height(64.dp)
            .clickable(onClick = onClick)
            .semantics { stateDescription = stateLabel }
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.height(64.dp)
        ) {
            Spacer(Modifier.height(10.dp))
            Icon(
                imageVector = ImageVector.vectorResource(iconRes),
                contentDescription = null,
                tint = if (selected) TrailOrange else InkMuted,
                modifier = Modifier.size(24.dp)
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                fontSize = 10.sp,
                color = if (selected) TrailOrange else InkMuted,
                maxLines = 1
            )
        }
    }
}
