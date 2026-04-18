package com.team3.sneakx.ui.buyer

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.team3.sneakx.ui.components.SneakScreenTitle
import com.team3.sneakx.ui.theme.SneakSpacing

@Composable
fun OrderConfirmationScreen(
    navController: NavHostController,
    orderId: String
) {
    Column(
        Modifier
            .fillMaxSize()
            .padding(horizontal = SneakSpacing.screenPadding)
            .padding(top = SneakSpacing.xxl, bottom = SneakSpacing.xl),
        verticalArrangement = Arrangement.spacedBy(SneakSpacing.lg),
    ) {
        Column(
            modifier = Modifier.widthIn(max = 520.dp),
            verticalArrangement = Arrangement.spacedBy(SneakSpacing.md),
        ) {
            SneakScreenTitle("Order placed", Modifier.fillMaxWidth())
            Surface(
                shape = MaterialTheme.shapes.large,
                color = MaterialTheme.colorScheme.surfaceContainerLow,
            ) {
                Text(
                    "Order ID: $orderId",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.padding(SneakSpacing.lg),
                )
            }
            Spacer(Modifier.padding(SneakSpacing.sm))
            Button(
                onClick = {
                    if (!navController.popBackStack("browse", inclusive = false)) {
                        navController.navigate("browse") {
                            popUpTo(navController.graph.id) { inclusive = true }
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.large,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                ),
            ) {
                Text("Back to marketplace")
            }
        }
    }
}
