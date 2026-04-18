package com.team3.sneakx.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.ErrorOutline
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.team3.sneakx.ui.theme.SneakSpacing

/** Shared [androidx.compose.material3.TopAppBar] colors for SneakX screens. */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun sneakTopAppBarColors() = TopAppBarDefaults.topAppBarColors(
    containerColor = Color.Transparent,
    titleContentColor = MaterialTheme.colorScheme.onSurface,
)

/** Default border and error colors for [androidx.compose.material3.OutlinedTextField] across SneakX. */
@Composable
fun sneakOutlinedTextFieldColors(): TextFieldColors = OutlinedTextFieldDefaults.colors(
    focusedBorderColor = MaterialTheme.colorScheme.primary,
    unfocusedBorderColor = MaterialTheme.colorScheme.outline,
    errorBorderColor = MaterialTheme.colorScheme.error,
    errorLabelColor = MaterialTheme.colorScheme.error,
)

/** Inline error from existing ViewModel/repository messages. */
@Composable
fun SneakErrorBanner(
    message: String,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.small,
        color = MaterialTheme.colorScheme.errorContainer,
    ) {
        Row(
            modifier = Modifier.padding(SneakSpacing.md),
            horizontalArrangement = Arrangement.spacedBy(SneakSpacing.sm),
            verticalAlignment = Alignment.Top,
        ) {
            Icon(
                Icons.Outlined.ErrorOutline,
                contentDescription = null,
                modifier = Modifier.size(20.dp),
                tint = MaterialTheme.colorScheme.onErrorContainer,
            )
            Text(
                text = message,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onErrorContainer,
            )
        }
    }
}

/** Success / confirmation copy from existing UI state. */
@Composable
fun SneakSuccessBanner(
    message: String,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.small,
        color = MaterialTheme.colorScheme.primaryContainer,
    ) {
        Row(
            modifier = Modifier.padding(SneakSpacing.md),
            horizontalArrangement = Arrangement.spacedBy(SneakSpacing.sm),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                Icons.Outlined.CheckCircle,
                contentDescription = null,
                modifier = Modifier.size(20.dp),
                tint = MaterialTheme.colorScheme.onPrimaryContainer,
            )
            Text(
                text = message,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
            )
        }
    }
}

/** Empty list / no data — titles and copy come from each screen. */
@Composable
fun SneakEmptyState(
    title: String,
    icon: ImageVector,
    modifier: Modifier = Modifier,
    body: String? = null,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = SneakSpacing.xxxl),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(48.dp),
            tint = MaterialTheme.colorScheme.outline,
        )
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = SneakSpacing.md),
        )
        body?.let {
            Text(
                text = it,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(
                    top = SneakSpacing.sm,
                    start = SneakSpacing.lg,
                    end = SneakSpacing.lg,
                ),
            )
        }
    }
}

@Composable
fun SneakScreenTitle(
    text: String,
    modifier: Modifier = Modifier,
) {
    Text(
        text = text,
        style = MaterialTheme.typography.headlineMedium,
        color = MaterialTheme.colorScheme.onBackground,
        modifier = modifier,
    )
}

@Composable
fun SneakSectionLabel(
    text: String,
    modifier: Modifier = Modifier,
) {
    Text(
        text = text.uppercase(),
        style = MaterialTheme.typography.labelMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        letterSpacing = 0.8.sp,
        modifier = modifier.fillMaxWidth(),
    )
}

/** Primary button row: optional spinner when [loading] (existing VM flag). */
@Composable
fun SneakPrimaryButtonContent(
    loading: Boolean,
    loadingText: String,
    idleText: String,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
    ) {
        if (loading) {
            CircularProgressIndicator(
                modifier = Modifier.size(18.dp),
                strokeWidth = 2.dp,
                color = MaterialTheme.colorScheme.onPrimary,
            )
            Spacer(Modifier.width(SneakSpacing.sm))
        }
        Text(if (loading) loadingText else idleText)
    }
}

/** App cold start / session restore — no new business state. */
@Composable
fun SneakStartupLoading(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(SneakSpacing.md),
    ) {
        CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
        Text(
            text = "Loading…",
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}
