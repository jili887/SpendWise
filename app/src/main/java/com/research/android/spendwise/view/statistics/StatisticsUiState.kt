package com.research.android.spendwise.view.statistics

data class StatisticsUiState(
    val income: Double = 0.0,
    val expenses: Double = 0.0,
    val balance: Double = 0.0,
    val expensesByCategory: Map<String, Double> = emptyMap(),
    val selectedFilter: StatisticsFilter = StatisticsFilter.THIS_MONTH,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

enum class StatisticsFilter(val label: String) {
    THIS_MONTH("This Month"),
    LAST_MONTH("Last Month"),
    LAST_3_MONTHS("Last 3 Months"),
    ALL_TIME("All Time")
}
