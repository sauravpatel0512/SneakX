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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.SearchOff
import androidx.compose.material.icons.outlined.Tune
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
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
import com.team3.sneakx.data.local.Categories
import com.team3.sneakx.domain.ListingSort
import com.team3.sneakx.ui.SneakViewModelFactory
import com.team3.sneakx.ui.components.SneakEmptyState
import com.team3.sneakx.ui.components.SneakListingCard
import com.team3.sneakx.ui.components.SneakScreenTitle
import com.team3.sneakx.ui.components.sneakOutlinedTextFieldColors
import com.team3.sneakx.ui.theme.SneakSpacing
import com.team3.sneakx.util.photosFromJson

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BrowseScreen(navController: NavHostController) {
    val container = LocalAppContainer.current
    val factory = SneakViewModelFactory(container)
    val vm: BrowseViewModel = viewModel(factory = factory)
    val filter by vm.filter.collectAsState()
    val listings by vm.listings.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = SneakSpacing.screenPadding)
            .padding(top = SneakSpacing.lg, bottom = SneakSpacing.sm),
    ) {
        SneakScreenTitle(
            text = "Marketplace",
            modifier = Modifier.fillMaxWidth(),
        )
        Spacer(Modifier.height(SneakSpacing.lg))

        OutlinedTextField(
            value = filter.keyword,
            onValueChange = vm::setKeyword,
            label = { Text("Keyword") },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Outlined.Search,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            shape = MaterialTheme.shapes.large,
            colors = sneakOutlinedTextFieldColors(),
        )

        Spacer(Modifier.height(SneakSpacing.md))

        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.large,
            color = MaterialTheme.colorScheme.surfaceContainerLow,
        ) {
            Column(
                modifier = Modifier.padding(SneakSpacing.lg),
                verticalArrangement = Arrangement.spacedBy(SneakSpacing.md),
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(SneakSpacing.sm),
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Tune,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp),
                        tint = MaterialTheme.colorScheme.primary,
                    )
                    Text(
                        text = "Filters & sort",
                        style = MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }

                var catExpanded by remember { mutableStateOf(false) }
                ExposedDropdownMenuBox(
                    expanded = catExpanded,
                    onExpandedChange = { catExpanded = it },
                ) {
                    OutlinedTextField(
                        value = filter.category ?: "",
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Category (optional)") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = catExpanded) },
                        modifier = Modifier.menuAnchor().fillMaxWidth(),
                        shape = MaterialTheme.shapes.medium,
                        colors = sneakOutlinedTextFieldColors(),
                    )
                    ExposedDropdownMenu(
                        expanded = catExpanded,
                        onDismissRequest = { catExpanded = false },
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
                        label = { Text("Min $") },
                        modifier = Modifier.weight(1f),
                        singleLine = true,
                        shape = MaterialTheme.shapes.medium,
                        colors = sneakOutlinedTextFieldColors(),
                    )
                    OutlinedTextField(
                        value = filter.maxPrice,
                        onValueChange = vm::setMaxPrice,
                        label = { Text("Max $") },
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
                    ExposedDropdownMenu(
                        expanded = sortExpanded,
                        onDismissRequest = { sortExpanded = false },
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

                Button(
                    onClick = { vm.applySearch() },
                    modifier = Modifier.fillMaxWidth(),
                    shape = MaterialTheme.shapes.large,
                ) {
                    Text("Search")
                }
            }
        }

        Spacer(Modifier.height(SneakSpacing.md))

        if (listings.isEmpty()) {
            SneakEmptyState(
                title = "No results",
                body = "Try another keyword or adjust filters, then tap Search.",
                icon = Icons.Outlined.SearchOff,
                modifier = Modifier.weight(1f, fill = true),
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
                        subtitle = "$${String.format("%.2f", listing.price)} · ${listing.category}",
                        onClick = { navController.navigate("listing_detail/${listing.id}") },
                    )
                }
            }
        }
    }
}
