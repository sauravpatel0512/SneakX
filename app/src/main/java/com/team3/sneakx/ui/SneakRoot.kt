package com.team3.sneakx.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.outlined.Inventory2
import androidx.compose.material.icons.outlined.People
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.team3.sneakx.LocalAppContainer
import com.team3.sneakx.data.session.Session
import com.team3.sneakx.domain.Role
import com.team3.sneakx.ui.admin.AdminListingsScreen
import com.team3.sneakx.ui.admin.AdminUsersScreen
import com.team3.sneakx.ui.auth.LoginScreen
import com.team3.sneakx.ui.auth.RegisterScreen
import com.team3.sneakx.ui.buyer.BrowseScreen
import com.team3.sneakx.ui.buyer.CartScreen
import com.team3.sneakx.ui.buyer.CheckoutScreen
import com.team3.sneakx.ui.buyer.ListingDetailScreen
import com.team3.sneakx.ui.buyer.OrderConfirmationScreen
import com.team3.sneakx.ui.profile.ProfileScreen
import com.team3.sneakx.ui.seller.ListingEditScreen
import com.team3.sneakx.ui.seller.MyListingsScreen
import kotlinx.coroutines.flow.first

private fun showBottomBar(route: String?): Boolean {
    if (route == null) return false
    return route == "browse" || route == "cart" || route == "my_listings" ||
        route == "admin_users" || route == "admin_listings" || route == "profile"
}

@Composable
fun SneakRoot() {
    val container = LocalAppContainer.current
    var startReady by remember { mutableStateOf(false) }
    var startRoute by remember { mutableStateOf<String?>(null) }
    LaunchedEffect(Unit) {
        val s = container.sessionStore.session.first()
        startRoute = if (s.userId != null) "browse" else "login"
        startReady = true
    }
    val navController = rememberNavController()
    val session by container.sessionStore.session.collectAsState(
        initial = Session(null, null, null)
    )

    if (!startReady || startRoute == null) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val role = session.role

    Scaffold(
        bottomBar = {
            if (session.userId != null && role != null && showBottomBar(currentRoute)) {
                BottomBar(navController, role, currentRoute ?: "")
            }
        }
    ) { padding ->
        NavHost(
            navController = navController,
            startDestination = startRoute!!,
            modifier = Modifier.padding(padding)
        ) {
            composable("login") {
                LoginScreen(
                    onRegister = { navController.navigate("register") },
                    onLoggedIn = {
                        navController.navigate("browse") {
                            popUpTo("login") { inclusive = true }
                        }
                    }
                )
            }
            composable("register") {
                RegisterScreen(
                    onRegistered = { navController.popBackStack() },
                    onBack = { navController.popBackStack() }
                )
            }
            composable("browse") {
                BrowseScreen(navController)
            }
            composable(
                "listing_detail/{listingId}",
                arguments = listOf(navArgument("listingId") { type = NavType.StringType })
            ) { entry ->
                val id = entry.arguments?.getString("listingId") ?: return@composable
                ListingDetailScreen(navController, id, session)
            }
            composable("cart") {
                val uid = session.userId ?: return@composable
                CartScreen(navController, uid)
            }
            composable("checkout") {
                val uid = session.userId ?: return@composable
                CheckoutScreen(navController, uid)
            }
            composable(
                "order_confirmation/{orderId}",
                arguments = listOf(navArgument("orderId") { type = NavType.StringType })
            ) { entry ->
                val oid = entry.arguments?.getString("orderId") ?: return@composable
                OrderConfirmationScreen(navController, oid)
            }
            composable("my_listings") {
                val uid = session.userId ?: return@composable
                MyListingsScreen(navController, uid)
            }
            composable(
                "listing_edit/{listingId}",
                arguments = listOf(navArgument("listingId") { type = NavType.StringType })
            ) { entry ->
                val lid = entry.arguments?.getString("listingId") ?: return@composable
                val uid = session.userId ?: return@composable
                ListingEditScreen(navController, uid, lid)
            }
            composable("admin_users") {
                val uid = session.userId ?: return@composable
                AdminUsersScreen(uid)
            }
            composable("admin_listings") {
                val uid = session.userId ?: return@composable
                AdminListingsScreen(uid)
            }
            composable("profile") {
                val uid = session.userId ?: return@composable
                val r = role ?: return@composable
                ProfileScreen(navController, uid, r)
            }
        }
    }
}

@Composable
private fun BottomBar(
    navController: NavHostController,
    role: Role,
    currentRoute: String
) {
    val items: List<Triple<String, String, ImageVector>> = when (role) {
        Role.BUYER -> listOf(
            Triple("browse", "Home", Icons.Default.Home),
            Triple("cart", "Cart", Icons.Default.ShoppingCart),
            Triple("profile", "Profile", Icons.Default.Person)
        )
        Role.SELLER -> listOf(
            Triple("browse", "Home", Icons.Default.Home),
            Triple("my_listings", "My listings", Icons.Outlined.Inventory2),
            Triple("profile", "Profile", Icons.Default.Person)
        )
        Role.ADMIN -> listOf(
            Triple("browse", "Home", Icons.Default.Home),
            Triple("admin_users", "Users", Icons.Outlined.People),
            Triple("admin_listings", "Listings", Icons.Outlined.Inventory2),
            Triple("profile", "Profile", Icons.Default.Person)
        )
    }
    NavigationBar {
        items.forEach { (route, label, icon) ->
            val selected = currentRoute == route
            NavigationBarItem(
                selected = selected,
                onClick = {
                    navController.navigate(route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                icon = { Icon(icon, contentDescription = label) },
                label = { Text(label) }
            )
        }
    }
}
