package com.research.android.spendwise.view.transaction

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.research.android.spendwise.data.local.entity.TransactionEntity
import com.research.android.spendwise.data.repository.TransactionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TransactionFormViewModel @Inject constructor(
    private val repository: TransactionRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(TransactionFormUiState())
    val uiState: StateFlow<TransactionFormUiState> = _uiState.asStateFlow()
    private var currentMode: TransactionFormMode = TransactionFormMode.Add
    private var originalTransaction: TransactionEntity? = null

    fun initialize(mode: TransactionFormMode) {
        currentMode = mode
        when (mode) {
            TransactionFormMode.Add -> {
                originalTransaction = null
                _uiState.value =
                    TransactionFormUiState()
            }
            is TransactionFormMode.Edit -> {
                loadTransaction(
                    mode.transactionId
                )
            }
        }
    }

    private fun loadTransaction(
        transactionId: Long
    ) {
        viewModelScope.launch {
            _uiState.update {
                it.copy(isLoading = true, errorMessage = null)
            }

            try {
                val transaction =
                    repository.getTransactionById(transactionId)

                if (transaction == null) {
                    _uiState.update {
                        it.copy(isLoading = false, errorMessage = "Transaction not found.")
                    }
                    return@launch
                }

                originalTransaction =
                    transaction

                _uiState.update {
                    it.copy(
                        type =
                            if (transaction.isIncome) {
                                TransactionType.INCOME
                            } else {
                                TransactionType.EXPENSE
                            },
                        amount = transaction.amount.toString(),
                        category = transaction.category,
                        date = transaction.date.toString(),
                        note = transaction.note.orEmpty(),
                        isLoading = false,
                        errorMessage = null
                    )
                }

            } catch (error: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = error.message ?: "Unable to load transaction."
                    )
                }
            }
        }
    }

    fun onTypeChanged(type: TransactionType) {
        _uiState.update { it.copy(type = type) }
    }

    fun onAmountChanged(amount: String) {
        _uiState.update { it.copy(amount = amount, amountError = null) }
    }

    fun onCategoryChanged(category: String) {
        _uiState.update { it.copy(category = category, categoryError = null) }
    }

    fun onDateChanged(date: String) {
        _uiState.update { it.copy(date = date) }
    }

    fun onNoteChanged(note: String) {
        _uiState.update { it.copy(note = note) }
    }

    fun saveTransaction() {
        val state = _uiState.value
        val amount = state.amount.toDoubleOrNull()
        if (amount == null || amount <= 0) {
            _uiState.update {
                it.copy(amountError = "Enter a valid amount greater than 0.")
            }
            return
        }
        if (state.category.isBlank()) {
            _uiState.update { it.copy(categoryError = "Category is required.")
            }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isSaving = true, errorMessage = null) }
            try {
                when (val mode = currentMode) {
                    TransactionFormMode.Add -> {
                        val transaction =
                            TransactionEntity(
                                title = state.category,
                                amount = amount,
                                type = if (state.type == TransactionType.INCOME) TransactionType.INCOME else TransactionType.EXPENSE,
                                category = state.category,
                                isIncome = state.type == TransactionType.INCOME,
                                date = System.currentTimeMillis(),
                                note = state.note.ifBlank { null }
                            )
                        repository.insertTransaction(
                            transaction
                        )
                    }

                    is TransactionFormMode.Edit -> {
                        val original = originalTransaction
                        if (original == null) {
                            _uiState.update {
                                it.copy(
                                    isSaving = false,
                                    errorMessage =
                                        "Transaction not found."
                                )
                            }
                            return@launch
                        }
                        val updatedTransaction =
                            original.copy(
                                title = state.category,
                                amount = amount,
                                type = if (state.type == TransactionType.INCOME) TransactionType.INCOME else TransactionType.EXPENSE,
                                category = state.category,
                                isIncome = state.type == TransactionType.INCOME,
                                date = System.currentTimeMillis(),
                                note = state.note.ifBlank { null }
                            )
                        repository.updateTransaction(
                            updatedTransaction
                        )
                    }
                }
                _uiState.update {
                    it.copy(isSaving = false, isSaved = true)
                }
            } catch (error: Exception) {
                _uiState.update {
                    it.copy(
                        isSaving = false,
                        errorMessage =
                            error.message
                                ?: "Unable to save transaction."
                    )
                }
            }
        }
    }

    fun clearSavedState() {
        _uiState.update { it.copy(isSaved = false) }
    }

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
