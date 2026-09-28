package com.research.android.spendwise.view.transaction

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class TransactionValidatorTest {

    @Test
    fun `valid transaction returns valid result`() {

        val result = TransactionValidator.validate(
            amount = "25.50",
            category = "Food",
            date = "09/28/2026",
            note = "Lunch"
        )

        assertTrue(result.isValid)
        assertEquals(null, result.amountError)
        assertEquals(null, result.categoryError)
        assertEquals(null, result.dateError)
        assertEquals(null, result.noteError)
    }

    @Test
    fun `empty amount returns error`() {

        val result = TransactionValidator.validate(
            amount = "",
            category = "Food",
            date = "09/28/2026",
            note = ""
        )

        assertFalse(result.isValid)
        assertEquals(
            "Amount is required.",
            result.amountError
        )
    }

    @Test
    fun `blank amount returns error`() {

        val result = TransactionValidator.validate(
            amount = "   ",
            category = "Food",
            date = "09/28/2026",
            note = ""
        )

        assertFalse(result.isValid)
        assertEquals(
            "Amount is required.",
            result.amountError
        )
    }

    @Test
    fun `non numeric amount returns error`() {

        val result = TransactionValidator.validate(
            amount = "abc",
            category = "Food",
            date = "09/28/2026",
            note = ""
        )

        assertFalse(result.isValid)
        assertEquals(
            "Enter a valid amount.",
            result.amountError
        )
    }

    @Test
    fun `zero amount returns error`() {

        val result = TransactionValidator.validate(
            amount = "0",
            category = "Food",
            date = "09/28/2026",
            note = ""
        )

        assertFalse(result.isValid)
        assertEquals(
            "Amount must be greater than 0.",
            result.amountError
        )
    }

    @Test
    fun `negative amount returns error`() {

        val result = TransactionValidator.validate(
            amount = "-10",
            category = "Food",
            date = "09/28/2026",
            note = ""
        )

        assertFalse(result.isValid)
        assertEquals(
            "Amount must be greater than 0.",
            result.amountError
        )
    }

    @Test
    fun `amount with two decimal places is valid`() {

        val result = TransactionValidator.validate(
            amount = "12.50",
            category = "Food",
            date = "09/28/2026",
            note = ""
        )

        assertTrue(result.isValid)
    }

    @Test
    fun `amount with more than two decimal places returns error`() {

        val result = TransactionValidator.validate(
            amount = "12.505",
            category = "Food",
            date = "09/28/2026",
            note = ""
        )

        assertFalse(result.isValid)
        assertEquals(
            "Amount can have at most 2 decimal places.",
            result.amountError
        )
    }

    @Test
    fun `empty category returns error`() {

        val result = TransactionValidator.validate(
            amount = "25",
            category = "",
            date = "09/28/2026",
            note = ""
        )

        assertFalse(result.isValid)
        assertEquals(
            "Category is required.",
            result.categoryError
        )
    }

    @Test
    fun `blank category returns error`() {

        val result = TransactionValidator.validate(
            amount = "25",
            category = "   ",
            date = "09/28/2026",
            note = ""
        )

        assertFalse(result.isValid)
        assertEquals(
            "Category is required.",
            result.categoryError
        )
    }

    @Test
    fun `category longer than 50 characters returns error`() {

        val category = "A".repeat(51)

        val result = TransactionValidator.validate(
            amount = "25",
            category = category,
            date = "09/28/2026",
            note = ""
        )

        assertFalse(result.isValid)
        assertEquals(
            "Category must be 50 characters or less.",
            result.categoryError
        )
    }

    @Test
    fun `empty date returns error`() {

        val result = TransactionValidator.validate(
            amount = "25",
            category = "Food",
            date = "",
            note = ""
        )

        assertFalse(result.isValid)
        assertEquals(
            "Date is required.",
            result.dateError
        )
    }

    @Test
    fun `blank date returns error`() {

        val result = TransactionValidator.validate(
            amount = "25",
            category = "Food",
            date = "   ",
            note = ""
        )

        assertFalse(result.isValid)
        assertEquals(
            "Date is required.",
            result.dateError
        )
    }

    @Test
    fun `empty note is valid`() {

        val result = TransactionValidator.validate(
            amount = "25",
            category = "Food",
            date = "09/28/2026",
            note = ""
        )

        assertTrue(result.isValid)
        assertEquals(null, result.noteError)
    }

    @Test
    fun `note longer than 200 characters returns error`() {

        val note = "A".repeat(201)

        val result = TransactionValidator.validate(
            amount = "25",
            category = "Food",
            date = "09/28/2026",
            note = note
        )

        assertFalse(result.isValid)
        assertEquals(
            "Note must be 200 characters or less.",
            result.noteError
        )
    }

    @Test
    fun `multiple invalid fields return all corresponding errors`() {

        val result = TransactionValidator.validate(
            amount = "",
            category = "",
            date = "",
            note = "A".repeat(201)
        )

        assertFalse(result.isValid)

        assertEquals(
            "Amount is required.",
            result.amountError
        )

        assertEquals(
            "Category is required.",
            result.categoryError
        )

        assertEquals(
            "Date is required.",
            result.dateError
        )

        assertEquals(
            "Note must be 200 characters or less.",
            result.noteError
        )
    }
}
