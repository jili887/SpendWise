package com.research.android.spendwise.view.transaction

data class TransactionFormUiState(
    val type: TransactionType = TransactionType.EXPENSE,
    val amount: String = "",
    val category: String = "",
    val date: String = "",
    val note: String = "",

    val isLoading: Boolean = false,
    val isSaving: Boolean = false,

    val errorMessage: String? = null,

    val amountError: String? = null,
    val categoryError: String? = null,

    val isSaved: Boolean = false
)
