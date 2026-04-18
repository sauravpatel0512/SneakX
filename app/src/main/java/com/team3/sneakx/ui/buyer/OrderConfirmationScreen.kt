package com.team3.sneakx.ui.buyer

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController

@Composable
fun OrderConfirmationScreen(
    navController: NavHostController,
    orderId: String
) {
    Column(Modifier.fillMaxSize().padding(24.dp)) {
        Text("Order placed", style = MaterialTheme.typography.headlineMedium)
        Spacer(Modifier.padding(8.dp))
        Text("Order ID: $orderId", style = MaterialTheme.typography.bodyLarge)
        Spacer(Modifier.padding(16.dp))
        Button(onClick = {
            if (!navController.popBackStack("browse", inclusive = false)) {
                navController.navigate("browse") {
                    popUpTo(navController.graph.id) { inclusive = true }
                }
            }
        }) {
            Text("Back to marketplace")
        }
    }
}
