package com.research.android.spendwise.view.home

import androidx.lifecycle.ViewModel
import com.research.android.spendwise.data.repository.TransactionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: TransactionRepository
): ViewModel() {
    private val _uiState = MutableStateFlow(
        HomeUiState(
            balance = 4280.00,
            income = 6500.00,
            expenses = 2220.00,
            transactions = listOf(
                TransactionUiModel(
                    id = 1,
                    title = "Groceries",
                    amount = 85.40,
                    category = "Food",
                    isIncome = false
                ),
                TransactionUiModel(
                    id = 2,
                    title = "Restaurant",
                    amount = 32.50,
                    category = "Food",
                    isIncome = false
                ),
                TransactionUiModel(
                    id = 3,
                    title = "Salary",
                    amount = 6500.00,
                    category = "Income",
                    isIncome = true
                ),
                TransactionUiModel(
                    id = 4,
                    title = "Transportation",
                    amount = 45.00,
                    category = "Transport",
                    isIncome = false
                )
            )
        )
    )

    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()
}
