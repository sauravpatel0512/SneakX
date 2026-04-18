package com.team3.sneakx.ui.buyer

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
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenu
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
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
import com.team3.sneakx.data.local.Categories
import com.team3.sneakx.domain.ListingSort
import com.team3.sneakx.LocalAppContainer
import com.team3.sneakx.ui.SneakViewModelFactory
import com.team3.sneakx.util.ListingImage
import com.team3.sneakx.util.photosFromJson

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BrowseScreen(navController: NavHostController) {
    val container = LocalAppContainer.current
    val factory = SneakViewModelFactory(container)
    val vm: BrowseViewModel = viewModel(factory = factory)
    val filter by vm.filter.collectAsState()
    val listings by vm.listings.collectAsState()

    Column(Modifier.fillMaxSize().padding(16.dp)) {
        Text("Marketplace", style = MaterialTheme.typography.headlineMedium)
        Spacer(Modifier.height(8.dp))
        OutlinedTextField(
            value = filter.keyword,
            onValueChange = vm::setKeyword,
            label = { Text("Keyword") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )
        Spacer(Modifier.height(8.dp))
        var catExpanded by remember { mutableStateOf(false) }
        ExposedDropdownMenuBox(expanded = catExpanded, onExpandedChange = { catExpanded = it }) {
            OutlinedTextField(
                value = filter.category ?: "",
                onValueChange = {},
                readOnly = true,
                label = { Text("Category (optional)") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = catExpanded) },
                modifier = Modifier.menuAnchor().fillMaxWidth()
            )
            ExposedDropdownMenu(expanded = catExpanded, onDismissRequest = { catExpanded = false }) {
                DropdownMenuItem(
                    text = { Text("All") },
                    onClick = {
                        vm.setCategory(null)
                        catExpanded = false
                    }
                )
                Categories.all.forEach { c ->
                    DropdownMenuItem(
                        text = { Text(c) },
                        onClick = {
                            vm.setCategory(c)
                            catExpanded = false
                        }
                    )
                }
            }
        }
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
            OutlinedTextField(
                value = filter.minPrice,
                onValueChange = vm::setMinPrice,
                label = { Text("Min $") },
                modifier = Modifier.weight(1f),
                singleLine = true
            )
            OutlinedTextField(
                value = filter.maxPrice,
                onValueChange = vm::setMaxPrice,
                label = { Text("Max $") },
                modifier = Modifier.weight(1f),
                singleLine = true
            )
        }
        Spacer(Modifier.height(8.dp))
        var sortExpanded by remember { mutableStateOf(false) }
        ExposedDropdownMenuBox(expanded = sortExpanded, onExpandedChange = { sortExpanded = it }) {
            OutlinedTextField(
                value = filter.sort.label,
                onValueChange = {},
                readOnly = true,
                label = { Text("Sort") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = sortExpanded) },
                modifier = Modifier.menuAnchor().fillMaxWidth()
            )
            ExposedDropdownMenu(expanded = sortExpanded, onDismissRequest = { sortExpanded = false }) {
                ListingSort.entries.forEach { s ->
                    DropdownMenuItem(
                        text = { Text(s.label) },
                        onClick = {
                            vm.setSort(s)
                            sortExpanded = false
                        }
                    )
                }
            }
        }
        Spacer(Modifier.height(8.dp))
        Button(onClick = { vm.applySearch() }, modifier = Modifier.fillMaxWidth()) {
            Text("Search")
        }
        Spacer(Modifier.height(8.dp))
        if (listings.isEmpty()) {
            Text("No results", color = MaterialTheme.colorScheme.onSurfaceVariant)
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(bottom = 88.dp)
            ) {
                items(listings, key = { it.id }) { listing ->
                    val photos = photosFromJson(listing.photosJson)
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                navController.navigate("listing_detail/${listing.id}")
                            }
                    ) {
                        Row(
                            Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            ListingImage(
                                photoUri = photos.firstOrNull(),
                                modifier = Modifier
                                    .height(80.dp)
                                    .weight(0.35f)
                            )
                            Column(Modifier.weight(0.65f)) {
                                Text(listing.title, style = MaterialTheme.typography.titleLarge)
                                Text(
                                    "$${String.format("%.2f", listing.price)} · ${listing.category}",
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
