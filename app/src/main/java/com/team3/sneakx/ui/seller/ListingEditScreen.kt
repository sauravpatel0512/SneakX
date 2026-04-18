package com.team3.sneakx.ui.seller

import androidx.compose.foundation.BorderStroke
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.AddPhotoAlternate
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.team3.sneakx.LocalAppContainer
import com.team3.sneakx.data.local.Categories
import com.team3.sneakx.ui.components.SneakErrorBanner
import com.team3.sneakx.ui.components.SneakPrimaryButtonContent
import com.team3.sneakx.ui.components.SneakSectionLabel
import com.team3.sneakx.ui.components.sneakOutlinedTextFieldColors
import com.team3.sneakx.ui.components.sneakTopAppBarColors
import com.team3.sneakx.ui.theme.SneakSpacing
import com.team3.sneakx.util.ListingImage
import com.team3.sneakx.util.copyContentUriToAppFiles

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListingEditScreen(
    navController: NavHostController,
    sellerId: String,
    rawListingId: String
) {
    val container = LocalAppContainer.current
    val existingId = rawListingId.takeIf { it != "new" }
    val vm: ListingEditViewModel = viewModel(
        key = "$sellerId-$rawListingId",
        factory = object : androidx.lifecycle.ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
                require(modelClass.isAssignableFrom(ListingEditViewModel::class.java))
                return ListingEditViewModel(
                    sellerId,
                    existingId,
                    container.listingRepository
                ) as T
            }
        }
    )
    val ui by vm.ui.collectAsState()
    val context = LocalContext.current
    val pick = rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        if (uri != null) {
            runCatching {
                val stored = copyContentUriToAppFiles(context, uri)
                vm.setPhotos(ui.photoUris + stored)
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (existingId == null) "New listing" else "Edit listing") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = sneakTopAppBarColors(),
            )
        },
    ) { padding ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = SneakSpacing.screenPadding)
                .padding(top = SneakSpacing.md, bottom = SneakSpacing.xl),
        ) {
            Column(
                modifier = Modifier
                    .widthIn(max = 520.dp)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(SneakSpacing.lg),
            ) {
                if (existingId != null) {
                    Text(
                        text = "Update your listing details, then save.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
                FormSection(title = "Listing") {
                    OutlinedTextField(
                        value = ui.title,
                        onValueChange = vm::setTitle,
                        label = { Text("Title") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        shape = MaterialTheme.shapes.medium,
                        colors = sneakOutlinedTextFieldColors(),
                    )
                    OutlinedTextField(
                        value = ui.description,
                        onValueChange = vm::setDescription,
                        label = { Text("Description") },
                        modifier = Modifier.fillMaxWidth(),
                        minLines = 3,
                        shape = MaterialTheme.shapes.medium,
                        colors = sneakOutlinedTextFieldColors(),
                    )
                }

                FormSection(title = "Details") {
                    var catExpanded by remember { mutableStateOf(false) }
                    ExposedDropdownMenuBox(
                        expanded = catExpanded,
                        onExpandedChange = { catExpanded = it },
                    ) {
                        OutlinedTextField(
                            value = ui.category,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Category") },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = catExpanded) },
                            modifier = Modifier.menuAnchor().fillMaxWidth(),
                            shape = MaterialTheme.shapes.medium,
                            colors = sneakOutlinedTextFieldColors(),
                        )
                        ExposedDropdownMenu(
                            expanded = catExpanded,
                            onDismissRequest = { catExpanded = false },
                        ) {
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
                    OutlinedTextField(
                        value = ui.price,
                        onValueChange = vm::setPrice,
                        label = { Text("Price") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        shape = MaterialTheme.shapes.medium,
                        colors = sneakOutlinedTextFieldColors(),
                    )
                    OutlinedTextField(
                        value = ui.condition,
                        onValueChange = vm::setCondition,
                        label = { Text("Condition (e.g. New, Used)") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = MaterialTheme.shapes.medium,
                        colors = sneakOutlinedTextFieldColors(),
                    )
                }

                FormSection(title = "Photos") {
                    Text(
                        text = "Photos: ${ui.photoUris.size}",
                        style = MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    if (ui.photoUris.isNotEmpty()) {
                        Surface(
                            modifier = Modifier.fillMaxWidth(),
                            shape = MaterialTheme.shapes.large,
                            color = MaterialTheme.colorScheme.surfaceContainer,
                            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
                            shadowElevation = 0.dp,
                        ) {
                            LazyRow(
                                contentPadding = PaddingValues(SneakSpacing.md),
                                horizontalArrangement = Arrangement.spacedBy(SneakSpacing.sm),
                                modifier = Modifier.fillMaxWidth(),
                            ) {
                                items(ui.photoUris, key = { it }) { uri ->
                                    Surface(
                                        shape = MaterialTheme.shapes.medium,
                                        tonalElevation = 0.dp,
                                        shadowElevation = 0.dp,
                                        color = MaterialTheme.colorScheme.surfaceContainerHigh,
                                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
                                    ) {
                                        ListingImage(
                                            photoUri = uri,
                                            modifier = Modifier
                                                .size(96.dp)
                                                .clip(MaterialTheme.shapes.medium),
                                        )
                                    }
                                }
                            }
                        }
                    }
                    FilledTonalButton(
                        onClick = {
                            pick.launch(
                                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly),
                            )
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = MaterialTheme.shapes.large,
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center,
                        ) {
                            Icon(
                                Icons.Outlined.AddPhotoAlternate,
                                contentDescription = null,
                                modifier = Modifier.size(20.dp),
                            )
                            Spacer(Modifier.width(SneakSpacing.sm))
                            Text("Add photo")
                        }
                    }
                }

                ui.error?.let { msg -> SneakErrorBanner(message = msg) }

                HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = MaterialTheme.shapes.large,
                    color = MaterialTheme.colorScheme.surfaceContainerHigh,
                    shadowElevation = 0.dp,
                ) {
                    Column(
                        modifier = Modifier.padding(SneakSpacing.lg),
                    ) {
                        Button(
                            onClick = {
                                vm.save { navController.popBackStack() }
                            },
                            enabled = !ui.loading,
                            modifier = Modifier.fillMaxWidth(),
                            shape = MaterialTheme.shapes.large,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.primary,
                                contentColor = MaterialTheme.colorScheme.onPrimary,
                                disabledContainerColor = MaterialTheme.colorScheme.surfaceContainerHighest,
                                disabledContentColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f),
                            ),
                        ) {
                            SneakPrimaryButtonContent(
                                loading = ui.loading,
                                loadingText = "Saving…",
                                idleText = "Save",
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun FormSection(
    title: String,
    content: @Composable ColumnScope.() -> Unit,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(SneakSpacing.md),
    ) {
        SneakSectionLabel(title)
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.large,
            color = MaterialTheme.colorScheme.surfaceContainerLow,
            shadowElevation = 0.dp,
        ) {
            Column(
                modifier = Modifier.padding(SneakSpacing.lg),
                verticalArrangement = Arrangement.spacedBy(SneakSpacing.md),
            ) {
                content()
            }
        }
    }
}

