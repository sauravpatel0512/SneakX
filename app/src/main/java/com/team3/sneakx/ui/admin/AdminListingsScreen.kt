package com.team3.sneakx.ui.admin

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
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.team3.sneakx.LocalAppContainer
import com.team3.sneakx.util.ListingImage
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

    Column(Modifier.fillMaxSize().padding(16.dp)) {
        Text("All listings", style = MaterialTheme.typography.headlineMedium)
        Spacer(Modifier.height(8.dp))
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(bottom = 88.dp)
        ) {
            items(listings, key = { it.id }) { listing ->
                val photos = photosFromJson(listing.photosJson)
                Card(Modifier.fillMaxWidth()) {
                    Column(Modifier.padding(12.dp)) {
                        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            ListingImage(
                                photoUri = photos.firstOrNull(),
                                modifier = Modifier
                                    .height(72.dp)
                                    .weight(0.3f)
                            )
                            Column(Modifier.weight(0.7f)) {
                                Text(listing.title, style = MaterialTheme.typography.titleLarge)
                                Text(
                                    "${listing.status} · $${String.format("%.2f", listing.price)}",
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                        Row(
                            Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Button(
                                onClick = { vm.disableListing(listing) }
                            ) {
                                Text("Disable")
                            }
                            Button(
                                onClick = { vm.removeListing(listing) }
                            ) {
                                Text("Remove")
                            }
                        }
                    }
                }
            }
        }
    }
}
