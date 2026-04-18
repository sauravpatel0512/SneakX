package com.team3.sneakx.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.team3.sneakx.ui.theme.SneakHeroShape
import com.team3.sneakx.ui.theme.SneakSpacing
import com.team3.sneakx.ui.theme.priceTextStyle

@Composable
fun SneakCartSummaryCard(
    subtotalFormatted: String,
    totalFormatted: String,
    modifier: Modifier = Modifier,
    onCheckout: () -> Unit,
) {
    androidx.compose.material3.Surface(
        modifier = modifier.fillMaxWidth(),
        shape = SneakHeroShape,
        color = MaterialTheme.colorScheme.primary,
    ) {
        Column(
            modifier = Modifier.padding(SneakSpacing.cardPadding),
            verticalArrangement = Arrangement.spacedBy(SneakSpacing.md),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text("Subtotal", style = MaterialTheme.typography.bodyMedium, color = Color.White.copy(alpha = 0.85f))
                Text(
                    subtotalFormatted,
                    style = priceTextStyle(MaterialTheme.typography.titleMedium.copy(color = Color.White)),
                )
            }
            HorizontalDivider(color = Color.White.copy(alpha = 0.2f))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text("Total", style = MaterialTheme.typography.titleLarge, color = Color.White)
                Text(
                    totalFormatted,
                    style = priceTextStyle(MaterialTheme.typography.headlineSmall.copy(color = Color.White)),
                )
            }
            SneakLimeCtaButton(
                onClick = onCheckout,
                modifier = Modifier.fillMaxWidth(),
                leadingText = "Checkout",
                trailingText = "$totalFormatted →",
            )
        }
    }
}
