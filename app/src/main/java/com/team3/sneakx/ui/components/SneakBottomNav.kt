package com.team3.sneakx.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.team3.sneakx.ui.theme.SneakNavActiveShape
import com.team3.sneakx.ui.theme.SneakNavShape
import com.team3.sneakx.ui.theme.SneakSpacing

data class SneakNavItem(
    val route: String,
    val label: String,
    val icon: ImageVector,
)

@Composable
fun SneakBottomNav(
    items: List<SneakNavItem>,
    currentRoute: String,
    onNavigate: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = SneakSpacing.screenPadding)
            .padding(bottom = 12.dp),
        shape = SneakNavShape,
        color = MaterialTheme.colorScheme.primary,
        shadowElevation = 6.dp,
        tonalElevation = 6.dp,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .padding(horizontal = SneakSpacing.sm, vertical = 6.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            items.forEach { item ->
                val selected = currentRoute == item.route
                if (selected) {
                    Surface(
                        onClick = { onNavigate(item.route) },
                        shape = SneakNavActiveShape,
                        color = MaterialTheme.colorScheme.secondary,
                        modifier = Modifier
                            .weight(1f)
                            .semantics {
                                this.selected = true
                                role = Role.Tab
                                contentDescription = item.label
                            },
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp, horizontal = 8.dp),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Icon(
                                imageVector = item.icon,
                                contentDescription = null,
                                modifier = Modifier.size(22.dp),
                                tint = MaterialTheme.colorScheme.onSecondary,
                            )
                            Text(
                                text = item.label,
                                style = MaterialTheme.typography.labelLarge,
                                color = MaterialTheme.colorScheme.onSecondary,
                                modifier = Modifier.padding(start = 6.dp),
                            )
                        }
                    }
                } else {
                    IconButton(
                        onClick = { onNavigate(item.route) },
                        modifier = Modifier
                            .weight(1f)
                            .semantics {
                                this.selected = false
                                role = Role.Tab
                                contentDescription = item.label
                            },
                    ) {
                        Icon(
                            imageVector = item.icon,
                            contentDescription = null,
                            modifier = Modifier
                                .size(24.dp)
                                .alpha(0.55f),
                            tint = MaterialTheme.colorScheme.onPrimary,
                        )
                    }
                }
            }
        }
    }
}
