package com.trailmileage.diary.presentation.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue

import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.trailmileage.diary.R
import com.trailmileage.diary.core.di.ServiceLocator
import com.trailmileage.diary.core.theme.ClayError
import com.trailmileage.diary.core.theme.Ink
import com.trailmileage.diary.core.theme.InkMuted
import com.trailmileage.diary.core.theme.Olive
import com.trailmileage.diary.core.theme.Parchment
import com.trailmileage.diary.core.theme.ParchmentDeep
import com.trailmileage.diary.core.ui.GlassCard
import com.trailmileage.diary.core.ui.ScreenBackground
import com.trailmileage.diary.core.ui.ScreenHeader
import com.trailmileage.diary.core.ui.SecondaryButton
import com.trailmileage.diary.core.ui.SegmentedRow
import com.trailmileage.diary.core.ui.dateLabel
import com.trailmileage.diary.core.ui.distanceUnit
import com.trailmileage.diary.core.ui.durationLabel
import com.trailmileage.diary.core.ui.elevationUnit
import com.trailmileage.diary.presentation.dialog.ClearDataDialog
import com.trailmileage.diary.presentation.dialog.ExportDialog

@Composable
fun SettingsRoute(
    onBack: () -> Unit,
    onDataCleared: () -> Unit
) {
    val settingsViewModel: SettingsViewModel = viewModel(factory = ServiceLocator.viewModelFactory)
    val state by settingsViewModel.state.collectAsStateWithLifecycle()
    LaunchedEffect(Unit) {
        settingsViewModel.cleared.collect { onDataCleared() }
    }
    SettingsScreen(
        state = state,
        onBack = onBack,
        onUnitsSelect = settingsViewModel::onUnitsSelect,
        onThemeSelect = settingsViewModel::onThemeSelect,
        onExportOpen = settingsViewModel::onExportOpen,
        onExportClose = settingsViewModel::onExportClose,
        onClearRequest = settingsViewModel::onClearRequest,
        onClearCancel = settingsViewModel::onClearCancel,
        onClearConfirm = settingsViewModel::onClearConfirm
    )
}

@Composable
private fun SettingsScreen(
    state: SettingsUiState,
    onBack: () -> Unit,
    onUnitsSelect: (Int) -> Unit,
    onThemeSelect: (Int) -> Unit,
    onExportOpen: () -> Unit,
    onExportClose: () -> Unit,
    onClearRequest: () -> Unit,
    onClearCancel: () -> Unit,
    onClearConfirm: () -> Unit
) {
    if (state.showExport) {
        ExportDialog(content = exportText(state), onDismiss = onExportClose)
    }
    if (state.showClear) {
        ClearDataDialog(onConfirm = onClearConfirm, onDismiss = onClearCancel)
    }
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
                title = stringResource(R.string.settings_title),
                backLabel = stringResource(R.string.cd_back),
                onBack = onBack
            )
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 20.dp, vertical = 16.dp)
            ) {
                SettingBlock(title = stringResource(R.string.settings_units)) {
                    SegmentedRow(
                        options = listOf(
                            stringResource(R.string.unit_km),
                            stringResource(R.string.unit_mi)
                        ),
                        selectedIndex = state.unitsIndex,
                        onSelect = onUnitsSelect,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                SettingBlock(title = stringResource(R.string.settings_theme)) {
                    SegmentedRow(
                        options = listOf(
                            stringResource(R.string.theme_parchment),
                            stringResource(R.string.theme_dusk)
                        ),
                        selectedIndex = state.themeIndex,
                        onSelect = onThemeSelect,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                SettingBlock(title = stringResource(R.string.settings_export)) {
                    Text(
                        text = stringResource(R.string.settings_export_hint, state.hikeCount),
                        style = MaterialTheme.typography.labelSmall,
                        color = InkMuted
                    )
                    Spacer(Modifier.height(10.dp))
                    SecondaryButton(
                        text = stringResource(R.string.cta_export),
                        onClick = onExportOpen,
                        enabled = state.hikeCount > 0,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                SettingBlock(title = stringResource(R.string.settings_clear)) {
                    Text(
                        text = stringResource(R.string.settings_clear_hint),
                        style = MaterialTheme.typography.labelSmall,
                        color = InkMuted
                    )
                    Spacer(Modifier.height(10.dp))
                    SecondaryButton(
                        text = stringResource(R.string.cta_clear_data),
                        onClick = onClearRequest,
                        borderColor = ClayError,
                        contentColor = ClayError,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
            Text(
                text = stringResource(R.string.settings_footer),
                style = MaterialTheme.typography.labelSmall,
                color = Olive.copy(alpha = 0.8f),
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 16.dp)
            )
        }
    }
}

@Composable
private fun SettingBlock(
    title: String,
    content: @Composable () -> Unit
) {
    GlassCard(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleSmall,
            color = Ink
        )
        Spacer(Modifier.height(10.dp))
        content()
    }
}

@Composable
private fun exportText(state: SettingsUiState): String {
    val header = stringResource(R.string.export_header)
    val distance = distanceUnit(state.imperial)
    val elevation = elevationUnit(state.imperial)
    val rows = state.exportRows.map { row ->
        stringResource(
            R.string.fmt_export_row,
            dateLabel(row.epochDay),
            row.trailName,
            row.distanceValue,
            distance,
            row.elevationValue,
            elevation,
            durationLabel(row.durationHours, row.durationMinutes)
        )
    }
    return (listOf(header) + rows).joinToString(System.lineSeparator())
}
