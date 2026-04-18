package com.team3.sneakx.ui.buyer

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.weight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.team3.sneakx.LocalAppContainer
import com.team3.sneakx.payment.MockPaymentGateway

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
        topBar = {
            TopAppBar(
                title = { Text("Checkout") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            OutlinedTextField(
                value = ui.shipping,
                onValueChange = vm::setShipping,
                label = { Text("Shipping address / info") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 3
            )
            Spacer(Modifier.padding(8.dp))
            Text("Payment method", style = MaterialTheme.typography.labelLarge)
            val methods = listOf("Mock card", "Mock PayPal", "Mock wallet")
            methods.forEach { m ->
                Row(
                    verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
                ) {
                    RadioButton(selected = ui.paymentMethod == m, onClick = { vm.setPaymentMethod(m) })
                    Text(m)
                }
            }
            Spacer(Modifier.padding(8.dp))
            Row(
                verticalAlignment = androidx.compose.ui.Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Simulate payment failure (demo)")
                Spacer(Modifier.weight(1f))
                Switch(
                    checked = simulateFail,
                    onCheckedChange = {
                        simulateFail = it
                        MockPaymentGateway.forceNextFailure = it
                    }
                )
            }
            ui.error?.let {
                Text(it, color = MaterialTheme.colorScheme.error)
                Spacer(Modifier.padding(4.dp))
            }
            ui.paymentFailedMessage?.let {
                Text(it, color = MaterialTheme.colorScheme.error)
                Spacer(Modifier.padding(4.dp))
            }
            Button(
                onClick = { vm.placeOrder() },
                enabled = !ui.loading,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(if (ui.loading) "Processing…" else "Place order")
            }
        }
    }
}
