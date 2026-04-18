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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.PeopleOutline
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.team3.sneakx.LocalAppContainer
import com.team3.sneakx.domain.Role
import com.team3.sneakx.ui.components.SneakEmptyState
import com.team3.sneakx.ui.components.SneakScreenTitle
import com.team3.sneakx.ui.theme.SneakSpacing

@Composable
fun AdminUsersScreen(adminUserId: String) {
    val container = LocalAppContainer.current
    val vm: AdminUsersViewModel = viewModel(
        key = adminUserId,
        factory = object : androidx.lifecycle.ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
                require(modelClass.isAssignableFrom(AdminUsersViewModel::class.java))
                return AdminUsersViewModel(adminUserId, container.adminRepository) as T
            }
        }
    )
    val users by vm.users.collectAsState()

    Column(
        Modifier
            .fillMaxSize()
            .padding(horizontal = SneakSpacing.screenPadding)
            .padding(top = SneakSpacing.screenTop, bottom = SneakSpacing.sm),
    ) {
        SneakScreenTitle("Users", Modifier.fillMaxWidth())
        Spacer(Modifier.height(SneakSpacing.md))
        if (users.isEmpty()) {
            SneakEmptyState(
                title = "No users",
                icon = Icons.Outlined.PeopleOutline,
                modifier = Modifier.weight(1f),
            )
        } else {
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(SneakSpacing.sm),
                contentPadding = PaddingValues(bottom = SneakSpacing.bottomContentInset),
            ) {
                items(users, key = { it.id }) { user ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = MaterialTheme.shapes.large,
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
                        ),
                        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
                    ) {
                        Row(
                            Modifier
                                .fillMaxWidth()
                                .padding(SneakSpacing.lg),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                        ) {
                            Column(
                                modifier = Modifier.weight(1f),
                                verticalArrangement = Arrangement.spacedBy(SneakSpacing.xs),
                            ) {
                                Text(
                                    user.name,
                                    style = MaterialTheme.typography.titleMedium,
                                    color = MaterialTheme.colorScheme.onSurface,
                                )
                                Text(
                                    user.email,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                )
                                Surface(
                                    shape = MaterialTheme.shapes.small,
                                    color = MaterialTheme.colorScheme.secondaryContainer,
                                ) {
                                    Text(
                                        user.role,
                                        style = MaterialTheme.typography.labelSmall,
                                        color = MaterialTheme.colorScheme.onSecondaryContainer,
                                        modifier = Modifier.padding(
                                            horizontal = SneakSpacing.sm,
                                            vertical = SneakSpacing.xs,
                                        ),
                                    )
                                }
                            }
                            if (user.role != Role.ADMIN.name) {
                                Switch(
                                    checked = user.enabled,
                                    onCheckedChange = { vm.setEnabled(user, it) },
                                    colors = SwitchDefaults.colors(
                                        checkedThumbColor = MaterialTheme.colorScheme.primary,
                                        checkedTrackColor = MaterialTheme.colorScheme.primaryContainer,
                                    ),
                                )
                            } else {
                                Text(
                                    "Admin",
                                    style = MaterialTheme.typography.labelMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
