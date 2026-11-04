package com.trailmileage.diary.presentation.dialog

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.trailmileage.diary.R
import com.trailmileage.diary.core.theme.Ink
import com.trailmileage.diary.core.theme.InkMuted
import com.trailmileage.diary.core.theme.Olive
import com.trailmileage.diary.core.theme.ParchmentLight

@Composable
fun ExportDialog(
    content: String,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    val clipLabel = stringResource(R.string.export_header)
    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = ParchmentLight,
        titleContentColor = Ink,
        textContentColor = InkMuted,
        title = {
            Text(
                text = stringResource(R.string.dialog_export_title),
                style = MaterialTheme.typography.headlineSmall
            )
        },
        text = {
            Text(
                text = content,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier
                    .heightIn(max = 280.dp)
                    .verticalScroll(rememberScrollState())
            )
        },
        confirmButton = {
            TextButton(
                onClick = {
                    val manager = context.getSystemService(Context.CLIPBOARD_SERVICE)
                        as? ClipboardManager
                    manager?.setPrimaryClip(ClipData.newPlainText(clipLabel, content))
                }
            ) {
                Text(
                    text = stringResource(R.string.cta_copy),
                    style = MaterialTheme.typography.labelSmall,
                    color = Olive
                )
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(
                    text = stringResource(R.string.cta_close),
                    style = MaterialTheme.typography.labelSmall,
                    color = InkMuted
                )
            }
        }
    )
}
