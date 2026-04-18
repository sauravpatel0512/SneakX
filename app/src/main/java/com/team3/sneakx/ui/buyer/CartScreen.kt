package com.team3.sneakx.ui.buyer

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.team3.sneakx.LocalAppContainer
import com.team3.sneakx.ui.components.SneakCartSummaryCard
import com.team3.sneakx.ui.components.SneakEmptyState
import com.team3.sneakx.ui.components.SneakTopBarBack
import com.team3.sneakx.ui.theme.SneakSpacing
import com.team3.sneakx.ui.theme.priceTextStyle
import com.team3.sneakx.util.ListingImage
import com.team3.sneakx.util.photosFromJson
import kotlin.math.round

@Composable
fun CartScreen(
    navController: NavHostController,
    buyerId: String
) {
    val container = LocalAppContainer.current
    val vm: CartViewModel = viewModel(
        key = buyerId,
        factory = object : androidx.lifecycle.ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
                require(modelClass.isAssignableFrom(CartViewModel::class.java))
                return CartViewModel(buyerId, container.cartRepository) as T
            }
        }
    )
    val lines by vm.lines.collectAsState()
    val subtotal = round(lines.sumOf { it.lineTotal } * 100.0) / 100.0
    val subtotalStr = "$${String.format("%.2f", subtotal)}"

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = SneakSpacing.screenPadding)
            .padding(top = SneakSpacing.screenTop),
    ) {
        SneakTopBarBack(
            eyebrow = "Bag",
            title = "Your cart",
            onBack = { navController.popBackStack() },
            trailing = {
                Text(
                    "${lines.size} items",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            },
            modifier = Modifier.fillMaxWidth(),
        )
        Spacer(Modifier.height(SneakSpacing.sectionGap))

        if (lines.isEmpty()) {
            SneakEmptyState(
                title = "Your cart is empty.",
                icon = Icons.Outlined.ShoppingCart,
                modifier = Modifier.weight(1f),
            )
        } else {
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(SneakSpacing.listItemGap),
                contentPadding = PaddingValues(bottom = SneakSpacing.md),
            ) {
                items(lines, key = { it.listing.id }) { line ->
                    val photos = photosFromJson(line.listing.photosJson)
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(20.dp),
                        color = MaterialTheme.colorScheme.surface,
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                        shadowElevation = 0.dp,
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(SneakSpacing.cardPadding),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(SneakSpacing.md),
                        ) {
                            ListingImage(
                                photoUri = photos.firstOrNull(),
                                modifier = Modifier
                                    .size(80.dp)
                                    .clip(RoundedCornerShape(16.dp)),
                            )
                            Column(Modifier.weight(1f)) {
                                Text(
                                    line.listing.category.uppercase(),
                                    style = MaterialTheme.typography.labelMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                )
                                Text(
                                    line.listing.title,
                                    style = MaterialTheme.typography.titleMedium,
                                    color = MaterialTheme.colorScheme.onSurface,
                                )
                                Text(
                                    "${line.listing.category} · ${line.listing.condition}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                )
                                Text(
                                    "$${String.format("%.2f", line.lineTotal)}",
                                    style = priceTextStyle(),
                                    modifier = Modifier.padding(top = SneakSpacing.xs),
                                )
                            }
                            Column(horizontalAlignment = Alignment.End) {
                                IconButton(onClick = { vm.remove(line.listing.id) }) {
                                    Icon(Icons.Default.Delete, contentDescription = "Remove")
                                }
                                Surface(
                                    shape = RoundedCornerShape(20.dp),
                                    color = MaterialTheme.colorScheme.surfaceVariant,
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        IconButton(onClick = { vm.setQty(line.listing.id, line.quantity - 1) }) {
                                            Icon(Icons.Default.Remove, contentDescription = "Decrease")
                                        }
                                        Text(
                                            "${line.quantity}",
                                            style = MaterialTheme.typography.titleMedium,
                                        )
                                        IconButton(onClick = { vm.setQty(line.listing.id, line.quantity + 1) }) {
                                            Icon(Icons.Default.Add, contentDescription = "Increase")
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            SneakCartSummaryCard(
                subtotalFormatted = subtotalStr,
                totalFormatted = subtotalStr,
                onCheckout = { navController.navigate("checkout") },
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}
