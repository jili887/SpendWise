package com.research.android.spendwise.view.home

data class HomeUiState(
    val balance: Double = 0.0,
    val income: Double = 0.0,
    val expenses: Double = 0.0,
    val transactions: List<TransactionUiModel> = emptyList(),
    val isLoading: Boolean = false
)
