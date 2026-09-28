package com.research.android.spendwise.view.transaction

object TransactionValidator {

    private const val MAX_CATEGORY_LENGTH = 50
    private const val MAX_NOTE_LENGTH = 200

    fun validate(
        amount: String,
        category: String,
        date: String,
        note: String
    ): TransactionValidationResult {

        val amountError = validateAmount(amount)
        val categoryError = validateCategory(category)
        val dateError = validateDate(date)
        val noteError = validateNote(note)

        return TransactionValidationResult(
            isValid =
                amountError == null &&
                        categoryError == null &&
                        dateError == null &&
                        noteError == null,

            amountError = amountError,
            categoryError = categoryError,
            dateError = dateError,
            noteError = noteError
        )
    }

    private fun validateAmount(
        amount: String
    ): String? {

        val trimmedAmount = amount.trim()

        if (trimmedAmount.isEmpty()) {
            return "Amount is required."
        }

        val value = trimmedAmount.toDoubleOrNull()

        if (value == null) {
            return "Enter a valid amount."
        }

        if (value <= 0) {
            return "Amount must be greater than 0."
        }

        val decimalPart =
            trimmedAmount
                .substringAfter('.', "")

        if (decimalPart.length > 2) {
            return "Amount can have at most 2 decimal places."
        }

        return null
    }

    private fun validateCategory(
        category: String
    ): String? {

        val trimmedCategory = category.trim()

        if (trimmedCategory.isEmpty()) {
            return "Category is required."
        }

        if (trimmedCategory.length > MAX_CATEGORY_LENGTH) {
            return "Category must be 50 characters or less."
        }

        return null
    }

    private fun validateDate(
        date: String
    ): String? {

        if (date.trim().isEmpty()) {
            return "Date is required."
        }

        return null
    }

    private fun validateNote(
        note: String
    ): String? {

        if (note.length > MAX_NOTE_LENGTH) {
            return "Note must be 200 characters or less."
        }

        return null
    }
}
