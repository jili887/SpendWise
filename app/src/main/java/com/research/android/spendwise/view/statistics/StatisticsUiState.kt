package com.research.android.spendwise.view.statistics

import androidx.annotation.StringRes
import com.research.android.spendwise.R

data class StatisticsUiState(
    val income: Double = 0.0,
    val expenses: Double = 0.0,
    val balance: Double = 0.0,
    val expensesByCategory: Map<String, Double> = emptyMap(),
    val selectedFilter: StatisticsFilter = StatisticsFilter.LAST_7_DAYS,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

enum class StatisticsFilter(@StringRes val labelRes: Int) {
    LAST_7_DAYS(R.string.stat_filter_last_7_days),
    LAST_14_DAYS(R.string.stat_filter_last_14_days),
    LAST_1_MONTH(R.string.stat_filter_last_1_month),
    LAST_3_MONTHS(R.string.stat_filter_last_3_months),
    ALL_TIME(R.string.stat_filter_all_time)
}
