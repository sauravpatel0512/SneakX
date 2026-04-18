package com.team3.sneakx.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import com.team3.sneakx.ui.theme.SneakAccentSerifFamily
import com.team3.sneakx.ui.theme.SneakHeroShape
import com.team3.sneakx.ui.theme.SneakSpacing
import com.team3.sneakx.ui.theme.priceTextStyle
import com.team3.sneakx.util.ListingImage

@Composable
fun SneakHeroBanner(
    headline: String,
    accentLine: String,
    priceFormatted: String,
    imageUri: String?,
    onShopClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(SneakHeroShape)
            .background(
                brush = Brush.horizontalGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.primary,
                        MaterialTheme.colorScheme.primaryContainer,
                    ),
                ),
            )
            .padding(SneakSpacing.xl),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(SneakSpacing.sm),
            ) {
                SneakTagPill(text = "DROP OF THE WEEK", useSecondary = true)
                Text(
                    text = headline,
                    style = MaterialTheme.typography.headlineSmall,
                    color = Color.White,
                )
                if (accentLine.isNotBlank()) {
                    Text(
                        text = accentLine,
                        style = MaterialTheme.typography.headlineSmall.copy(
                            fontFamily = SneakAccentSerifFamily,
                            fontStyle = FontStyle.Italic,
                        ),
                        color = MaterialTheme.colorScheme.secondary,
                    )
                }
                Text(
                    text = priceFormatted,
                    style = priceTextStyle(MaterialTheme.typography.titleLarge.copy(color = Color.White)),
                )
                TextButton(onClick = onShopClick) {
                    Text(
                        "Shop ↗",
                        color = MaterialTheme.colorScheme.secondary,
                        style = MaterialTheme.typography.labelLarge,
                    )
                }
            }
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .rotate(-8f)
                    .clip(RoundedCornerShape(20.dp)),
            ) {
                ListingImage(
                    photoUri = imageUri,
                    modifier = Modifier
                        .fillMaxHeight()
                        .fillMaxWidth(),
                )
            }
        }
    }
}
