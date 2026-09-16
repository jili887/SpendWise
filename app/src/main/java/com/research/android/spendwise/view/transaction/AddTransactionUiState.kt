package com.research.android.spendwise.view.transaction

data class AddTransactionUiState(
    val type: TransactionType = TransactionType.EXPENSE,
    val amount: String = "",
    val category: String = "",
    val date: String = "",
    val note: String = ""
)
