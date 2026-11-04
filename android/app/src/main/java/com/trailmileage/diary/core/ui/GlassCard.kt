package com.trailmileage.diary.core.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.trailmileage.diary.core.theme.ParchmentLight
import com.trailmileage.diary.core.theme.RuleLine

@Composable
fun GlassCard(
    modifier: Modifier = Modifier,
    container: Color = ParchmentLight.copy(alpha = 0.92f),
    border: Color = RuleLine,
    corner: Dp = 18.dp,
    innerPadding: Dp = 16.dp,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        shape = RoundedCornerShape(corner),
        border = BorderStroke(1.dp, border),
        colors = CardDefaults.cardColors(containerColor = container),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(innerPadding),
            content = content
        )
    }
}
