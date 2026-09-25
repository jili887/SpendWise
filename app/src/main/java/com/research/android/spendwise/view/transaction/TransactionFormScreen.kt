package com.research.android.spendwise.view.transaction

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun TransactionFormScreen(
    mode: TransactionFormMode,
    viewModel: TransactionFormViewModel,
    onSaved: () -> Unit,
    onBackClick: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(mode) { viewModel.initialize(mode) }
    LaunchedEffect(uiState.isSaved) {
        if (uiState.isSaved) {
            viewModel.clearSavedState()
            onSaved()
        }
    }

    when {
        uiState.isLoading -> {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp)
            ) {
                CircularProgressIndicator()
            }
        }

        uiState.errorMessage != null -> {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp)
            ) {
                Text(
                    text = uiState.errorMessage!!,
                    color = MaterialTheme.colorScheme.error
                )
            }
        }

        else -> {
            TransactionFormContent(
                mode = mode,
                uiState = uiState,
                onTypeChanged = viewModel::onTypeChanged,
                onAmountChanged = viewModel::onAmountChanged,
                onCategoryChanged = viewModel::onCategoryChanged,
                onDateChanged = viewModel::onDateChanged,
                onNoteChanged = viewModel::onNoteChanged,
                onSaveClick = viewModel::saveTransaction,
                onBackClick = onBackClick
            )
        }
    }
}

@Composable
private fun TransactionFormContent(
    mode: TransactionFormMode,
    uiState: TransactionFormUiState,
    onTypeChanged: (TransactionType) -> Unit,
    onAmountChanged: (String) -> Unit,
    onCategoryChanged: (String) -> Unit,
    onDateChanged: (String) -> Unit,
    onNoteChanged: (String) -> Unit,
    onSaveClick: () -> Unit,
    onBackClick: () -> Unit
) {
    val isEditMode = mode is TransactionFormMode.Edit

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(
                rememberScrollState()
            )
            .padding(16.dp),
        verticalArrangement =
            Arrangement.spacedBy(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = onBackClick
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back"
                )
            }
            Text(
                text =
                    if (isEditMode) {
                        "Edit Transaction"
                    } else {
                        "Add Transaction"
                    },
                style =
                    MaterialTheme.typography
                        .headlineMedium
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            FilterChip(
                selected = uiState.type == TransactionType.EXPENSE,
                onClick = {
                    onTypeChanged(
                        TransactionType.EXPENSE
                    )
                },
                label = {
                    Text("Expense")
                }
            )

            FilterChip(
                selected = uiState.type == TransactionType.INCOME,
                onClick = {
                    onTypeChanged(
                        TransactionType.INCOME
                    )
                },
                label = {
                    Text("Income")
                }
            )
        }

        OutlinedTextField(
            value = uiState.amount,
            onValueChange = onAmountChanged,
            modifier = Modifier.fillMaxWidth(),
            label = {
                Text("Amount")
            },
            singleLine = true,
            isError = uiState.amountError != null,
            supportingText = {
                uiState.amountError?.let {
                    Text(it)
                }
            }
        )

        OutlinedTextField(
            value = uiState.category,
            onValueChange = onCategoryChanged,
            modifier = Modifier.fillMaxWidth(),
            label = {
                Text("Category")
            },
            singleLine = true,
            isError = uiState.categoryError != null,
            supportingText = {
                uiState.categoryError?.let {
                    Text(it)
                }
            }
        )

        OutlinedTextField(
            value = uiState.date,
            onValueChange = onDateChanged,
            modifier = Modifier.fillMaxWidth(),
            label = {
                Text("Date")
            },
            singleLine = true
        )

        OutlinedTextField(
            value = uiState.note,
            onValueChange = onNoteChanged,
            modifier = Modifier.fillMaxWidth(),
            label = {
                Text("Note")
            },
            minLines = 3
        )

        Button(
            onClick = onSaveClick,
            modifier = Modifier.fillMaxWidth(),
            enabled = !uiState.isSaving
        ) {
            if (uiState.isSaving) {
                CircularProgressIndicator()
            } else {
                Text(
                    text =
                        if (isEditMode) {
                            "Save Changes"
                        } else {
                            "Save Transaction"
                        }
                )
            }
        }
    }
}
