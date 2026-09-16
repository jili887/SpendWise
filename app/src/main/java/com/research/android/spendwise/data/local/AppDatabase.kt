package com.research.android.spendwise.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.research.android.spendwise.data.local.dao.TransactionDao
import com.research.android.spendwise.data.local.entity.TransactionEntity

@Database(
    entities = [TransactionEntity::class],
    version = 1,
    exportSchema = true
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun transactionDao(): TransactionDao
}
