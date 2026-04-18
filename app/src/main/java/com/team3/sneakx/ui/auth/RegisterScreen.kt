package com.team3.sneakx.ui.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.team3.sneakx.LocalAppContainer
import com.team3.sneakx.domain.Role
import com.team3.sneakx.ui.SneakViewModelFactory
import com.team3.sneakx.ui.components.SneakErrorBanner
import com.team3.sneakx.ui.components.SneakPrimaryButton
import com.team3.sneakx.ui.components.SneakPrimaryButtonContent
import com.team3.sneakx.ui.components.sneakOutlinedTextFieldColors
import com.team3.sneakx.ui.theme.SneakFieldShape
import com.team3.sneakx.ui.theme.SneakSpacing

@Composable
fun RegisterScreen(
    onRegistered: () -> Unit,
    onBack: () -> Unit
) {
    val container = LocalAppContainer.current
    val factory = SneakViewModelFactory(container)
    val vm: RegisterViewModel = viewModel(factory = factory)
    val ui by vm.ui.collectAsState()

    val scroll = rememberScrollState()
    val hasError = ui.error != null

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scroll)
            .padding(horizontal = SneakSpacing.screenPadding)
            .padding(top = SneakSpacing.xxl, bottom = SneakSpacing.xl),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Column(
            modifier = Modifier
                .widthIn(max = 420.dp)
                .fillMaxWidth(),
        ) {
            Text(
                text = "Join SneakX.",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onBackground,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth(),
            )
            Spacer(Modifier.height(SneakSpacing.xl))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.large,
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceContainerLow,
                    contentColor = MaterialTheme.colorScheme.onSurface,
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
            ) {
                Column(
                    modifier = Modifier.padding(SneakSpacing.lg),
                    verticalArrangement = Arrangement.spacedBy(SneakSpacing.md),
                ) {
                    Text(
                        text = "Account",
                        style = MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    OutlinedTextField(
                        value = ui.name,
                        onValueChange = vm::setName,
                        label = { Text("Name") },
                        singleLine = true,
                        isError = hasError,
                        shape = SneakFieldShape,
                        colors = sneakOutlinedTextFieldColors(),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Text,
                            imeAction = ImeAction.Next,
                        ),
                        modifier = Modifier.fillMaxWidth(),
                    )
                    OutlinedTextField(
                        value = ui.email,
                        onValueChange = vm::setEmail,
                        label = { Text("Email") },
                        singleLine = true,
                        isError = hasError,
                        shape = SneakFieldShape,
                        colors = sneakOutlinedTextFieldColors(),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Email,
                            imeAction = ImeAction.Next,
                        ),
                        modifier = Modifier.fillMaxWidth(),
                    )
                    OutlinedTextField(
                        value = ui.password,
                        onValueChange = vm::setPassword,
                        label = { Text("Password") },
                        singleLine = true,
                        isError = hasError,
                        visualTransformation = PasswordVisualTransformation(),
                        shape = SneakFieldShape,
                        colors = sneakOutlinedTextFieldColors(),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Password,
                            imeAction = ImeAction.Done,
                        ),
                        modifier = Modifier.fillMaxWidth(),
                    )

                    Spacer(Modifier.height(SneakSpacing.xs))
                    Text(
                        text = "Role",
                        style = MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .selectableGroup(),
                    ) {
                        RowRadio(Role.BUYER, "Buyer", ui.role, vm::setRole)
                        RowRadio(Role.SELLER, "Seller", ui.role, vm::setRole)
                    }

                    ui.error?.let { msg -> SneakErrorBanner(message = msg) }

                    SneakPrimaryButton(
                        onClick = { vm.register(onRegistered) },
                        enabled = !ui.loading,
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        SneakPrimaryButtonContent(
                            loading = ui.loading,
                            loadingText = "Creating…",
                            idleText = "Create account",
                        )
                    }
                }
            }

            TextButton(
                onClick = onBack,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = SneakSpacing.sm),
                shape = MaterialTheme.shapes.large,
            ) {
                Text(
                    text = "Back to sign in",
                    style = MaterialTheme.typography.labelLarge,
                )
            }
        }
    }
}

@Composable
private fun RowRadio(
    role: Role,
    label: String,
    selected: Role,
    onSelect: (Role) -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 48.dp),
    ) {
        RadioButton(
            selected = selected == role,
            onClick = { onSelect(role) },
            colors = RadioButtonDefaults.colors(
                selectedColor = MaterialTheme.colorScheme.primary,
            ),
        )
        Text(
            text = label,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(start = SneakSpacing.xs),
        )
    }
}
