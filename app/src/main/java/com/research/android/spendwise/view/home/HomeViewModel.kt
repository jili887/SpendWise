package com.research.android.spendwise.view.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.research.android.spendwise.data.local.entity.TransactionEntity
import com.research.android.spendwise.data.repository.TransactionRepository
import com.research.android.spendwise.view.transaction.TransactionType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: TransactionRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(
        HomeUiState(
            isLoading = true
        )
    )

    val uiState: StateFlow<HomeUiState> = _uiState

    init {
        observeTransactions()

    }

    private fun observeTransactions() {
        viewModelScope.launch {
            repository.getTransactions()
                .catch { error ->
                    _uiState.value = HomeUiState(
                        isLoading = false,
                        errorMessage = error.message
                            ?: "Unable to load transactions"
                    )
                }
                .collect { transactions ->

                    val income = transactions
                        .filter { it.type == TransactionType.INCOME }
                        .sumOf { it.amount }

                    val expenses = transactions
                        .filter { it.type == TransactionType.EXPENSE }
                        .sumOf { it.amount }

                    val balance = income - expenses

                    val transactionUiModels =
                        transactions.map { transaction ->
                            transaction.toUiModel()
                        }

                    _uiState.value = HomeUiState(
                        balance = balance,
                        income = income,
                        expenses = expenses,
                        transactions = transactionUiModels,
                        isLoading = false,
                        errorMessage = null
                    )
                }
        }
    }

    fun retry() {
        _uiState.value = _uiState.value.copy(
            isLoading = true,
            errorMessage = null
        )
        observeTransactions()
    }

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
