package com.research.android.spendwise.view.statistics

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.research.android.spendwise.view.common.EmptyState
import com.research.android.spendwise.view.common.ErrorState
import com.research.android.spendwise.view.common.LoadingState

@Composable
fun StatisticsScreen(
    viewModel: StatisticsViewModel
) {

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    when {
        uiState.isLoading -> {
            LoadingState()
        }

        uiState.errorMessage != null -> {
            ErrorState(
                message = uiState.errorMessage!!,
                onRetry = viewModel::retry

            )
        }

        uiState.expensesByCategory.isEmpty() &&
                uiState.income == 0.0 &&
                uiState.expenses == 0.0 -> {

            EmptyState(
                title = "No data yet",
                message = "Add some transactions to see your statistics."
            )
        }

        else -> {
            StatisticsContent(
                uiState = uiState,
                viewModel = viewModel
            )
        }
    }
}

@Composable
private fun StatisticsContent(
    uiState: StatisticsUiState,
    viewModel: StatisticsViewModel
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        item {
            Text(
                text = "Statistics",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )
        }

        item {
            StatisticsFilterDropdown(
                selectedFilter = uiState.selectedFilter,
                onFilterSelected = viewModel::selectFilter
            )
        }

        item {
            SummaryCard(
                title = "Income",
                amount = uiState.income
            )
        }

        item {
            SummaryCard(
                title = "Expenses",
                amount = uiState.expenses
            )
        }

        item {
            SummaryCard(
                title = "Net Balance",
                amount = uiState.balance
            )
        }

        item {
            Text(
                text = "Expenses by Category",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
        }

        items(
            items = uiState.expensesByCategory.toList()
        ) { (category, amount) ->

            CategoryExpenseItem(
                category = category,
                amount = amount
            )
        }
    }
}

@Composable
private fun SummaryCard(
    title: String,
    amount: Double
) {

    Card(
        modifier = Modifier.fillMaxWidth()
    ) {

        Column(
            modifier = Modifier.padding(16.dp)
        ) {

            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(
                modifier = Modifier.height(8.dp)
            )

            Text(
                text = "$${"%,.2f".format(amount)}",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun CategoryExpenseItem(
    category: String,
    amount: Double
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Text(
            text = category,
            modifier = Modifier.weight(1f),
            style = MaterialTheme.typography.bodyLarge
        )

        Text(
            text = "$${"%,.2f".format(amount)}",
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
private fun StatisticsFilterDropdown(
    selectedFilter: StatisticsFilter,
    onFilterSelected: (StatisticsFilter) -> Unit
) {

    var expanded by remember {
        mutableStateOf(false)
    }

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.End,
        verticalAlignment = Alignment.CenterVertically
    ) {

        Card {

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {

                Text(
                    text = selectedFilter.label,
                    modifier = Modifier.padding(
                        start = 16.dp
                    )
                )

                IconButton(
                    onClick = {
                        expanded = true
                    }
                ) {

                    Icon(
                        imageVector = Icons.Default.ArrowDropDown,
                        contentDescription = "Select date range"
                    )
                }

                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = {
                        expanded = false
                    }
                ) {

                    StatisticsFilter.entries.forEach { filter ->

                        DropdownMenuItem(
                            text = {
                                Text(filter.label)
                            },
                            onClick = {

                                onFilterSelected(filter)

                                expanded = false
                            }
                        )
                    }
                }
            }
        }
    }
}
