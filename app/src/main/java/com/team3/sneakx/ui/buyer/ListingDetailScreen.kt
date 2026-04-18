package com.team3.sneakx.ui.buyer

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.team3.sneakx.LocalAppContainer
import com.team3.sneakx.data.session.Session
import com.team3.sneakx.domain.ListingStatus
import com.team3.sneakx.domain.Role
import com.team3.sneakx.util.ListingImage
import com.team3.sneakx.util.photosFromJson

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListingDetailScreen(
    navController: NavHostController,
    listingId: String,
    session: Session
) {
    val container = LocalAppContainer.current
    val vm: ListingDetailViewModel = viewModel(
        key = listingId,
        factory = object : androidx.lifecycle.ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
                require(modelClass.isAssignableFrom(ListingDetailViewModel::class.java))
                return ListingDetailViewModel(
                    listingId,
                    container.listingRepository,
                    container.cartRepository
                ) as T
            }
        }
    )
    val listing by vm.listing.collectAsState()
    val message by vm.message.collectAsState()
    val snackbar = remember { SnackbarHostState() }

    LaunchedEffect(message) {
        if (message != null) {
            snackbar.showSnackbar(message!!)
            vm.clearMessage()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Listing") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(snackbar) }
    ) { padding ->
        Column(Modifier.fillMaxSize().padding(padding)) {
            val photos = listing?.let { photosFromJson(it.photosJson) } ?: emptyList()
            ListingImage(
                photoUri = photos.firstOrNull(),
                modifier = Modifier
                    .height(220.dp)
                    .fillMaxWidth()
            )
            Column(
                Modifier
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp)
            ) {
                listing?.let { l ->
                    Text(l.title, style = MaterialTheme.typography.headlineMedium)
                    Spacer(Modifier.height(8.dp))
                    Text("$${String.format("%.2f", l.price)}", style = MaterialTheme.typography.titleLarge)
                    Spacer(Modifier.height(4.dp))
                    Text("${l.category} · ${l.condition}", color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Spacer(Modifier.height(12.dp))
                    Text(l.description, style = MaterialTheme.typography.bodyLarge)
                    val buyerId = session.userId
                    val canBuy = session.role == Role.BUYER &&
                        l.status == ListingStatus.ACTIVE.name &&
                        buyerId != null
                    if (canBuy) {
                        Spacer(Modifier.height(16.dp))
                        Button(
                            onClick = {
                                vm.addToCart(buyerId) { }
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Add to cart")
                        }
                    }
                } ?: Text("Listing not found")
            }
        }
    }
}
