package com.team3.sneakx.ui.buyer

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import com.team3.sneakx.payment.MockPaymentGateway
import com.team3.sneakx.ui.components.SneakErrorBanner
import com.team3.sneakx.ui.components.SneakPrimaryButton
import com.team3.sneakx.ui.components.SneakPrimaryButtonContent
import com.team3.sneakx.ui.components.SneakSectionLabel
import com.team3.sneakx.ui.components.SneakTopBarBack
import com.team3.sneakx.ui.components.sneakOutlinedTextFieldColors
import com.team3.sneakx.ui.theme.SneakFieldShape
import com.team3.sneakx.ui.theme.SneakSpacing

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CheckoutScreen(
    navController: NavHostController,
    buyerId: String
) {
    val container = LocalAppContainer.current
    val vm: CheckoutViewModel = viewModel(
        key = buyerId,
        factory = object : androidx.lifecycle.ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
                require(modelClass.isAssignableFrom(CheckoutViewModel::class.java))
                return CheckoutViewModel(buyerId, container.orderRepository) as T
            }
        }
    )
    val ui by vm.ui.collectAsState()
    var simulateFail by remember { mutableStateOf(false) }

    LaunchedEffect(ui.successOrderId) {
        val id = ui.successOrderId ?: return@LaunchedEffect
        navController.navigate("order_confirmation/$id") {
            popUpTo("checkout") { inclusive = true }
        }
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
    ) { padding ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(padding),
        ) {
            SneakTopBarBack(
                eyebrow = "Checkout",
                title = "Order",
                onBack = { navController.popBackStack() },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = SneakSpacing.screenPadding),
            )
            Column(
                Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = SneakSpacing.screenPadding)
                    .padding(top = SneakSpacing.md, bottom = SneakSpacing.xl),
                verticalArrangement = Arrangement.spacedBy(SneakSpacing.md),
            ) {
                OutlinedTextField(
                    value = ui.shipping,
                    onValueChange = vm::setShipping,
                    label = { Text("Shipping address / info") },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 3,
                    shape = SneakFieldShape,
                    colors = sneakOutlinedTextFieldColors(),
                )
                SneakSectionLabel("Payment method")
                val methods = listOf("Mock card", "Mock PayPal", "Mock wallet")
                Column(Modifier.selectableGroup()) {
                    methods.forEach { m ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = SneakSpacing.xs),
                        ) {
                            RadioButton(
                                selected = ui.paymentMethod == m,
                                onClick = { vm.setPaymentMethod(m) },
                                colors = RadioButtonDefaults.colors(
                                    selectedColor = MaterialTheme.colorScheme.primary,
                                ),
                            )
                            Text(
                                m,
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier.padding(start = SneakSpacing.sm),
                            )
                        }
                    }
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Text(
                        "Simulate payment failure (demo)",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                    )
                    Spacer(Modifier.weight(1f))
                    Switch(
                        checked = simulateFail,
                        onCheckedChange = {
                            simulateFail = it
                            MockPaymentGateway.forceNextFailure = it
                        },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = MaterialTheme.colorScheme.primary,
                            checkedTrackColor = MaterialTheme.colorScheme.primaryContainer,
                        ),
                    )
                }
                ui.error?.let { SneakErrorBanner(message = it) }
                ui.paymentFailedMessage?.let { SneakErrorBanner(message = it) }
                SneakPrimaryButton(
                    onClick = { vm.placeOrder() },
                    enabled = !ui.loading,
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    SneakPrimaryButtonContent(
                        loading = ui.loading,
                        loadingText = "Processing…",
                        idleText = "Place order",
                    )
                }
            }
        }
    }
}
