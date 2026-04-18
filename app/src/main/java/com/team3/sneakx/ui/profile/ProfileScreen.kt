package com.team3.sneakx.ui.profile

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.team3.sneakx.LocalAppContainer
import com.team3.sneakx.data.session.ThemeMode
import com.team3.sneakx.domain.Role
import com.team3.sneakx.ui.components.SneakErrorBanner
import com.team3.sneakx.ui.components.SneakPrimaryButton
import com.team3.sneakx.ui.components.SneakSectionLabel
import com.team3.sneakx.ui.components.SneakSuccessBanner
import com.team3.sneakx.ui.components.sneakOutlinedTextFieldColors
import com.team3.sneakx.ui.theme.SneakFieldShape
import com.team3.sneakx.ui.theme.SneakHeroShape
import com.team3.sneakx.ui.theme.SneakSpacing
import com.team3.sneakx.util.ListingImage
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
            .padding(horizontal = SneakSpacing.screenPadding)
            .padding(top = SneakSpacing.screenTop, bottom = SneakSpacing.xl),
    ) {
        Column(
            modifier = Modifier
                .widthIn(max = 520.dp)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(SneakSpacing.lg),
        ) {
            Text(
                "Account",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Text(
                "Profile",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onBackground,
            )
            Spacer(Modifier.height(SneakSpacing.sm))

            user?.let { u ->
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = SneakHeroShape,
                    shadowElevation = 0.dp,
                ) {
                    Box(
                        modifier = Modifier
                            .background(
                                brush = Brush.verticalGradient(
                                    colors = listOf(
                                        MaterialTheme.colorScheme.primary,
                                        MaterialTheme.colorScheme.primaryContainer,
                                    ),
                                ),
                            )
                            .padding(SneakSpacing.cardPadding),
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(SneakSpacing.md),
                        ) {
                            ListingImage(
                                photoUri = u.avatarUrl,
                                modifier = Modifier
                                    .size(72.dp)
                                    .clip(CircleShape),
                            )
                            Column {
                                Text(
                                    u.name,
                                    style = MaterialTheme.typography.headlineSmall,
                                    color = MaterialTheme.colorScheme.onPrimary,
                                )
                                Text(
                                    u.email,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.75f),
                                )
                            }
                        }
                    }
                }
            }

            ProfileSection(title = "Account") {
                OutlinedTextField(
                    value = ui.name,
                    onValueChange = vm::setName,
                    label = { Text("Name") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = SneakFieldShape,
                    colors = sneakOutlinedTextFieldColors(),
                )
                OutlinedTextField(
                    value = ui.email,
                    onValueChange = vm::setEmail,
                    label = { Text("Email") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    shape = SneakFieldShape,
                    colors = sneakOutlinedTextFieldColors(),
                )
                ui.error?.let { msg -> SneakErrorBanner(message = msg) }
                if (ui.saved) {
                    SneakSuccessBanner(message = "Saved.")
                }
                SneakPrimaryButton(
                    onClick = { vm.save() },
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Text("Save profile", style = MaterialTheme.typography.labelLarge)
                }
            }

            ProfileSection(title = "Appearance") {
                Column(Modifier.selectableGroup()) {
                    ThemeRow("System", themeMode == ThemeMode.SYSTEM) {
                        scope.launch { container.themePreferenceStore.setDarkOverride(null) }
                    }
                    ThemeRow("Light", themeMode == ThemeMode.LIGHT) {
                        scope.launch { container.themePreferenceStore.setDarkOverride(false) }
                    }
                    ThemeRow("Dark", themeMode == ThemeMode.DARK) {
                        scope.launch { container.themePreferenceStore.setDarkOverride(true) }
                    }
                }
            }

            OutlinedButton(
                onClick = {
                    scope.launch {
                        container.authRepository.logout()
                        navController.navigate("login") {
                            popUpTo(0) { inclusive = true }
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
            ) {
                Text("Log out", color = MaterialTheme.colorScheme.error)
            }

            Column(verticalArrangement = Arrangement.spacedBy(SneakSpacing.xs)) {
                Text(
                    "Role: ${role.name}",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                user?.let {
                    Text(
                        "User id: ${it.id}",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.outline,
                    )
                }
            }
        }
    }
}

@Composable
private fun ProfileSection(
    title: String,
    content: @Composable ColumnScope.() -> Unit,
) {
    Column(verticalArrangement = Arrangement.spacedBy(SneakSpacing.md)) {
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

@Composable
private fun ThemeRow(label: String, selected: Boolean, onClick: () -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 48.dp),
    ) {
        RadioButton(
            selected = selected,
            onClick = onClick,
            colors = RadioButtonDefaults.colors(
                selectedColor = MaterialTheme.colorScheme.primary,
            ),
        )
        Text(
            label,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(start = SneakSpacing.sm),
        )
    }
}

