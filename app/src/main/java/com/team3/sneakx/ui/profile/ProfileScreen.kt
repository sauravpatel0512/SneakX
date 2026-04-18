package com.team3.sneakx.ui.profile

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.team3.sneakx.LocalAppContainer
import com.team3.sneakx.data.session.ThemeMode
import com.team3.sneakx.domain.Role
import kotlinx.coroutines.launch

@Composable
fun ProfileScreen(
    navController: NavHostController,
    userId: String,
    role: Role
) {
    val container = LocalAppContainer.current
    val scope = rememberCoroutineScope()
    val vm: ProfileViewModel = viewModel(
        key = userId,
        factory = object : androidx.lifecycle.ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
                require(modelClass.isAssignableFrom(ProfileViewModel::class.java))
                return ProfileViewModel(userId, container.profileRepository, container.sessionStore) as T
            }
        }
    )
    val ui by vm.ui.collectAsState()
    val user by vm.user.collectAsState()
    val themeMode by container.themePreferenceStore.themeMode.collectAsState(initial = ThemeMode.SYSTEM)

    Column(
        Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Text("Profile", style = MaterialTheme.typography.headlineMedium)
        Spacer(Modifier.padding(8.dp))
        OutlinedTextField(
            value = ui.name,
            onValueChange = vm::setName,
            label = { Text("Name") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.padding(8.dp))
        OutlinedTextField(
            value = ui.email,
            onValueChange = vm::setEmail,
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )
        ui.error?.let {
            Text(it, color = MaterialTheme.colorScheme.error)
            Spacer(Modifier.padding(4.dp))
        }
        if (ui.saved) {
            Text("Saved.", color = MaterialTheme.colorScheme.primary)
            Spacer(Modifier.padding(4.dp))
        }
        Button(onClick = { vm.save() }, modifier = Modifier.fillMaxWidth()) {
            Text("Save profile")
        }
        Spacer(Modifier.padding(16.dp))
        Text("Theme", style = MaterialTheme.typography.titleLarge)
        ThemeRow("System", themeMode == ThemeMode.SYSTEM) {
            scope.launch { container.themePreferenceStore.setDarkOverride(null) }
        }
        ThemeRow("Light", themeMode == ThemeMode.LIGHT) {
            scope.launch { container.themePreferenceStore.setDarkOverride(false) }
        }
        ThemeRow("Dark", themeMode == ThemeMode.DARK) {
            scope.launch { container.themePreferenceStore.setDarkOverride(true) }
        }
        Spacer(Modifier.padding(16.dp))
        Button(
            onClick = {
                scope.launch {
                    container.authRepository.logout()
                    navController.navigate("login") {
                        popUpTo(0) { inclusive = true }
                    }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Log out")
        }
        Spacer(Modifier.padding(8.dp))
        Text("Role: ${role.name}", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        user?.let {
            Text("User id: ${it.id}", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.outline)
        }
    }
}

@Composable
private fun ThemeRow(label: String, selected: Boolean, onClick: () -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        RadioButton(selected = selected, onClick = onClick)
        Text(label)
    }
}
