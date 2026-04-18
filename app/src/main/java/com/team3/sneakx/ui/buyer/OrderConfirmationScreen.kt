package com.team3.sneakx.ui.buyer

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.team3.sneakx.ui.components.SneakPrimaryButton
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
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Column(
            modifier = Modifier.widthIn(max = 520.dp),
            verticalArrangement = Arrangement.spacedBy(SneakSpacing.lg),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                "Order placed",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onBackground,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth(),
            )
            Text(
                "Thanks — your order is in.",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
            )
            Surface(
                shape = MaterialTheme.shapes.large,
                color = MaterialTheme.colorScheme.surface,
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
            ) {
                Text(
                    "Order ID: $orderId",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.padding(SneakSpacing.lg),
                )
            }
            Spacer(Modifier.height(SneakSpacing.sm))
            SneakPrimaryButton(
                onClick = {
                    if (!navController.popBackStack("browse", inclusive = false)) {
                        navController.navigate("browse") {
                            popUpTo(navController.graph.id) { inclusive = true }
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text("Back to marketplace", style = MaterialTheme.typography.labelLarge)
            }
        }
    }
}
