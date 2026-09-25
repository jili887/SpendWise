package com.research.android.spendwise.view.transaction

sealed class TransactionFormMode {

    data object Add : TransactionFormMode()

    data class Edit(
        val transactionId: Long
    ) : TransactionFormMode()
}
