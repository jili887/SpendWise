package com.research.android.spendwise.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.research.android.spendwise.view.home.HomeScreen
import com.research.android.spendwise.view.transaction.AddTransactionScreen

sealed class SpendWiseRoute(val route: String) {
    data object Home : SpendWiseRoute("home")
    data object AddTransaction : SpendWiseRoute("add_transaction")
}

@Composable
fun SpendWiseNavHost() {

    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = SpendWiseRoute.Home.route
    ) {
        composable(SpendWiseRoute.Home.route) {

            HomeScreen(
                viewModel = hiltViewModel(),
                onAddTransactionClick = {
                    navController.navigate(
                        SpendWiseRoute.AddTransaction.route
                    )
                }
            )
        }

        composable(SpendWiseRoute.AddTransaction.route) {

            AddTransactionScreen(
                onSave = {
                    navController.popBackStack()
                },
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }
    }
}
