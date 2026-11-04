package com.trailmileage.diary.core.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.trailmileage.diary.core.theme.Parchment
import com.trailmileage.diary.core.theme.TrailOrange

@Composable
fun DistanceBadge(
    text: String,
    modifier: Modifier = Modifier,
    container: Color = TrailOrange,
    contentColor: Color = Parchment
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .height(36.dp)
            .background(container, RoundedCornerShape(18.dp))
            .padding(horizontal = 14.dp)
    ) {
        Text(
            text = text,
            color = contentColor,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            maxLines = 1
        )
    }
}
