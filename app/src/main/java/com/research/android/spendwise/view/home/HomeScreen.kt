package com.research.android.spendwise.view.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.research.android.spendwise.view.common.EmptyState
import com.research.android.spendwise.view.common.ErrorState
import com.research.android.spendwise.view.common.LoadingState

@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    onAddTransactionClick: () -> Unit,
    onStatisticsClick: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    when {
        uiState.isLoading ->
            LoadingState()


        uiState.errorMessage != null -> {
            ErrorState(
                message = uiState.errorMessage!!,
                onRetry = viewModel::retry
            )
        }

        uiState.transactions.isEmpty() -> {
            EmptyState(
                title = "No transactions yet",
                message = "Add your first transaction to get started."
            )
        }

        else -> {
            HomeContent(
                uiState = uiState,
                onAddTransactionClick = onAddTransactionClick,
                onStatisticsClick = onStatisticsClick
            )
        }
    }
}

@Composable
private fun HomeContent(
    uiState: HomeUiState,
    onAddTransactionClick: () -> Unit,
    onStatisticsClick: () -> Unit
) {
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddTransactionClick
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add transaction"
                )
            }
        }
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {

            Text(
                text = "SpendWise",
                style = MaterialTheme.typography.headlineMedium
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "September 2026",
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(modifier = Modifier.height(16.dp))

            BalanceCard(
                balance = uiState.balance,
                income = uiState.income,
                expenses = uiState.expenses
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Recent Transactions",
                style = MaterialTheme.typography.titleLarge
            )

            Spacer(modifier = Modifier.height(8.dp))

            uiState.transactions.forEach { transaction ->
                TransactionItem(
                    transaction = transaction
                )
            }

            Button(
                onClick = onStatisticsClick
            ) {
                Text("Statistics")
            }
        }
    }
}

@Composable
private fun BalanceCard(
    balance: Double,
    income: Double,
    expenses: Double
) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Text(
                text = "Total Balance",
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "$${"%,.2f".format(balance)}",
                style = MaterialTheme.typography.headlineLarge
            )

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text("Income")
                    Text("$${"%,.2f".format(income)}")
                }

                Column {
                    Text("Expenses")
                    Text("$${"%,.2f".format(expenses)}")
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun HomeScreenPreview() {
//    MaterialTheme {
//        HomeScreen(
//            viewModel = HomeViewModel(),
//            onAddTransactionClick = {}
//        )
//    }
}
