package com.research.android.spendwise.view.transaction

import com.research.android.spendwise.MainDispatcherRule
import com.research.android.spendwise.data.local.entity.TransactionEntity
import com.research.android.spendwise.data.repository.TransactionRepository
import com.research.android.spendwise.util.millisToDateString
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class TransactionFormViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val repository: TransactionRepository = mockk(relaxed = true)
    private lateinit var viewModel: TransactionFormViewModel

    private val sampleTransaction = TransactionEntity(
        id = 1L,
        title = "Grocery Shopping",
        amount = 45.50,
        type = TransactionType.EXPENSE,
        category = "Food",
        isIncome = false,
        date = 1700000000000L,
        note = "Weekly groceries"
    )

    @Before
    fun setUp() {
        viewModel = TransactionFormViewModel(repository)
    }

    @Test
    fun `initialize in Add mode resets UI state to default`() {
        viewModel.initialize(TransactionFormMode.Add)

        val state = viewModel.uiState.value
        assertEquals("", state.title)
        assertEquals("", state.amount)
        assertEquals("", state.category)
        assertFalse(state.isLoading)
        assertNull(state.errorMessage)
    }

    @Test
    fun `initialize in Edit mode populates UI state when transaction is found`() = runTest {
        coEvery { repository.getTransactionById(1L) } returns sampleTransaction

        viewModel.initialize(TransactionFormMode.Edit(transactionId = 1L))

        val state = viewModel.uiState.value
        assertFalse(state.isLoading)
        assertEquals("Grocery Shopping", state.title)
        assertEquals("45.5", state.amount)
        assertEquals("Food", state.category)
        assertEquals(TransactionType.EXPENSE, state.type)
        assertEquals("Weekly groceries", state.note)
        assertNull(state.errorMessage)
    }

    @Test
    fun `initialize in Edit mode sets error message when transaction is not found`() = runTest {
        coEvery { repository.getTransactionById(99L) } returns null

        viewModel.initialize(TransactionFormMode.Edit(transactionId = 99L))

        val state = viewModel.uiState.value
        assertFalse(state.isLoading)
        assertEquals("Transaction not found.", state.errorMessage)
    }

    @Test
    fun `initialize in Edit mode handles repository exception gracefully`() = runTest {
        coEvery { repository.getTransactionById(1L) } throws RuntimeException("Database error")

        viewModel.initialize(TransactionFormMode.Edit(transactionId = 1L))

        val state = viewModel.uiState.value
        assertFalse(state.isLoading)
        assertEquals("Database error", state.errorMessage)
    }

    @Test
    fun `onTypeChanged updates transaction type in state`() {
        viewModel.onTypeChanged(TransactionType.INCOME)
        assertEquals(TransactionType.INCOME, viewModel.uiState.value.type)
    }

    @Test
    fun `onTitleChanged updates title in state`() {
        viewModel.onTitleChanged("Salary")
        assertEquals("Salary", viewModel.uiState.value.title)
    }

    @Test
    fun `onAmountChanged updates amount and clears amount error`() {
        viewModel.onAmountChanged("100.0")

        val state = viewModel.uiState.value
        assertEquals("100.0", state.amount)
        assertNull(state.amountError)
    }

    @Test
    fun `onCategoryChanged updates category and clears category error`() {
        viewModel.onCategoryChanged("Income")

        val state = viewModel.uiState.value
        assertEquals("Income", state.category)
        assertNull(state.categoryError)
    }

    @Test
    fun `onNoteChanged updates note in state`() {
        viewModel.onNoteChanged("Monthly salary payment")
        assertEquals("Monthly salary payment", viewModel.uiState.value.note)
    }

    @Test
    fun `saveTransaction in Add mode inserts transaction into repository on valid inputs`() = runTest {
        viewModel.initialize(TransactionFormMode.Add)
        viewModel.onTitleChanged("Rent")
        viewModel.onAmountChanged("1200.00")
        viewModel.onCategoryChanged("Housing")

        // Ensure this string format matches what dateStringToMillis expects (e.g. "03/25/2026" or "2026-03-25")
        val validDateString = millisToDateString(System.currentTimeMillis())
        viewModel.onDateChanged(validDateString)

        viewModel.saveTransaction()
        testScheduler.advanceUntilIdle()

        coVerify(exactly = 1) { repository.insertTransaction(any()) }

        val state = viewModel.uiState.value
        assertFalse(state.isSaving)
        assertTrue(state.isSaved)
        assertNull(state.dateError)
    }

    @Test
    fun `saveTransaction in Edit mode updates transaction in repository on valid inputs`() = runTest {
        coEvery { repository.getTransactionById(1L) } returns sampleTransaction
        viewModel.initialize(TransactionFormMode.Edit(1L))

        viewModel.onTitleChanged("Updated Groceries")
        viewModel.saveTransaction()

        coVerify(exactly = 1) { repository.updateTransaction(any()) }

        val state = viewModel.uiState.value
        assertFalse(state.isSaving)
        assertTrue(state.isSaved)
    }

    @Test
    fun `saveTransaction sets error message when insert throws exception`() = runTest {
        coEvery { repository.insertTransaction(any()) } throws RuntimeException("Insert failed")

        viewModel.initialize(TransactionFormMode.Add)
        viewModel.onTitleChanged("Coffee")
        viewModel.onAmountChanged("4.50")
        viewModel.onCategoryChanged("Food")

        val validDateString = millisToDateString(System.currentTimeMillis())
        viewModel.onDateChanged(validDateString)

        viewModel.saveTransaction()
        testScheduler.advanceUntilIdle()

        val state = viewModel.uiState.value
        assertFalse(state.isSaving)
        assertFalse(state.isSaved)
        assertEquals("Insert failed", state.errorMessage)
    }

    @Test
    fun `clearSavedState resets isSaved back to false`() {
        viewModel.clearSavedState()
        assertFalse(viewModel.uiState.value.isSaved)
    }
}
