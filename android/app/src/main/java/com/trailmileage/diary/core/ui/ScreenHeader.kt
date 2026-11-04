package com.trailmileage.diary.core.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.ui.unit.dp
import com.trailmileage.diary.core.theme.Ink
import com.trailmileage.diary.core.theme.Olive
import com.trailmileage.diary.core.theme.ParchmentLight
import com.trailmileage.diary.core.theme.RuleLine

@Composable
fun ScreenHeader(
    title: String,
    backLabel: String,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    container: Color = ParchmentLight.copy(alpha = 0.92f),
    titleColor: Color = Ink,
    ruleColor: Color = RuleLine,
    tint: Color = Olive,
    trailing: @Composable (() -> Unit)? = null
) {
    Column(modifier = modifier.fillMaxWidth().background(container)) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .height(72.dp)
                .padding(horizontal = 8.dp)
        ) {
            BackButton(onClick = onBack, label = backLabel, tint = tint)
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.headlineSmall,
                    color = titleColor,
                    maxLines = 1
                )
            }
            Row(
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.size(width = 56.dp, height = 48.dp)
            ) {
                if (trailing != null) {
                    trailing()
                }
            }
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(ruleColor)
        )
    }
}
