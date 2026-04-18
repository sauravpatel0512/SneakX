package com.team3.sneakx.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.team3.sneakx.ui.theme.SneakSpacing
import com.team3.sneakx.util.ListingImage

/** Shared listing row card. Pass [subtitle] and/or [statusBadge] from existing listing fields only. */
@Composable
fun SneakListingCard(
    photoUri: String?,
    title: String,
    subtitle: String?,
    modifier: Modifier = Modifier,
    imageWeight: Float = 0.35f,
    statusBadge: String? = null,
    onClick: (() -> Unit)? = null,
    trailing: (@Composable RowScope.() -> Unit)? = null,
    footer: (@Composable ColumnScope.() -> Unit)? = null,
) {
    val cardMod = if (onClick != null) modifier.clickable(onClick = onClick) else modifier
    Card(
        modifier = cardMod.fillMaxWidth(),
        shape = MaterialTheme.shapes.extraLarge,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
        ),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
    ) {
        Column(Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(SneakSpacing.cardPadding),
                horizontalArrangement = Arrangement.spacedBy(SneakSpacing.md),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Box(
                    modifier = Modifier
                        .weight(imageWeight)
                        .height(88.dp)
                        .clip(MaterialTheme.shapes.medium),
                ) {
                    ListingImage(
                        photoUri = photoUri,
                        modifier = Modifier.fillMaxSize(),
                    )
                }
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(SneakSpacing.xs),
                ) {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onSurface,
                    )
                    statusBadge?.takeIf { it.isNotBlank() }?.let { s ->
                        Surface(
                            shape = MaterialTheme.shapes.small,
                            color = MaterialTheme.colorScheme.secondaryContainer,
                        ) {
                            Text(
                                text = s,
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSecondaryContainer,
                                modifier = Modifier.padding(
                                    horizontal = SneakSpacing.sm,
                                    vertical = SneakSpacing.xs,
                                ),
                            )
                        }
                    }
                    subtitle?.takeIf { it.isNotBlank() }?.let {
                        Text(
                            text = it,
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                }
                trailing?.invoke(this)
            }
            footer?.invoke(this)
        }
    }
}
