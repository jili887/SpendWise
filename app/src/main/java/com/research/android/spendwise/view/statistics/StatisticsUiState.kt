package com.research.android.spendwise.view.statistics

data class StatisticsUiState(
    val income: Double = 0.0,
    val expenses: Double = 0.0,
    val balance: Double = 0.0,
    val expensesByCategory: Map<String, Double> = emptyMap(),
    val selectedFilter: StatisticsFilter = StatisticsFilter.LAST_7_DAYS,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

enum class StatisticsFilter(val label: String) {
    LAST_7_DAYS("Last 7 days"),
    LAST_14_DAYS("Last 14 days"),
    LAST_1_MONTH("Last 1 Month"),
    LAST_3_MONTHS("Last 3 Months"),
    ALL_TIME("All Time")
}
