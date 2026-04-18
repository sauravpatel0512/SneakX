package com.team3.sneakx.ui.admin

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Storefront
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.team3.sneakx.LocalAppContainer
import com.team3.sneakx.ui.components.SneakEmptyState
import com.team3.sneakx.ui.components.SneakListingCard
import com.team3.sneakx.ui.components.SneakScreenTitle
import com.team3.sneakx.ui.theme.SneakLime
import com.team3.sneakx.ui.theme.SneakSpacing
import com.team3.sneakx.util.photosFromJson

@Composable
fun AdminListingsScreen(adminUserId: String) {
    val container = LocalAppContainer.current
    val vm: AdminListingsViewModel = viewModel(
        key = adminUserId,
        factory = object : androidx.lifecycle.ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
                require(modelClass.isAssignableFrom(AdminListingsViewModel::class.java))
                return AdminListingsViewModel(adminUserId, container.adminRepository) as T
            }
        }
    )
    val listings by vm.listings.collectAsState()

    Column(
        Modifier
            .fillMaxSize()
            .padding(horizontal = SneakSpacing.screenPadding)
            .padding(top = SneakSpacing.screenTop, bottom = SneakSpacing.sm),
    ) {
        Row(verticalAlignment = Alignment.Bottom) {
            SneakScreenTitle("Admin", Modifier.weight(1f))
            Text(
                "Listings",
                style = MaterialTheme.typography.labelLarge,
                color = SneakLime,
            )
        }
        Spacer(Modifier.height(SneakSpacing.sm))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(SneakSpacing.sm),
        ) {
            AdminKpiCard("Listings", listings.size.toString())
        }
        Spacer(Modifier.height(SneakSpacing.md))
        if (listings.isEmpty()) {
            SneakEmptyState(
                title = "No listings",
                icon = Icons.Outlined.Storefront,
                modifier = Modifier.weight(1f),
            )
        } else {
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(SneakSpacing.sm),
                contentPadding = PaddingValues(bottom = SneakSpacing.bottomContentInset),
            ) {
                items(listings, key = { it.id }) { listing ->
                    val photos = photosFromJson(listing.photosJson)
                    SneakListingCard(
                        photoUri = photos.firstOrNull(),
                        title = listing.title,
                        subtitle = "$${String.format("%.2f", listing.price)}",
                        statusBadge = listing.status,
                        imageWeight = 0.3f,
                        onClick = null,
                        footer = {
                            Row(
                                Modifier
                                    .fillMaxWidth()
                                    .padding(
                                        start = SneakSpacing.cardPadding,
                                        end = SneakSpacing.cardPadding,
                                        bottom = SneakSpacing.cardPadding,
                                    ),
                                horizontalArrangement = Arrangement.spacedBy(SneakSpacing.sm),
                            ) {
                                FilledTonalButton(
                                    onClick = { vm.disableListing(listing) },
                                    shape = MaterialTheme.shapes.large,
                                    modifier = Modifier.weight(1f),
                                ) {
                                    Text("Disable")
                                }
                                OutlinedButton(
                                    onClick = { vm.removeListing(listing) },
                                    shape = MaterialTheme.shapes.large,
                                    modifier = Modifier.weight(1f),
                                    colors = ButtonDefaults.outlinedButtonColors(
                                        contentColor = MaterialTheme.colorScheme.error,
                                    ),
                                ) {
                                    Text("Remove")
                                }
                            }
                        },
                    )
                }
            }
        }
    }
}

@Composable
private fun AdminKpiCard(label: String, value: String) {
    Surface(
        modifier = Modifier
            .width(160.dp)
            .height(100.dp),
        shape = MaterialTheme.shapes.large,
        color = MaterialTheme.colorScheme.surface,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
        tonalElevation = 0.dp,
        shadowElevation = 0.dp,
    ) {
        Column(
            modifier = Modifier.padding(SneakSpacing.cardPadding),
            verticalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(
                label.uppercase(),
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Text(
                value,
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onBackground,
            )
        }
    }
}
