package com.trailmileage.diary.core.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.trailmileage.diary.R
import com.trailmileage.diary.core.theme.Ink
import com.trailmileage.diary.core.theme.InkMuted
import com.trailmileage.diary.core.theme.ParchmentDeep
import com.trailmileage.diary.core.theme.ParchmentLight
import com.trailmileage.diary.core.theme.RuleLine

@Composable
fun HikeCard(
    trailName: String,
    distanceText: String,
    metaText: String,
    difficulty: Int,
    photoSlots: List<Int>,
    photoLabel: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    seed: Int = 0
) {
    val photos = photoSlots.filter { it > 0 }.ifEmpty { listOf(2) }
    Card(
        shape = RoundedCornerShape(18.dp),
        border = BorderStroke(1.dp, RuleLine),
        colors = CardDefaults.cardColors(containerColor = ParchmentLight),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(92.dp)
                .background(ParchmentDeep)
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(14.dp),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .padding(start = 18.dp, end = 96.dp)
            ) {
                photos.take(2).forEach { slot ->
                    Image(
                        painter = painterResource(
                            when (slot) {
                                1 -> R.drawable.sprite_2
                                2 -> R.drawable.sprite_3
                                else -> R.drawable.sprite_4
                            }
                        ),
                        contentDescription = photoLabel,
                        contentScale = ContentScale.Fit,
                        modifier = Modifier.size(46.dp)
                    )
                }
            }
            TornPaperEdge(
                color = ParchmentLight,
                seed = seed,
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .fillMaxWidth()
                    .height(10.dp)
            )
            DistanceBadge(
                text = distanceText,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(end = 12.dp)
                    .offset(y = 14.dp)
            )
        }
        Column(modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 22.dp, bottom = 14.dp)) {
            Text(
                text = trailName,
                color = Ink,
                fontFamily = FontFamily.Serif,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                maxLines = 1
            )
            Spacer(Modifier.height(6.dp))
            Text(
                text = metaText,
                style = MaterialTheme.typography.labelSmall,
                color = InkMuted,
                maxLines = 1
            )
            Spacer(Modifier.height(10.dp))
            BootRating(value = difficulty, bootSize = 20.dp, spacing = 4.dp)
        }
    }
}
