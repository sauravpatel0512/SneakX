package com.team3.sneakx.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.team3.sneakx.ui.theme.SneakSpacing

@Composable
fun SneakStickyCtaBar(
    primaryLabel: String,
    onPrimaryClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.primary,
        tonalElevation = 3.dp,
        shadowElevation = 0.dp,
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(SneakSpacing.screenPadding)
                .padding(vertical = SneakSpacing.sm),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            SneakPrimaryButton(
                onClick = onPrimaryClick,
                enabled = enabled,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(primaryLabel, style = MaterialTheme.typography.labelLarge)
            }
        }
    }
}
