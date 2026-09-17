package com.research.android.spendwise.view.transaction

import androidx.lifecycle.ViewModel
import com.research.android.spendwise.data.repository.TransactionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TransactionViewModel @Inject constructor(
    private val repository: TransactionRepository
) : ViewModel() {

    val transactions = repository.observeTransactions()
}