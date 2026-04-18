package com.team3.sneakx.ui.seller

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Inventory2
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.team3.sneakx.LocalAppContainer
import com.team3.sneakx.data.local.entity.ListingEntity
import com.team3.sneakx.ui.components.SneakEmptyState
import com.team3.sneakx.ui.components.SneakListingCard
import com.team3.sneakx.ui.components.SneakPrimaryButton
import com.team3.sneakx.ui.components.SneakScreenTitle
import com.team3.sneakx.ui.theme.SneakSpacing
import com.team3.sneakx.util.photosFromJson

@Composable
fun MyListingsScreen(
    navController: NavHostController,
    sellerId: String
) {
    val container = LocalAppContainer.current
    val vm: SellerListingsViewModel = viewModel(
        key = sellerId,
        factory = object : androidx.lifecycle.ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
                require(modelClass.isAssignableFrom(SellerListingsViewModel::class.java))
                return SellerListingsViewModel(sellerId, container.listingRepository) as T
            }
        }
    )
    val listings by vm.listings.collectAsState()
    var toDelete by remember { mutableStateOf<ListingEntity?>(null) }

    toDelete?.let { listing ->
        AlertDialog(
            onDismissRequest = { toDelete = null },
            title = { Text("Delete listing?") },
            text = { Text(listing.title) },
            confirmButton = {
                TextButton(
                    onClick = {
                        vm.delete(listing)
                        toDelete = null
                    },
                ) { Text("Delete") }
            },
            dismissButton = {
                TextButton(onClick = { toDelete = null }) { Text("Cancel") }
            },
            containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
        )
    }

    Column(
        Modifier
            .fillMaxSize()
            .padding(horizontal = SneakSpacing.screenPadding)
            .padding(top = SneakSpacing.screenTop, bottom = SneakSpacing.sm),
    ) {
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            SneakScreenTitle("My listings", modifier = Modifier.weight(1f))
            SneakPrimaryButton(onClick = { navController.navigate("listing_edit/new") }) {
                Text("New", style = MaterialTheme.typography.labelLarge)
            }
        }
        Spacer(Modifier.height(SneakSpacing.md))
        if (listings.isEmpty()) {
            SneakEmptyState(
                title = "No listings yet",
                body = "Tap New to create your first listing.",
                icon = Icons.Outlined.Inventory2,
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
                        subtitle = null,
                        statusBadge = listing.status,
                        imageWeight = 0.3f,
                        onClick = { navController.navigate("listing_edit/${listing.id}") },
                        trailing = {
                            TextButton(
                                onClick = { toDelete = listing },
                                shape = MaterialTheme.shapes.large,
                            ) {
                                Text("Delete")
                            }
                        },
                    )
                }
            }
        }
    }
}
