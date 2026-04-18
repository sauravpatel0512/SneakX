package com.team3.sneakx.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.team3.sneakx.ui.theme.SneakSpacing

@Composable
fun SneakFilterChip(
    label: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val shape = RoundedCornerShape(18.dp)
    Surface(
        onClick = onClick,
        modifier = modifier,
        shape = shape,
        color = if (selected) {
            MaterialTheme.colorScheme.primary
        } else {
            MaterialTheme.colorScheme.surface
        },
        border = if (selected) null else BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelLarge,
            color = if (selected) {
                MaterialTheme.colorScheme.onPrimary
            } else {
                MaterialTheme.colorScheme.onSurface
            },
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
        )
    }
}

@Composable
fun SneakFilterChipRow(
    labels: List<String>,
    selectedIndex: Int,
    onSelect: (index: Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(SneakSpacing.sm),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        labels.forEachIndexed { index, label ->
            SneakFilterChip(
                label = label,
                selected = index == selectedIndex,
                onClick = { onSelect(index) },
            )
        }
    }
}

@Composable
fun SneakConditionToggle(
    selected: String,
    options: List<String>,
    onSelect: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(SneakSpacing.sm),
    ) {
        options.forEach { opt ->
            val sel = opt.equals(selected, ignoreCase = true)
            Surface(
                onClick = { onSelect(opt) },
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(18.dp),
                color = if (sel) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface,
                border = if (sel) null else BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp),
                ) {
                    Text(
                        text = opt,
                        style = MaterialTheme.typography.labelLarge,
                        color = if (sel) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface,
                        textAlign = TextAlign.Center,
                    )
                }
            }
        }
    }
}

@Composable
fun SneakTagPill(
    text: String,
    modifier: Modifier = Modifier,
    useSecondary: Boolean = true,
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(11.dp),
        color = if (useSecondary) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.surface,
    ) {
        Text(
            text = text.uppercase(),
            style = MaterialTheme.typography.labelSmall,
            color = if (useSecondary) MaterialTheme.colorScheme.onSecondary else MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
        )
    }
}
