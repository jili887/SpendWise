package com.research.android.spendwise.view.statistics

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.research.android.spendwise.data.local.entity.TransactionEntity
import com.research.android.spendwise.data.repository.TransactionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import java.util.Calendar
import javax.inject.Inject

@HiltViewModel
class StatisticsViewModel @Inject constructor(
    private val repository: TransactionRepository
) : ViewModel() {

    private val selectedFilter =
        MutableStateFlow(StatisticsFilter.THIS_MONTH)

    private val _uiState = MutableStateFlow(
        StatisticsUiState(isLoading = true)
    )

    val uiState: StateFlow<StatisticsUiState> =
        _uiState.asStateFlow()

    init {
        observeStatistics()
    }

    private fun observeStatistics() {
        viewModelScope.launch {
            combine(
                repository.getTransactions(),
                selectedFilter
            ) { transactions, filter ->

                val filteredTransactions =
                    filterTransactions(transactions, filter)

                calculateStatistics(
                    filteredTransactions,
                    filter
                )

            }
                .catch { error ->
                    _uiState.value = StatisticsUiState(
                        isLoading = false,
                        errorMessage = error.message
                    )
                }
                .collect { state ->
                    _uiState.value = state
                }
        }
    }

    fun selectFilter(filter: StatisticsFilter) {
        selectedFilter.value = filter
    }

    private fun calculateStatistics(
        transactions: List<TransactionEntity>,
        filter: StatisticsFilter
    ): StatisticsUiState {

        val income = transactions
            .filter { it.isIncome }
            .sumOf { it.amount }

        val expenses = transactions
            .filter { !it.isIncome }
            .sumOf { it.amount }

        val expensesByCategory = transactions
            .filter { !it.isIncome }
            .groupBy { it.category }
            .mapValues { (_, categoryTransactions) ->
                categoryTransactions.sumOf { it.amount }
            }
            .toList()
            .sortedByDescending { it.second }
            .toMap()

        return StatisticsUiState(
            income = income,
            expenses = expenses,
            balance = income - expenses,
            expensesByCategory = expensesByCategory,
            selectedFilter = filter,
            isLoading = false
        )
    }

    private fun filterTransactions(
        transactions: List<TransactionEntity>,
        filter: StatisticsFilter
    ): List<TransactionEntity> {

        if (filter == StatisticsFilter.ALL_TIME) {
            return transactions
        }

        val calendar = Calendar.getInstance()

        return when (filter) {

            StatisticsFilter.THIS_MONTH -> {

                val start = Calendar.getInstance().apply {
                    set(
                        Calendar.DAY_OF_MONTH,
                        1
                    )
                    set(
                        Calendar.HOUR_OF_DAY,
                        0
                    )
                    set(
                        Calendar.MINUTE,
                        0
                    )
                    set(
                        Calendar.SECOND,
                        0
                    )
                    set(
                        Calendar.MILLISECOND,
                        0
                    )
                }.timeInMillis

                transactions.filter {
                    it.date >= start
                }
            }

            StatisticsFilter.LAST_MONTH -> {

                val start = Calendar.getInstance().apply {
                    add(Calendar.MONTH, -1)
                    set(Calendar.DAY_OF_MONTH, 1)
                    set(Calendar.HOUR_OF_DAY, 0)
                    set(Calendar.MINUTE, 0)
                    set(Calendar.SECOND, 0)
                    set(Calendar.MILLISECOND, 0)
                }.timeInMillis

                val end = Calendar.getInstance().apply {
                    set(Calendar.DAY_OF_MONTH, 1)
                    set(Calendar.HOUR_OF_DAY, 0)
                    set(Calendar.MINUTE, 0)
                    set(Calendar.SECOND, 0)
                    set(Calendar.MILLISECOND, 0)
                }.timeInMillis

                transactions.filter {
                    it.date >= start && it.date < end
                }
            }

            StatisticsFilter.LAST_3_MONTHS -> {

                val start = Calendar.getInstance().apply {
                    add(Calendar.MONTH, -3)
                    set(Calendar.HOUR_OF_DAY, 0)
                    set(Calendar.MINUTE, 0)
                    set(Calendar.SECOND, 0)
                    set(Calendar.MILLISECOND, 0)
                }.timeInMillis

                transactions.filter {
                    it.date >= start
                }
            }

            StatisticsFilter.ALL_TIME -> {
                transactions
            }
        }
    }
}
