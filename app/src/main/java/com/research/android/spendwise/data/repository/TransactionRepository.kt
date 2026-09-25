package com.research.android.spendwise.data.repository

import com.research.android.spendwise.data.local.dao.TransactionDao
import com.research.android.spendwise.data.local.entity.TransactionEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class TransactionRepository @Inject constructor(
    private val transactionDao: TransactionDao
) {

    fun getTransactions(): Flow<List<TransactionEntity>> {
        return transactionDao.getTransactions()
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

    suspend fun deleteTransactionById(transactionId: Long) {
        transactionDao.deleteTransactionById(transactionId)
    }
}
