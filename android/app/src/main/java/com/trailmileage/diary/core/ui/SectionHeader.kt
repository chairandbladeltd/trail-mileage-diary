package com.trailmileage.diary.core.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.trailmileage.diary.core.theme.Ink
import com.trailmileage.diary.core.theme.TrailOrange

@Composable
fun SectionHeader(
    title: String,
    modifier: Modifier = Modifier,
    titleColor: Color = Ink,
    blazeColor: Color = TrailOrange
) {
    Column(modifier = modifier) {
        Text(
            text = title,
            style = MaterialTheme.typography.headlineSmall,
            color = titleColor
        )
        Spacer(Modifier.height(6.dp))
        Box(
            modifier = Modifier
                .width(56.dp)
                .height(2.dp)
                .background(blazeColor)
        )
    }
}
