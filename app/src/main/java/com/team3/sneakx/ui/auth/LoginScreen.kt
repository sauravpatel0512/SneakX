package com.team3.sneakx.ui.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
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
import com.team3.sneakx.ui.SneakViewModelFactory
import com.team3.sneakx.ui.components.SneakErrorBanner
import com.team3.sneakx.ui.components.SneakPrimaryButton
import com.team3.sneakx.ui.components.SneakPrimaryButtonContent
import com.team3.sneakx.ui.components.sneakOutlinedTextFieldColors
import com.team3.sneakx.ui.theme.SneakFieldShape
import com.team3.sneakx.ui.theme.SneakSpacing
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun LoginScreen(
    onRegister: () -> Unit,
    onLoggedIn: () -> Unit
) {
    val container = LocalAppContainer.current
    val factory = SneakViewModelFactory(container)
    val vm: LoginViewModel = viewModel(factory = factory)
    val ui by vm.ui.collectAsState()

    val scroll = rememberScrollState()
    val hasError = ui.error != null || ui.lockedUntil != null

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
            text = "SneakX",
            style = MaterialTheme.typography.displaySmall,
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = TextAlign.Center,
        )
        Spacer(Modifier.height(SneakSpacing.md))
        Text(
            text = "Welcome back.",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth(),
        )
        Text(
            text = "Sign in to keep trading on campus.",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
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

                if (hasError) {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(SneakSpacing.sm),
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        ui.error?.let { SneakErrorBanner(message = it) }
                        ui.lockedUntil?.let { until ->
                            val fmt = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
                            SneakErrorBanner(message = "Try again after ${fmt.format(Date(until))}")
                        }
                    }
                }

                SneakPrimaryButton(
                    onClick = { vm.login(onLoggedIn) },
                    enabled = !ui.loading,
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    SneakPrimaryButtonContent(
                        loading = ui.loading,
                        loadingText = "Signing in…",
                        idleText = "Sign in",
                    )
                }
            }
        }

        TextButton(
            onClick = onRegister,
            modifier = Modifier.padding(top = SneakSpacing.sm),
            shape = MaterialTheme.shapes.large,
        ) {
            Text(
                text = "Create account",
                style = MaterialTheme.typography.labelLarge,
            )
        }
        }
    }
}
