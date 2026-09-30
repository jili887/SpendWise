package com.research.android.spendwise.view.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.research.android.spendwise.R
import com.research.android.spendwise.ui.theme.SpendWiseTheme
import com.research.android.spendwise.ui.theme.spacing
import com.research.android.spendwise.util.getCurrentMonthLabel
import com.research.android.spendwise.view.common.EmptyState
import com.research.android.spendwise.view.common.ErrorState
import com.research.android.spendwise.view.common.LoadingState
import com.research.android.spendwise.view.common.previewHomeViewModel

@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    onAddTransactionClick: () -> Unit,
    onStatisticsClick: () -> Unit,
    onEditTransactionClick: (Long) -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    /*
    * Stores the transaction that the user wants to delete.
    *
    * null = no delete dialog
    * non-null = show delete confirmation dialog
    */
    var transactionToDelete by remember {
        mutableStateOf<TransactionUiModel?>(null)
    }

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
                title = stringResource(R.string.no_transactions_yet),
                message = stringResource(R.string.no_transactions_message),
                actionLabel = stringResource(R.string.add_transaction),
                onActionClick = onAddTransactionClick
            )
        }

        else -> {
            HomeContent(
                uiState = uiState,
                onAddTransactionClick = onAddTransactionClick,
                onStatisticsClick = onStatisticsClick,
                onDeleteTransactionClick = { transaction ->
                    transactionToDelete = transaction
                },
                onEditTransactionClick = onEditTransactionClick
            )
        }
    }
    transactionToDelete?.let { transaction ->

            AlertDialog(
                onDismissRequest = {
                    transactionToDelete = null
                },

                title = {
                    Text(stringResource(R.string.delete_transaction))
                },

                text = {
                    Text(
                        stringResource(
                            R.string.delete_transaction_message,
                            transaction.title
                        )
                    )
                },

                confirmButton = {
                    TextButton(
                        onClick = {
                            viewModel.deleteTransaction(
                                transaction.id
                            )
                            transactionToDelete = null
                        }
                    ) {
                        Text(stringResource(R.string.delete))
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = {
                            transactionToDelete = null
                        }
                    ) {
                        Text(stringResource(R.string.cancel))
                    }
                }
            )
        }
}

@Composable
private fun HomeContent(
    uiState: HomeUiState,
    onAddTransactionClick: () -> Unit,
    onStatisticsClick: () -> Unit,
    onDeleteTransactionClick: (TransactionUiModel) -> Unit,
    onEditTransactionClick: (Long) -> Unit
) {
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddTransactionClick
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = stringResource(R.string.add_transaction)
                )
            }
        }
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(MaterialTheme.spacing.lg)
        ) {

            Text(
                text = stringResource(R.string.app_name),
                style = MaterialTheme.typography.headlineMedium
            )

            Spacer(modifier = Modifier.height(MaterialTheme.spacing.xxl))

            Text(
                text = getCurrentMonthLabel(),
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(modifier = Modifier.height(MaterialTheme.spacing.lg))

            BalanceCard(
                balance = uiState.balance,
                income = uiState.income,
                expenses = uiState.expenses
            )

            Spacer(modifier = Modifier.height(MaterialTheme.spacing.xxl))

            Text(
                text = stringResource(R.string.recent_transactions),
                style = MaterialTheme.typography.titleLarge
            )

            Spacer(modifier = Modifier.height(MaterialTheme.spacing.sm))

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(MaterialTheme.spacing.lg),
                verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.sm)
            ) {
                items(
                    items = uiState.transactions,
                    key = { it.id }
                ) { transaction ->
                    TransactionItem(
                        transaction = transaction,
                        onEditClick = { transactionId ->
                            onEditTransactionClick(transactionId)
                        },
                        onDeleteClick = {
                            onDeleteTransactionClick(transaction)
                        }
                    )
                }
            }

            Button(
                onClick = onStatisticsClick
            ) {
                Text(stringResource(R.string.statistics))
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
            modifier = Modifier.padding(MaterialTheme.spacing.xl)
        ) {
            Text(
                text = stringResource(R.string.total_balance),
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(modifier = Modifier.height(MaterialTheme.spacing.sm))

            Text(
                text = "$${"%,.2f".format(balance)}",
                style = MaterialTheme.typography.headlineLarge
            )

            Spacer(modifier = Modifier.height(MaterialTheme.spacing.xxl))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(stringResource(R.string.income))
                    Text("$${"%,.2f".format(income)}")
                }

                Column {
                    Text(stringResource(R.string.expenses))
                    Text("$${"%,.2f".format(expenses)}")
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun HomeScreenPreview() {
    SpendWiseTheme {
        HomeScreen(
            viewModel = previewHomeViewModel(),
            onAddTransactionClick = {},
            onStatisticsClick = {},
            onEditTransactionClick = {}
        )
    }
}
