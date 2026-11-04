package com.trailmileage.diary.core.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.trailmileage.diary.core.theme.InkMuted
import com.trailmileage.diary.core.theme.ParchmentLight
import com.trailmileage.diary.core.theme.RuleLine

@Composable
fun StatCard(
    label: String,
    value: String,
    accent: Color,
    modifier: Modifier = Modifier,
    container: Color = ParchmentLight,
    labelColor: Color = InkMuted,
    onClick: (() -> Unit)? = null
) {
    val base = modifier.heightIn(min = 76.dp)
    Card(
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.dp, RuleLine),
        colors = CardDefaults.cardColors(containerColor = container),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = if (onClick == null) base else base.clickable(onClick = onClick)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 12.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .background(accent, CircleShape)
            )
            Spacer(Modifier.height(8.dp))
            Text(
                text = value,
                color = accent,
                fontFamily = FontFamily.Serif,
                fontWeight = FontWeight.Black,
                fontSize = 22.sp,
                maxLines = 1
            )
            Spacer(Modifier.height(2.dp))
            Text(
                text = label,
                color = labelColor,
                style = MaterialTheme.typography.labelSmall,
                fontSize = 10.sp,
                maxLines = 2
            )
        }
    }
}
