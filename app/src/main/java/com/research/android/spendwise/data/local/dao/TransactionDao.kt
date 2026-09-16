package com.research.android.spendwise.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.research.android.spendwise.data.local.entity.TransactionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TransactionDao {

    @Query(
        """
        SELECT * FROM transactions
        ORDER BY date DESC
        """
    )
    fun observeTransactions(): Flow<List<TransactionEntity>>

    @Insert
    suspend fun insertTransaction(
        transaction: TransactionEntity
    )

    @Delete
    suspend fun deleteTransaction(
        transaction: TransactionEntity
    )
}