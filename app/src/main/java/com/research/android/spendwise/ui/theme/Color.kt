package com.research.android.spendwise.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

val Purple80 = Color(0xFFD0BCFF)
val PurpleGrey80 = Color(0xFFCCC2DC)
val Pink80 = Color(0xFFEFB8C8)

val Purple40 = Color(0xFF6650a4)
val PurpleGrey40 = Color(0xFF625b71)
val Pink40 = Color(0xFF7D5260)

val IncomeGreen = Color(0xFF3F7D63)
val ExpenseRose = Color(0xFF9D4B67)

@Immutable
data class TransactionColors(
    val income: Color = IncomeGreen,
    val expense: Color = ExpenseRose
)

val LocalTransactionColors = staticCompositionLocalOf { TransactionColors() }

val MaterialTheme.transactionColors: TransactionColors
    @Composable
    @ReadOnlyComposable
    get() = LocalTransactionColors.current