package com.research.android.spendwise.data.repository

import com.research.android.spendwise.data.local.dao.TransactionDao
import com.research.android.spendwise.data.local.entity.TransactionEntity
import kotlinx.coroutines.flow.Flow

class TransactionRepository(
    private val transactionDao: TransactionDao
) {

    fun observeTransactions(): Flow<List<TransactionEntity>> {
        return transactionDao.observeTransactions()
    }

    suspend fun insertTransaction(
        transaction: TransactionEntity
    ) {
        transactionDao.insertTransaction(transaction)
    }

    suspend fun deleteTransaction(
        transaction: TransactionEntity
    ) {
        transactionDao.deleteTransaction(transaction)
    }
}
