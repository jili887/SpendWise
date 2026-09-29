package com.research.android.spendwise.view.statistics

import com.research.android.spendwise.MainDispatcherRule
import com.research.android.spendwise.data.local.entity.TransactionEntity
import com.research.android.spendwise.data.repository.TransactionRepository
import com.research.android.spendwise.view.transaction.TransactionType
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Rule
import org.junit.Test
import java.util.concurrent.TimeUnit

@OptIn(ExperimentalCoroutinesApi::class)
class StatisticsViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val repository: TransactionRepository = mockk()
    private lateinit var viewModel: StatisticsViewModel

    private val now = System.currentTimeMillis()

    private val incomeItem = TransactionEntity(
        id = 1L,
        title = "Salary",
        amount = 3000.0,
        type = TransactionType.INCOME,
        category = "Income",
        isIncome = true,
        date = now - TimeUnit.DAYS.toMillis(1),
        note = null
    )

    private val recentExpenseFood = TransactionEntity(
        id = 2L,
        title = "Groceries",
        amount = 150.0,
        type = TransactionType.EXPENSE,
        category = "Food",
        isIncome = false,
        date = now - TimeUnit.DAYS.toMillis(2),
        note = null
    )

    private val recentExpenseRent = TransactionEntity(
        id = 3L,
        title = "Rent Payment",
        amount = 1200.0,
        type = TransactionType.EXPENSE,
        category = "Housing",
        isIncome = false,
        date = now - TimeUnit.DAYS.toMillis(5),
        note = null
    )

    private val oldExpense = TransactionEntity(
        id = 4L,
        title = "Car Repair",
        amount = 500.0,
        type = TransactionType.EXPENSE,
        category = "Transport",
        isIncome = false,
        date = now - TimeUnit.DAYS.toMillis(20),
        note = null
    )

    @Test
    fun `init correctly filters LAST_7_DAYS by default and calculates statistics`() = runTest {
        val transactions = listOf(incomeItem, recentExpenseFood, recentExpenseRent, oldExpense)
        every { repository.getTransactions() } returns flowOf(transactions)

        viewModel = StatisticsViewModel(repository)
        testScheduler.advanceUntilIdle()

        val state = viewModel.uiState.value
        assertFalse(state.isLoading)
        assertNull(state.errorMessage)

        assertEquals(3000.0, state.income, 0.01)
        assertEquals(1350.0, state.expenses, 0.01) // 150 + 1200
        assertEquals(1650.0, state.balance, 0.01) // 3000 - 1350
        assertEquals(StatisticsFilter.LAST_7_DAYS, state.selectedFilter)
    }

    @Test
    fun `expensesByCategory sums totals per category and sorts in descending order`() = runTest {
        val transactions = listOf(recentExpenseFood, recentExpenseRent)
        every { repository.getTransactions() } returns flowOf(transactions)

        viewModel = StatisticsViewModel(repository)
        testScheduler.advanceUntilIdle()

        val state = viewModel.uiState.value
        val categoryList = state.expensesByCategory.toList()

        assertEquals(2, categoryList.size)
        // Highest expense ("Housing": 1200.0) comes before "Food" (150.0)
        assertEquals("Housing", categoryList[0].first)
        assertEquals(1200.0, categoryList[0].second, 0.01)

        assertEquals("Food", categoryList[1].first)
        assertEquals(150.0, categoryList[1].second, 0.01)
    }

    @Test
    fun `selectFilter ALL_TIME includes old transactions`() = runTest {
        val transactions = listOf(incomeItem, recentExpenseFood, recentExpenseRent, oldExpense)
        every { repository.getTransactions() } returns flowOf(transactions)

        viewModel = StatisticsViewModel(repository)
        testScheduler.advanceUntilIdle()

        viewModel.selectFilter(StatisticsFilter.ALL_TIME)
        testScheduler.advanceUntilIdle()

        val state = viewModel.uiState.value
        assertEquals(StatisticsFilter.ALL_TIME, state.selectedFilter)
        // Includes oldExpense (500.0) -> total expenses = 150 + 1200 + 500 = 1850.0
        assertEquals(1850.0, state.expenses, 0.01)
        assertEquals(1150.0, state.balance, 0.01)
    }

    @Test
    fun `selectFilter LAST_1_MONTH includes items within 30 days`() = runTest {
        val transactions = listOf(incomeItem, recentExpenseFood, oldExpense)
        every { repository.getTransactions() } returns flowOf(transactions)

        viewModel = StatisticsViewModel(repository)
        testScheduler.advanceUntilIdle()

        viewModel.selectFilter(StatisticsFilter.LAST_1_MONTH)
        testScheduler.advanceUntilIdle()

        val state = viewModel.uiState.value
        assertEquals(StatisticsFilter.LAST_1_MONTH, state.selectedFilter)
        // 20 days ago (oldExpense) falls within 1 month window
        assertEquals(650.0, state.expenses, 0.01) // 150 + 500
    }

    @Test
    fun `observeStatistics sets error state when repository flow throws exception`() = runTest {
        every { repository.getTransactions() } returns flow {
            throw RuntimeException("Database stream error")
        }

        viewModel = StatisticsViewModel(repository)
        testScheduler.advanceUntilIdle()

        val state = viewModel.uiState.value
        assertFalse(state.isLoading)
        assertEquals("Database stream error", state.errorMessage)
    }

    @Test
    fun `retry resets loading state and re-subscribes to repository`() = runTest {
        val transactionsFlow = MutableSharedFlow<List<TransactionEntity>>()
        every { repository.getTransactions() } returns transactionsFlow

        viewModel = StatisticsViewModel(repository)
        testScheduler.advanceUntilIdle()

        viewModel.retry()

        val loadingState = viewModel.uiState.value
        assertEquals(true, loadingState.isLoading)
        assertNull(loadingState.errorMessage)

        transactionsFlow.emit(listOf(incomeItem))
        testScheduler.advanceUntilIdle()

        val successState = viewModel.uiState.value
        assertFalse(successState.isLoading)
        assertEquals(3000.0, successState.income, 0.01)
    }
}
