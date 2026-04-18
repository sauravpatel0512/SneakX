package com.team3.sneakx.ui.buyer

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.SearchOff
import androidx.compose.material.icons.outlined.Tune
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
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
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.team3.sneakx.LocalAppContainer
import com.team3.sneakx.data.local.Categories
import com.team3.sneakx.data.session.Session
import com.team3.sneakx.domain.ListingSort
import com.team3.sneakx.ui.SneakViewModelFactory
import com.team3.sneakx.ui.components.SneakEmptyState
import com.team3.sneakx.ui.components.SneakFilterChipRow
import com.team3.sneakx.ui.components.SneakHeroBanner
import com.team3.sneakx.ui.components.SneakIconCircle
import com.team3.sneakx.ui.components.SneakListingCardGrid
import com.team3.sneakx.ui.components.SneakTopBarHome
import com.team3.sneakx.ui.components.sneakOutlinedTextFieldColors
import com.team3.sneakx.ui.theme.SneakAccentSerifFamily
import com.team3.sneakx.ui.theme.SneakFieldShape
import com.team3.sneakx.ui.theme.SneakSpacing
import com.team3.sneakx.util.photosFromJson
import kotlinx.coroutines.flow.flowOf

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BrowseScreen(navController: NavHostController) {
    val container = LocalAppContainer.current
    val factory = SneakViewModelFactory(container)
    val vm: BrowseViewModel = viewModel(factory = factory)
    val filter by vm.filter.collectAsState()
    val listings by vm.listings.collectAsState()
    val session by container.sessionStore.session.collectAsState(initial = Session(null, null, null))
    val buyerId = session.userId
    val cartFlow = remember(buyerId) {
        if (buyerId != null) container.cartRepository.observeCartLines(buyerId)
        else flowOf(emptyList())
    }
    val cartLines by cartFlow.collectAsState(initial = emptyList())

    var filtersExpanded by remember { mutableStateOf(false) }
    val firstName = session.email?.substringBefore("@")?.takeIf { it.isNotBlank() } ?: "there"
    val chipLabels = remember { listOf("All") + Categories.all }
    val selectedChipIndex = when (val c = filter.category) {
        null -> 0
        else -> chipLabels.indexOf(c).takeIf { it >= 0 } ?: 0
    }

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier.fillMaxSize(),
        horizontalArrangement = Arrangement.spacedBy(SneakSpacing.gridGutterH),
        verticalArrangement = Arrangement.spacedBy(SneakSpacing.gridGutterV),
        contentPadding = PaddingValues(
            start = SneakSpacing.screenPadding,
            end = SneakSpacing.screenPadding,
            top = SneakSpacing.screenTop,
            bottom = SneakSpacing.bottomContentInset,
        ),
    ) {
        item(span = { GridItemSpan(2) }) {
            if (buyerId != null) {
                SneakTopBarHome(
                    userFirstNameOrShort = firstName,
                    cartItemCount = cartLines.size,
                    onNotificationClick = { },
                    onCartClick = { navController.navigate("cart") },
                    avatarUrl = null,
                    modifier = Modifier.fillMaxWidth(),
                )
                Spacer(Modifier.height(SneakSpacing.sectionGap))
            }
        }

        item(span = { GridItemSpan(2) }) {
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = buildAnnotatedString {
                        append("Find your ")
                        withStyle(
                            SpanStyle(
                                fontFamily = SneakAccentSerifFamily,
                                fontStyle = FontStyle.Italic,
                                color = MaterialTheme.colorScheme.onBackground,
                            ),
                        ) {
                            append("sole")
                        }
                        append(".")
                    },
                    style = MaterialTheme.typography.displaySmall,
                    color = MaterialTheme.colorScheme.onBackground,
                )
                Text(
                    text = "Authentic sneakers, traded on campus.",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(top = SneakSpacing.sm),
                )
                Spacer(Modifier.height(SneakSpacing.sectionGap))
            }
        }

        item(span = { GridItemSpan(2) }) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(SneakSpacing.sm),
            ) {
                OutlinedTextField(
                    value = filter.keyword,
                    onValueChange = vm::setKeyword,
                    placeholder = { Text("Air Max, Jordan, Samba…") },
                    leadingIcon = {
                        Icon(Icons.Outlined.Search, contentDescription = null)
                    },
                    modifier = Modifier.weight(1f),
                    singleLine = true,
                    shape = SneakFieldShape,
                    colors = sneakOutlinedTextFieldColors(),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                    keyboardActions = KeyboardActions(
                        onSearch = { vm.applySearch() },
                    ),
                )
                SneakIconCircle(
                    onClick = { filtersExpanded = !filtersExpanded },
                    icon = Icons.Outlined.Tune,
                    contentDescription = "Filters",
                    usePrimaryContainer = true,
                )
            }
        }

        if (filtersExpanded) {
            item(span = { GridItemSpan(2) }) {
                Spacer(Modifier.height(SneakSpacing.md))
                Surface(
                    shape = MaterialTheme.shapes.large,
                    color = MaterialTheme.colorScheme.surfaceContainerLow,
                ) {
                    Column(
                        modifier = Modifier.padding(SneakSpacing.cardPadding),
                        verticalArrangement = Arrangement.spacedBy(SneakSpacing.md),
                    ) {
                        var catExpanded by remember { mutableStateOf(false) }
                        ExposedDropdownMenuBox(
                            expanded = catExpanded,
                            onExpandedChange = { catExpanded = it },
                        ) {
                            val exposedMenuBoxScope = this
                            OutlinedTextField(
                                value = filter.category ?: "",
                                onValueChange = {},
                                readOnly = true,
                                label = { Text("Category") },
                                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = catExpanded) },
                                modifier = Modifier.menuAnchor().fillMaxWidth(),
                                shape = MaterialTheme.shapes.medium,
                                colors = sneakOutlinedTextFieldColors(),
                            )
                            DropdownMenu(
                                expanded = catExpanded,
                                onDismissRequest = { catExpanded = false },
                                modifier = with(exposedMenuBoxScope) {
                                    Modifier.exposedDropdownSize()
                                },
                            ) {
                                DropdownMenuItem(
                                    text = { Text("All") },
                                    onClick = {
                                        vm.setCategory(null)
                                        catExpanded = false
                                    },
                                )
                                Categories.all.forEach { c ->
                                    DropdownMenuItem(
                                        text = { Text(c) },
                                        onClick = {
                                            vm.setCategory(c)
                                            catExpanded = false
                                        },
                                    )
                                }
                            }
                        }
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(SneakSpacing.sm),
                            modifier = Modifier.fillMaxWidth(),
                        ) {
                            OutlinedTextField(
                                value = filter.minPrice,
                                onValueChange = vm::setMinPrice,
                                label = { Text("Min \$") },
                                modifier = Modifier.weight(1f),
                                singleLine = true,
                                shape = MaterialTheme.shapes.medium,
                                colors = sneakOutlinedTextFieldColors(),
                            )
                            OutlinedTextField(
                                value = filter.maxPrice,
                                onValueChange = vm::setMaxPrice,
                                label = { Text("Max \$") },
                                modifier = Modifier.weight(1f),
                                singleLine = true,
                                shape = MaterialTheme.shapes.medium,
                                colors = sneakOutlinedTextFieldColors(),
                            )
                        }
                        var sortExpanded by remember { mutableStateOf(false) }
                        ExposedDropdownMenuBox(
                            expanded = sortExpanded,
                            onExpandedChange = { sortExpanded = it },
                        ) {
                            val exposedMenuBoxScope = this
                            OutlinedTextField(
                                value = filter.sort.label,
                                onValueChange = {},
                                readOnly = true,
                                label = { Text("Sort") },
                                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = sortExpanded) },
                                modifier = Modifier.menuAnchor().fillMaxWidth(),
                                shape = MaterialTheme.shapes.medium,
                                colors = sneakOutlinedTextFieldColors(),
                            )
                            DropdownMenu(
                                expanded = sortExpanded,
                                onDismissRequest = { sortExpanded = false },
                                modifier = with(exposedMenuBoxScope) {
                                    Modifier.exposedDropdownSize()
                                },
                            ) {
                                ListingSort.entries.forEach { s ->
                                    DropdownMenuItem(
                                        text = { Text(s.label) },
                                        onClick = {
                                            vm.setSort(s)
                                            sortExpanded = false
                                        },
                                    )
                                }
                            }
                        }
                        TextButton(onClick = { vm.applySearch() }, modifier = Modifier.fillMaxWidth()) {
                            Text("Apply filters")
                        }
                    }
                }
            }
        }

        item(span = { GridItemSpan(2) }) {
            Spacer(Modifier.height(SneakSpacing.sectionGap))
        }

        listings.firstOrNull()?.let { featured ->
            item(span = { GridItemSpan(2) }) {
                val photos = photosFromJson(featured.photosJson)
                SneakHeroBanner(
                    headline = featured.title,
                    accentLine = featured.category,
                    priceFormatted = "$${String.format("%.2f", featured.price)}",
                    imageUri = photos.firstOrNull(),
                    onShopClick = { navController.navigate("listing_detail/${featured.id}") },
                    modifier = Modifier.fillMaxWidth(),
                )
                Spacer(Modifier.height(SneakSpacing.sectionGap))
            }
        }

        item(span = { GridItemSpan(2) }) {
            SneakFilterChipRow(
                labels = chipLabels,
                selectedIndex = selectedChipIndex,
                onSelect = { idx ->
                    if (idx == 0) vm.setCategory(null) else vm.setCategory(Categories.all[idx - 1])
                },
                modifier = Modifier.fillMaxWidth(),
            )
            Spacer(Modifier.height(SneakSpacing.sectionGap))
        }

        item(span = { GridItemSpan(2) }) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Column {
                    Text("For you", style = MaterialTheme.typography.titleLarge)
                    Text(
                        "${listings.size} listings · curated",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
                var sortOpen by remember { mutableStateOf(false) }
                Box {
                    TextButton(onClick = { sortOpen = true }) {
                        Text("Sort", style = MaterialTheme.typography.labelLarge)
                    }
                    DropdownMenu(
                        expanded = sortOpen,
                        onDismissRequest = { sortOpen = false },
                    ) {
                        ListingSort.entries.forEach { s ->
                            DropdownMenuItem(
                                text = { Text(s.label) },
                                onClick = {
                                    vm.setSort(s)
                                    sortOpen = false
                                },
                            )
                        }
                    }
                }
            }
            Spacer(Modifier.height(SneakSpacing.md))
        }

        if (listings.isEmpty()) {
            item(span = { GridItemSpan(2) }) {
                SneakEmptyState(
                    title = "No results",
                    body = "Try another keyword or adjust filters.",
                    icon = Icons.Outlined.SearchOff,
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        } else {
            items(listings, key = { it.id }) { listing ->
                val photos = photosFromJson(listing.photosJson)
                SneakListingCardGrid(
                    photoUri = photos.firstOrNull(),
                    brandLine = listing.category,
                    title = listing.title,
                    subtitle = listing.condition,
                    priceFormatted = "$${String.format("%.2f", listing.price)}",
                    conditionBadge = listing.condition,
                    onClick = { navController.navigate("listing_detail/${listing.id}") },
                )
            }
        }
    }
}
