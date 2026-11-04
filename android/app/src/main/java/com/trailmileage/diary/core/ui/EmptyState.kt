package com.trailmileage.diary.core.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.trailmileage.diary.core.theme.Ink
import com.trailmileage.diary.core.theme.InkMuted

@Composable
fun EmptyState(
    imageRes: Int,
    title: String,
    subtitle: String,
    modifier: Modifier = Modifier,
    titleColor: Color = Ink,
    subtitleColor: Color = InkMuted
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 32.dp)
    ) {
        Image(
            painter = painterResource(imageRes),
            contentDescription = null,
            contentScale = ContentScale.Fit,
            alpha = 0.5f,
            modifier = Modifier.size(72.dp)
        )
        Spacer(Modifier.height(14.dp))
        Text(
            text = title,
            style = MaterialTheme.typography.headlineSmall,
            color = titleColor,
            textAlign = TextAlign.Center
        )
        Spacer(Modifier.height(6.dp))
        Text(
            text = subtitle,
            style = MaterialTheme.typography.labelSmall,
            color = subtitleColor,
            textAlign = TextAlign.Center
        )
    }
}
