package com.research.android.spendwise.view.transaction

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.research.android.spendwise.data.local.entity.TransactionEntity
import com.research.android.spendwise.data.repository.TransactionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TransactionViewModel @Inject constructor(
    private val repository: TransactionRepository
) : ViewModel() {

    fun addTransaction(
        title: String,
        amount: Double,
        category: String,
        isIncome: Boolean,
        date: Long,
        note: String?
    ) {
        viewModelScope.launch {
            repository.insertTransaction(
                TransactionEntity(
                    title = title,
                    amount = amount,
                    type = if (isIncome) TransactionType.INCOME else TransactionType.EXPENSE,
                    category = category,
                    isIncome = isIncome,
                    date = date,
                    note = note
                )
            )
        }
    }
}
