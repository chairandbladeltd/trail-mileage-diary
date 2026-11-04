package com.trailmileage.diary.presentation.library

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.trailmileage.diary.R
import com.trailmileage.diary.core.di.ServiceLocator
import com.trailmileage.diary.core.theme.Ink
import com.trailmileage.diary.core.theme.InkMuted
import com.trailmileage.diary.core.theme.Olive
import com.trailmileage.diary.core.theme.Parchment
import com.trailmileage.diary.core.theme.ParchmentDeep
import com.trailmileage.diary.core.theme.ParchmentLight
import com.trailmileage.diary.core.theme.RuleLine
import com.trailmileage.diary.core.theme.TrailOrange
import com.trailmileage.diary.core.ui.EmptyState
import com.trailmileage.diary.core.ui.ScreenBackground
import com.trailmileage.diary.core.ui.ScreenHeader
import com.trailmileage.diary.core.ui.distanceUnit

@Composable
fun TrailLibraryRoute(
    onBack: () -> Unit,
    onPickTrail: (String) -> Unit
) {
    val libraryViewModel: TrailLibraryViewModel = viewModel(factory = ServiceLocator.viewModelFactory)
    val state by libraryViewModel.state.collectAsStateWithLifecycle()
    TrailLibraryScreen(state = state, onBack = onBack, onPickTrail = onPickTrail)
}

@Composable
private fun TrailLibraryScreen(
    state: TrailLibraryUiState,
    onBack: () -> Unit,
    onPickTrail: (String) -> Unit
) {
    ScreenBackground(
        gradient = listOf(Color(0xFFF7F3EA), Parchment, ParchmentDeep),
        textureAlpha = 0.12f
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding()
        ) {
            ScreenHeader(
                title = stringResource(R.string.library_title),
                backLabel = stringResource(R.string.cd_back),
                onBack = onBack
            )
            if (!state.isLoading && !state.isEmpty) {
                Text(
                    text = stringResource(R.string.library_hint),
                    style = MaterialTheme.typography.labelSmall,
                    color = Olive.copy(alpha = 0.8f),
                    modifier = Modifier.padding(start = 20.dp, end = 20.dp, top = 14.dp)
                )
            }
            Box(modifier = Modifier.weight(1f)) {
                when {
                    state.isLoading -> CircularProgressIndicator(
                        color = Olive,
                        modifier = Modifier.align(Alignment.Center)
                    )

                    state.isEmpty -> EmptyState(
                        imageRes = R.drawable.sprite_5,
                        title = stringResource(R.string.library_empty_title),
                        subtitle = stringResource(R.string.library_empty_subtitle),
                        modifier = Modifier.align(Alignment.TopCenter)
                    )

                    else -> LazyColumn(
                        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 14.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(state.rows, key = { row -> row.id }) { row ->
                            TrailRow(
                                name = row.name,
                                summary = stringResource(
                                    R.string.fmt_trail_summary,
                                    row.visitCount,
                                    row.totalValue,
                                    distanceUnit(state.imperial)
                                ),
                                badge = row.visitCount.toString(),
                                onClick = { onPickTrail(row.name) }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun TrailRow(
    name: String,
    summary: String,
    badge: String,
    onClick: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(14.dp),
        color = ParchmentLight,
        border = BorderStroke(1.dp, RuleLine),
        modifier = Modifier
            .fillMaxWidth()
            .height(72.dp)
            .clickable(onClick = onClick)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = name,
                    color = Ink,
                    fontFamily = FontFamily.Serif,
                    fontWeight = FontWeight.Bold,
                    fontSize = 17.sp,
                    maxLines = 1
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text = summary,
                    style = MaterialTheme.typography.labelSmall,
                    color = InkMuted,
                    fontSize = 11.sp,
                    maxLines = 1
                )
            }
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(44.dp)
                    .background(TrailOrange, CircleShape)
            ) {
                Text(
                    text = badge,
                    color = Parchment,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            }
        }
    }
}
