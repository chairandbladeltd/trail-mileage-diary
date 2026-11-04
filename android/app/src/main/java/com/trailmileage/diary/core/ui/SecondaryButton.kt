package com.trailmileage.diary.core.ui

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.trailmileage.diary.core.config.GameConfig
import com.trailmileage.diary.core.theme.Olive

@Composable
fun SecondaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    leadingIcon: ImageVector? = null,
    borderColor: Color = Olive,
    contentColor: Color = Olive
) {
    val interactionSource = remember { MutableInteractionSource() }
    val pressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (pressed && enabled) 0.96f else 1f,
        animationSpec = spring(
            dampingRatio = GameConfig.PRESS_DAMPING,
            stiffness = GameConfig.PRESS_STIFFNESS
        ),
        label = "press"
    )
    OutlinedButton(
        onClick = onClick,
        enabled = enabled,
        interactionSource = interactionSource,
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.dp, if (enabled) borderColor else borderColor.copy(alpha = 0.38f)),
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = Color.Transparent,
            contentColor = contentColor,
            disabledContentColor = contentColor.copy(alpha = 0.38f)
        ),
        modifier = modifier
            .heightIn(min = 50.dp)
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            }
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            if (leadingIcon != null) {
                Icon(
                    imageVector = leadingIcon,
                    contentDescription = null,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(Modifier.width(8.dp))
            }
            Text(
                text = text,
                style = MaterialTheme.typography.labelSmall,
                maxLines = 1
            )
        }
    }
}
