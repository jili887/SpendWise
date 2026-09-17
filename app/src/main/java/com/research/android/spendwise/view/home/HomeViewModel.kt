package com.research.android.spendwise.view.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.research.android.spendwise.data.local.entity.TransactionEntity
import com.research.android.spendwise.data.repository.TransactionRepository
import com.research.android.spendwise.view.transaction.TransactionType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: TransactionRepository
): ViewModel() {
    private val _uiState: StateFlow<HomeUiState> =
        repository.getTransactions()
            .map { transactions ->

                val income = transactions
                    .filter { it.type == TransactionType.INCOME }
                    .sumOf { it.amount }

                val expenses = transactions
                    .filter { it.type == TransactionType.EXPENSE }
                    .sumOf { it.amount }

                val recentTransactions = transactions
                    .take(5)
                    .map { it.toUiModel() }

                HomeUiState(
                    balance = income - expenses,
                    income = income,
                    expenses = expenses,
                    transactions = recentTransactions
                )
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = HomeUiState()
            )

    val uiState: StateFlow<HomeUiState> = _uiState

    fun TransactionEntity.toUiModel(): TransactionUiModel {
        return TransactionUiModel(
            id = id,
            title = title,
            amount = amount,
            type = type,
            category = category,
            isIncome = isIncome,
            date = date,
            note = note
        )
    }
}
