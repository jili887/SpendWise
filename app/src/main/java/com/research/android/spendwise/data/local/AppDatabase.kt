package com.research.android.spendwise.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.research.android.spendwise.data.local.dao.TransactionDao
import com.research.android.spendwise.data.local.entity.Converters
import com.research.android.spendwise.data.local.entity.TransactionEntity

@Database(
    entities = [TransactionEntity::class],
    version = 2,
    exportSchema = true
)

@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun transactionDao(): TransactionDao
}

val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(db: SupportSQLiteDatabase) {
        // Add the new column with a default value for existing rows.
        // SQLite requires NOT NULL columns to have a DEFAULT.
        db.execSQL(
            """
            ALTER TABLE transactions
            ADD COLUMN type TEXT NOT NULL DEFAULT 'EXPENSE'
            """.trimIndent()
        )
    }
}
