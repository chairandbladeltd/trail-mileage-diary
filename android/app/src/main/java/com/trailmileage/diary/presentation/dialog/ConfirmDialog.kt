package com.trailmileage.diary.presentation.dialog

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.trailmileage.diary.R
import com.trailmileage.diary.core.theme.ClayError
import com.trailmileage.diary.core.theme.Ink
import com.trailmileage.diary.core.theme.InkMuted
import com.trailmileage.diary.core.theme.Olive
import com.trailmileage.diary.core.theme.ParchmentLight

@Composable
fun DeleteHikeDialog(
    trailName: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    ConfirmDialog(
        title = stringResource(R.string.dialog_delete_title),
        message = stringResource(R.string.dialog_delete_message, trailName),
        confirmText = stringResource(R.string.cta_delete),
        confirmColor = ClayError,
        onConfirm = onConfirm,
        onDismiss = onDismiss
    )
}

@Composable
fun ClearDataDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    ConfirmDialog(
        title = stringResource(R.string.dialog_clear_title),
        message = stringResource(R.string.dialog_clear_message),
        confirmText = stringResource(R.string.cta_clear_data),
        confirmColor = ClayError,
        onConfirm = onConfirm,
        onDismiss = onDismiss
    )
}

@Composable
private fun ConfirmDialog(
    title: String,
    message: String,
    confirmText: String,
    confirmColor: androidx.compose.ui.graphics.Color,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = ParchmentLight,
        titleContentColor = Ink,
        textContentColor = InkMuted,
        title = { Text(text = title, style = MaterialTheme.typography.headlineSmall) },
        text = { Text(text = message, style = MaterialTheme.typography.bodyLarge) },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text(
                    text = confirmText,
                    style = MaterialTheme.typography.labelSmall,
                    color = confirmColor
                )
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(
                    text = stringResource(R.string.cta_cancel),
                    style = MaterialTheme.typography.labelSmall,
                    color = Olive
                )
            }
        }
    )
}
