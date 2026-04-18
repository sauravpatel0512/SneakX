package com.team3.sneakx.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.ShoppingBag
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.team3.sneakx.ui.theme.SneakSpacing
import com.team3.sneakx.util.ListingImage

@Composable
fun SneakTopBarHome(
    userFirstNameOrShort: String,
    cartItemCount: Int,
    onNotificationClick: () -> Unit,
    onCartClick: () -> Unit,
    modifier: Modifier = Modifier,
    avatarUrl: String? = null,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = SneakSpacing.sm),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(SneakSpacing.md),
        ) {
            if (!avatarUrl.isNullOrBlank()) {
                ListingImage(
                    photoUri = avatarUrl,
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape),
                )
            } else {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.surfaceVariant),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = userFirstNameOrShort.take(1).uppercase(),
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }
            Column {
                Text(
                    text = "Welcome back",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                Text(
                    text = userFirstNameOrShort,
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onBackground,
                )
            }
        }
        Row(
            horizontalArrangement = Arrangement.spacedBy(SneakSpacing.sm),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            SneakIconCircle(
                onClick = onNotificationClick,
                icon = Icons.Outlined.Notifications,
                contentDescription = "Notifications",
            )
            Box {
                SneakIconCircle(
                    onClick = onCartClick,
                    icon = Icons.Outlined.ShoppingBag,
                    contentDescription = "Cart",
                    usePrimaryContainer = true,
                )
                if (cartItemCount > 0) {
                    Surface(
                        shape = CircleShape,
                        color = MaterialTheme.colorScheme.secondary,
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(end = 0.dp, top = 0.dp),
                    ) {
                        Text(
                            text = cartItemCount.coerceAtMost(99).toString(),
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSecondary,
                            modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp),
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun SneakTopBarBack(
    eyebrow: String,
    title: String,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    trailing: @Composable (() -> Unit)? = null,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = SneakSpacing.sm),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(SneakSpacing.md),
        ) {
            SneakIconCircle(
                onClick = onBack,
                icon = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Back",
            )
            Column {
                Text(
                    text = eyebrow.uppercase(),
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    letterSpacing = 0.8.sp,
                )
                Text(
                    text = title,
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onBackground,
                )
            }
        }
        trailing?.invoke()
    }
}
