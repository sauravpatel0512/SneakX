package com.team3.sneakx.ui.admin

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
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

    Column(Modifier.fillMaxSize().padding(16.dp)) {
        Text("Users", style = MaterialTheme.typography.headlineMedium)
        Spacer(Modifier.padding(8.dp))
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(bottom = 88.dp)
        ) {
            items(users, key = { it.id }) { user ->
                Card(Modifier.fillMaxWidth()) {
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(user.name, style = MaterialTheme.typography.titleLarge)
                            Text(user.email, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            Text(user.role, style = MaterialTheme.typography.labelLarge)
                        }
                        if (user.role != Role.ADMIN.name) {
                            Switch(
                                checked = user.enabled,
                                onCheckedChange = { vm.setEnabled(user, it) }
                            )
                        } else {
                            Text("Admin", style = MaterialTheme.typography.bodySmall)
                        }
                    }
                }
            }
        }
    }
}
