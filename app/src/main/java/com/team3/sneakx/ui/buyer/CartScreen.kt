package com.team3.sneakx.ui.buyer

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
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.team3.sneakx.LocalAppContainer
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

    Column(Modifier.fillMaxSize().padding(16.dp)) {
        Text("Cart", style = MaterialTheme.typography.headlineMedium)
        Spacer(Modifier.padding(8.dp))
        if (lines.isEmpty()) {
            Text("Your cart is empty.", color = MaterialTheme.colorScheme.onSurfaceVariant)
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(bottom = 120.dp)
            ) {
                items(lines, key = { it.listing.id }) { line ->
                    val photos = photosFromJson(line.listing.photosJson)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        ListingImage(
                            photoUri = photos.firstOrNull(),
                            modifier = Modifier
                                .height(72.dp)
                                .weight(0.25f)
                                .padding(4.dp)
                        )
                        Column(Modifier.weight(0.45f)) {
                            Text(line.listing.title, style = MaterialTheme.typography.titleLarge)
                            Text(
                                "$${String.format("%.2f", line.listing.price)} each",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            IconButton(onClick = { vm.setQty(line.listing.id, line.quantity - 1) }) {
                                Icon(Icons.Default.Remove, contentDescription = "Decrease")
                            }
                            Text("${line.quantity}")
                            IconButton(onClick = { vm.setQty(line.listing.id, line.quantity + 1) }) {
                                Icon(Icons.Default.Add, contentDescription = "Increase")
                            }
                            IconButton(onClick = { vm.remove(line.listing.id) }) {
                                Icon(Icons.Default.Delete, contentDescription = "Remove")
                            }
                        }
                    }
                    Text(
                        "Line: $${String.format("%.2f", line.lineTotal)}",
                        style = MaterialTheme.typography.labelLarge,
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }
            }
        }
        Spacer(Modifier.weight(1f))
        Text("Subtotal: $${String.format("%.2f", subtotal)}", style = MaterialTheme.typography.titleLarge)
        Spacer(Modifier.padding(8.dp))
        Button(
            onClick = { navController.navigate("checkout") },
            enabled = lines.isNotEmpty(),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Checkout")
        }
    }
}
