package com.research.android.spendwise.view.common

import com.research.android.spendwise.data.local.dao.TransactionDao
import com.research.android.spendwise.data.local.entity.TransactionEntity
import com.research.android.spendwise.data.repository.TransactionRepository
import com.research.android.spendwise.view.home.HomeViewModel
import com.research.android.spendwise.view.statistics.StatisticsViewModel
import com.research.android.spendwise.view.transaction.TransactionFormViewModel
import com.research.android.spendwise.view.transaction.TransactionType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

private class PreviewTransactionDao : TransactionDao {
    private val transactions = MutableStateFlow(
        listOf(
            TransactionEntity(
                id = 1,
                title = "Salary",
                amount = 3500.0,
                type = TransactionType.INCOME,
                category = "Work",
                isIncome = true,
                date = System.currentTimeMillis(),
                note = "Monthly pay"
            ),
            TransactionEntity(
                id = 2,
                title = "Groceries",
                amount = 128.5,
                type = TransactionType.EXPENSE,
                category = "Food",
                isIncome = false,
                date = System.currentTimeMillis() - 86400000,
                note = "Weekly restock"
            ),
            TransactionEntity(
                id = 3,
                title = "Netflix",
                amount = 15.99,
                type = TransactionType.EXPENSE,
                category = "Entertainment",
                isIncome = false,
                date = System.currentTimeMillis() - 3 * 86400000,
                note = "Streaming"
            ),
            TransactionEntity(
                id = 4,
                title = "Rent",
                amount = 3400.0,
                type = TransactionType.EXPENSE,
                category = "Housing",
                isIncome = false,
                date = System.currentTimeMillis() - 86400000,
                note = "Monthly pay"
            )
        )
    )

    override fun getTransactions(): Flow<List<TransactionEntity>> =
        transactions.asStateFlow()

    override suspend fun getTransactionById(transactionId: Long): TransactionEntity? =
        transactions.value.firstOrNull { it.id == transactionId }

    override suspend fun insertTransaction(transaction: TransactionEntity) {
        transactions.value = listOf(transaction) + transactions.value
    }

    override suspend fun updateTransaction(transaction: TransactionEntity) {
        transactions.value = transactions.value.map {
            if (it.id == transaction.id) transaction else it
        }
    }

    override suspend fun deleteTransaction(transaction: TransactionEntity) {
        transactions.value = transactions.value.filterNot { it.id == transaction.id }
    }

    override suspend fun deleteTransactionById(transactionId: Long) {
        transactions.value = transactions.value.filterNot { it.id == transactionId }
    }
}

fun previewTransactionRepository(): TransactionRepository =
    TransactionRepository(PreviewTransactionDao())

fun previewHomeViewModel(): HomeViewModel =
    HomeViewModel(previewTransactionRepository())

fun previewStatisticsViewModel(): StatisticsViewModel =
    StatisticsViewModel(previewTransactionRepository())

fun previewTransactionFormViewModel(): TransactionFormViewModel =
    TransactionFormViewModel(previewTransactionRepository())
