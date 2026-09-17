package com.research.android.spendwise.view.home

import com.research.android.spendwise.view.transaction.TransactionType

data class TransactionUiModel(
    val id: Long,
    val title: String,
    val amount: Double,
    val type: TransactionType,
    val category: String,
    val isIncome: Boolean,
    val date: Long,
    val note: String?
)
