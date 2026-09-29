package com.research.android.spendwise.view.transaction

data class TransactionValidationResult(
    val isValid: Boolean,
    val titleError: String?= null,
    val amountError: String? = null,
    val categoryError: String? = null,
    val dateError: String? = null,
    val noteError: String? = null
)
