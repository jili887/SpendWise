package com.research.android.spendwise.view.home

import com.research.android.spendwise.MainDispatcherRule
import com.research.android.spendwise.data.local.entity.TransactionEntity
import com.research.android.spendwise.data.repository.TransactionRepository
import com.research.android.spendwise.view.transaction.TransactionType
import io.mockk.coEvery
import io.mockk.coVerify
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
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val repository: TransactionRepository = mockk(relaxed = true)
    private lateinit var viewModel: HomeViewModel

    private val incomeEntity = TransactionEntity(
        id = 1L,
        title = "Paycheck",
        amount = 3200.0,
        type = TransactionType.INCOME,
        category = "Salary",
        isIncome = true,
        date = 1700000000000L,
        note = "Bi-weekly pay"
    )

    private val expenseEntity = TransactionEntity(
        id = 2L,
        title = "Supermarket",
        amount = 145.50,
        type = TransactionType.EXPENSE,
        category = "Groceries",
        isIncome = false,
        date = 1700086400000L,
        note = null
    )

    @Test
    fun `init observes transactions, calculates totals, and maps to UI models`() = runTest {
        val transactions = listOf(incomeEntity, expenseEntity)
        every { repository.getTransactions() } returns flowOf(transactions)

        viewModel = HomeViewModel(repository)
        testScheduler.advanceUntilIdle()

        val state = viewModel.uiState.value
        assertFalse(state.isLoading)
        assertNull(state.errorMessage)

        assertEquals(3200.0, state.income, 0.01)
        assertEquals(145.50, state.expenses, 0.01)
        assertEquals(3054.50, state.balance, 0.01)

        assertEquals(2, state.transactions.size)
        val mappedIncome = state.transactions[0]
        assertEquals(1L, mappedIncome.id)
        assertEquals("Paycheck", mappedIncome.title)
        assertEquals(3200.0, mappedIncome.amount, 0.01)
        assertEquals(TransactionType.INCOME, mappedIncome.type)
        assertEquals("Bi-weekly pay", mappedIncome.note)
    }

    @Test
    fun `observeTransactions handles empty list correctly`() = runTest {
        every { repository.getTransactions() } returns flowOf(emptyList())

        viewModel = HomeViewModel(repository)
        testScheduler.advanceUntilIdle()

        val state = viewModel.uiState.value
        assertFalse(state.isLoading)
        assertNull(state.errorMessage)
        assertEquals(0.0, state.income, 0.01)
        assertEquals(0.0, state.expenses, 0.01)
        assertEquals(0.0, state.balance, 0.01)
        assertTrue(state.transactions.isEmpty())
    }

    @Test
    fun `observeTransactions catches error and updates state with error message`() = runTest {
        every { repository.getTransactions() } returns flow {
            throw RuntimeException("Database error")
        }

        viewModel = HomeViewModel(repository)
        testScheduler.advanceUntilIdle()

        val state = viewModel.uiState.value
        assertFalse(state.isLoading)
        assertEquals("Database error", state.errorMessage)
    }

    @Test
    fun `retry sets loading state and re-subscribes to transaction flow`() = runTest {
        val transactionsFlow = MutableSharedFlow<List<TransactionEntity>>()
        every { repository.getTransactions() } returns transactionsFlow

        viewModel = HomeViewModel(repository)
        testScheduler.advanceUntilIdle()

        viewModel.retry()

        val loadingState = viewModel.uiState.value
        assertTrue(loadingState.isLoading)
        assertNull(loadingState.errorMessage)

        transactionsFlow.emit(listOf(incomeEntity))
        testScheduler.advanceUntilIdle()

        val successState = viewModel.uiState.value
        assertFalse(successState.isLoading)
        assertEquals(3200.0, successState.income, 0.01)
    }

    @Test
    fun `deleteTransaction invokes repository deletion`() = runTest {
        every { repository.getTransactions() } returns flowOf(emptyList())
        coEvery { repository.deleteTransactionById(1L) } returns Unit

        viewModel = HomeViewModel(repository)
        testScheduler.advanceUntilIdle()

        viewModel.deleteTransaction(1L)
        testScheduler.advanceUntilIdle()

        coVerify(exactly = 1) { repository.deleteTransactionById(1L) }
        assertNull(viewModel.uiState.value.errorMessage)
    }

    @Test
    fun `deleteTransaction updates errorMessage when repository deletion fails`() = runTest {
        every { repository.getTransactions() } returns flowOf(emptyList())
        coEvery { repository.deleteTransactionById(1L) } throws RuntimeException("Delete failed")

        viewModel = HomeViewModel(repository)
        testScheduler.advanceUntilIdle()

        viewModel.deleteTransaction(1L)
        testScheduler.advanceUntilIdle()

        val state = viewModel.uiState.value
        assertEquals("Delete failed", state.errorMessage)
    }
}
