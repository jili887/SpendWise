package com.research.android.spendwise.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.research.android.spendwise.view.home.HomeScreen
import com.research.android.spendwise.view.statistics.StatisticsScreen
import com.research.android.spendwise.view.transaction.TransactionFormMode
import com.research.android.spendwise.view.transaction.TransactionFormScreen

sealed class SpendWiseRoute(val route: String) {
    data object Home : SpendWiseRoute("home")
    data object AddTransaction : SpendWiseRoute("add_transaction")
    data object Statistics : SpendWiseRoute("statistics")
    data object EditTransaction : SpendWiseRoute("edit_transaction/{transactionId}") {
        fun createRoute(transactionId: Long): String {
            return "edit_transaction/$transactionId"
        }
    }
}

data class BottomNavItem(
    val route: String,
    val label: String,
    val icon: ImageVector
)

val bottomNavItems = listOf(
    BottomNavItem(
        route = SpendWiseRoute.Home.route,
        label = "Home",
        icon = Icons.Default.Home
    ),
    BottomNavItem(
        route = SpendWiseRoute.Statistics.route,
        label = "Statistics",
        icon = Icons.Default.BarChart
    ),
    BottomNavItem(
        route = SpendWiseRoute.AddTransaction.route,
        label = "Add",
        icon = Icons.Default.Add
    )
)

@Composable
fun SpendWiseNavHost() {

    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Scaffold(
        bottomBar = {
            if (
                currentRoute == SpendWiseRoute.Home.route ||
                currentRoute == SpendWiseRoute.Statistics.route
            ) {
                NavigationBar {
                    bottomNavItems.forEach { item ->
                        NavigationBarItem(
                            selected = currentRoute == item.route,
                            onClick = {
                                navController.navigate(item.route) {
                                    popUpTo(navController.graph.startDestinationId) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                            icon = {
                                Icon(
                                    imageVector = item.icon,
                                    contentDescription = item.label
                                )
                            },
                            label = {
                                Text(text = item.label)
                            }
                        )
                    }
                }
            }
        }
    ) { innerPadding ->

        NavHost(
            navController = navController,
            startDestination = SpendWiseRoute.Home.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(SpendWiseRoute.Home.route) {

                HomeScreen(
                    viewModel = hiltViewModel(),
                    onAddTransactionClick = {
                        navController.navigate(
                            SpendWiseRoute.AddTransaction.route
                        )
                    },
                    onStatisticsClick = {
                        navController.navigate(
                            SpendWiseRoute.Statistics.route
                        )
                    },
                    onEditTransactionClick = { transactionId ->
                        navController.navigate(
                            SpendWiseRoute.EditTransaction.createRoute(transactionId)
                        )
                    }
                )
            }

            composable(SpendWiseRoute.AddTransaction.route) {

                TransactionFormScreen(
                    mode = TransactionFormMode.Add,
                    viewModel = hiltViewModel(),
                    onSaved = {
                        navController.popBackStack()
                    },
                    onBackClick = {
                        navController.popBackStack()
                    }
                )
            }

            composable(SpendWiseRoute.Statistics.route) {
                StatisticsScreen(
                    viewModel = hiltViewModel(),
                    onAddTransactionClick = {
                        navController.navigate(
                            SpendWiseRoute.AddTransaction.route
                        )
                    }
                )
            }

            composable(SpendWiseRoute.EditTransaction.route) { backStackEntry ->
                val transactionId =
                    backStackEntry.arguments
                        ?.getString("transactionId")
                        ?.toLongOrNull()
                if (transactionId != null) {
                    TransactionFormScreen(
                        mode = TransactionFormMode.Edit(transactionId = transactionId),
                        viewModel = hiltViewModel(),
                        onSaved = {
                            navController.popBackStack()
                        },
                        onBackClick = {
                            navController.popBackStack()
                        }
                    )
                }
            }
        }
    }
}
