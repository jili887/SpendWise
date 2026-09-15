package com.research.android.spendwise.view.home

data class TransactionUiModel(
    val id: Long,
    val title: String,
    val amount: Double,
    val category: String,
    val isIncome: Boolean
)
