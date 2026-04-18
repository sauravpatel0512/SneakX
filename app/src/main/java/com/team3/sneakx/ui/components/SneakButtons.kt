package com.team3.sneakx.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.team3.sneakx.ui.theme.SneakButtonShape
import com.team3.sneakx.ui.theme.SneakSpacing

@Composable
fun SneakPrimaryButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    content: @Composable RowScope.() -> Unit,
) {
    Button(
        onClick = onClick,
        modifier = modifier.defaultMinSize(minHeight = 52.dp),
        enabled = enabled,
        shape = SneakButtonShape,
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary,
        ),
        contentPadding = PaddingValues(horizontal = SneakSpacing.xl, vertical = SneakSpacing.md),
        content = content,
    )
}

@Composable
fun SneakSecondaryButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    content: @Composable RowScope.() -> Unit,
) {
    OutlinedButton(
        onClick = onClick,
        modifier = modifier.defaultMinSize(minHeight = 52.dp),
        enabled = enabled,
        shape = SneakButtonShape,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface,
        ),
        contentPadding = PaddingValues(horizontal = SneakSpacing.xl, vertical = SneakSpacing.md),
        content = content,
    )
}

@Composable
fun SneakIconCircle(
    onClick: () -> Unit,
    icon: ImageVector,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    usePrimaryContainer: Boolean = false,
) {
    val bg = if (usePrimaryContainer) {
        MaterialTheme.colorScheme.primary
    } else {
        MaterialTheme.colorScheme.surfaceVariant
    }
    val fg = if (usePrimaryContainer) {
        MaterialTheme.colorScheme.onPrimary
    } else {
        MaterialTheme.colorScheme.onSurfaceVariant
    }
    Surface(
        onClick = onClick,
        modifier = modifier.size(40.dp),
        shape = CircleShape,
        color = bg,
    ) {
        Box(contentAlignment = Alignment.Center) {
            Icon(icon, contentDescription = contentDescription, tint = fg, modifier = Modifier.size(18.dp))
        }
    }
}

/** Lime CTA (e.g. checkout on dark summary card) */
@Composable
fun SneakLimeCtaButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    leadingText: String,
    trailingText: String? = null,
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .defaultMinSize(minHeight = 52.dp)
            .fillMaxWidth(),
        enabled = enabled,
        shape = SneakButtonShape,
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.secondary,
            contentColor = MaterialTheme.colorScheme.onSecondary,
        ),
        contentPadding = PaddingValues(horizontal = SneakSpacing.xl, vertical = SneakSpacing.md),
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text(leadingText, style = MaterialTheme.typography.labelLarge)
            trailingText?.let {
                Text(it, style = MaterialTheme.typography.labelLarge)
            }
        }
    }
}
