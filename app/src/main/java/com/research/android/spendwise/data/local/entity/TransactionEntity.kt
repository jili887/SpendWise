package com.research.android.spendwise.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "transactions")
data class TransactionEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    val title: String,

    val amount: Double,

    val category: String,

    val isIncome: Boolean,

    val date: Long,

    val note: String?
)