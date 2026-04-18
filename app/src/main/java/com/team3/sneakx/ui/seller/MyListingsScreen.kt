package com.team3.sneakx.ui.seller

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.weight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.team3.sneakx.LocalAppContainer
import com.team3.sneakx.data.local.entity.ListingEntity
import com.team3.sneakx.util.ListingImage
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
                TextButton(onClick = {
                    vm.delete(listing)
                    toDelete = null
                }) { Text("Delete") }
            },
            dismissButton = {
                TextButton(onClick = { toDelete = null }) { Text("Cancel") }
            }
        )
    }

    Column(Modifier.fillMaxSize().padding(16.dp)) {
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("My listings", style = MaterialTheme.typography.headlineMedium)
            Button(onClick = { navController.navigate("listing_edit/new") }) {
                Text("New")
            }
        }
        Spacer(Modifier.height(8.dp))
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(bottom = 88.dp)
        ) {
            items(listings, key = { it.id }) { listing ->
                val photos = photosFromJson(listing.photosJson)
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { navController.navigate("listing_edit/${listing.id}") }
                ) {
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        ListingImage(
                            photoUri = photos.firstOrNull(),
                            modifier = Modifier.height(72.dp).weight(0.3f)
                        )
                        Column(Modifier.weight(0.5f)) {
                            Text(listing.title, style = MaterialTheme.typography.titleLarge)
                            Text(listing.status, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                        TextButton(onClick = { toDelete = listing }) {
                            Text("Delete")
                        }
                    }
                }
            }
        }
    }
}
