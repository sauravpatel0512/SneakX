package com.team3.sneakx.ui.buyer

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
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
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.team3.sneakx.LocalAppContainer
import com.team3.sneakx.data.session.Session
import com.team3.sneakx.domain.ListingStatus
import com.team3.sneakx.domain.Role
import com.team3.sneakx.ui.components.sneakTopAppBarColors
import com.team3.sneakx.ui.theme.SneakSpacing
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
                },
                colors = sneakTopAppBarColors(),
            )
        },
        snackbarHost = { SnackbarHost(snackbar) },
    ) { padding ->
        Column(Modifier.fillMaxSize().padding(padding)) {
            val photos = listing?.let { photosFromJson(it.photosJson) } ?: emptyList()
            var mainIndex by remember(listingId) { mutableIntStateOf(0) }

            Column(
                Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState()),
            ) {
                Column(Modifier.fillMaxWidth()) {
                    ListingImage(
                        photoUri = photos.getOrNull(
                            mainIndex.coerceIn(0, (photos.size - 1).coerceAtLeast(0)),
                        ),
                        modifier = Modifier
                            .height(240.dp)
                            .fillMaxWidth()
                            .padding(horizontal = SneakSpacing.screenPadding)
                            .padding(top = SneakSpacing.sm)
                            .clip(MaterialTheme.shapes.large),
                    )
                    if (photos.size > 1) {
                        LazyRow(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = SneakSpacing.screenPadding)
                                .padding(top = SneakSpacing.sm),
                            horizontalArrangement = Arrangement.spacedBy(SneakSpacing.sm),
                        ) {
                            itemsIndexed(photos) { index, uri ->
                                Surface(
                                    onClick = { mainIndex = index },
                                    shape = MaterialTheme.shapes.small,
                                    border = if (index == mainIndex) {
                                        BorderStroke(2.dp, MaterialTheme.colorScheme.primary)
                                    } else {
                                        BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
                                    },
                                ) {
                                    ListingImage(
                                        photoUri = uri,
                                        modifier = Modifier.size(64.dp),
                                    )
                                }
                            }
                        }
                    }
                }

                Column(
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = SneakSpacing.screenPadding)
                        .padding(top = SneakSpacing.lg, bottom = SneakSpacing.xl),
                    verticalArrangement = Arrangement.spacedBy(SneakSpacing.md),
                ) {
                    listing?.let { l ->
                        Text(
                            l.title,
                            style = MaterialTheme.typography.headlineMedium,
                            color = MaterialTheme.colorScheme.onBackground,
                        )
                        Text(
                            "$${String.format("%.2f", l.price)}",
                            style = MaterialTheme.typography.titleLarge,
                            color = MaterialTheme.colorScheme.primary,
                        )
                        Text(
                            "${l.category} · ${l.condition}",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )

                        val buyerId = session.userId
                        val canBuy = session.role == Role.BUYER &&
                            l.status == ListingStatus.ACTIVE.name &&
                            buyerId != null
                        val showStatusOnly = session.role == Role.BUYER &&
                            buyerId != null &&
                            l.status != ListingStatus.ACTIVE.name

                        if (showStatusOnly) {
                            Surface(
                                shape = MaterialTheme.shapes.small,
                                color = MaterialTheme.colorScheme.surfaceContainerHighest,
                                tonalElevation = 0.dp,
                            ) {
                                Text(
                                    text = l.status,
                                    style = MaterialTheme.typography.labelLarge,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.padding(SneakSpacing.md),
                                )
                            }
                        }

                        Surface(
                            shape = MaterialTheme.shapes.large,
                            color = MaterialTheme.colorScheme.surfaceContainerLow,
                        ) {
                            Text(
                                l.description,
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier.padding(SneakSpacing.lg),
                            )
                        }

                        if (canBuy) {
                            Button(
                                onClick = {
                                    vm.addToCart(buyerId) { }
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = SneakSpacing.sm),
                                shape = MaterialTheme.shapes.large,
                            ) {
                                Text("Add to cart")
                            }
                        }
                    } ?: Text(
                        "Listing not found",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(SneakSpacing.lg),
                    )
                }
            }
        }
    }
}
