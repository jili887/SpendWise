package com.research.android.spendwise.view.transaction

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.research.android.spendwise.ui.theme.SpendWiseTheme
import com.research.android.spendwise.ui.theme.spacing
import com.research.android.spendwise.util.millisToDateString
import com.research.android.spendwise.view.common.previewTransactionFormViewModel

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
                    .padding(MaterialTheme.spacing.xxl)
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.size(20.dp),
                    strokeWidth = 2.dp
                )
            }
        }

        uiState.errorMessage != null -> {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(MaterialTheme.spacing.xxl)
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
                onTitleChanged = viewModel::onTitleChanged,
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
    onTitleChanged: (String) -> Unit,
    onAmountChanged: (String) -> Unit,
    onCategoryChanged: (String) -> Unit,
    onDateChanged: (String) -> Unit,
    onNoteChanged: (String) -> Unit,
    onSaveClick: () -> Unit,
    onBackClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(
                rememberScrollState()
            )
            .padding(MaterialTheme.spacing.lg),
        verticalArrangement =
            Arrangement.spacedBy(MaterialTheme.spacing.lg)
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
                text = when (mode) {
                    TransactionFormMode.Add ->
                        "Add Transaction"

                    is TransactionFormMode.Edit ->
                        "Edit Transaction"
                },
                style =
                    MaterialTheme.typography.headlineMedium
            )
        }

        Text(
            modifier = Modifier.padding(start = MaterialTheme.spacing.lg),
            text = when (mode) {
                TransactionFormMode.Add ->
                    "Track your income or expenses"

                is TransactionFormMode.Edit ->
                    "Update your transaction details"
            },
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        SingleChoiceSegmentedButtonRow(
            modifier = Modifier.fillMaxWidth()
        ) {

            SegmentedButton(
                selected =
                    uiState.type == TransactionType.EXPENSE,
                onClick = {
                    onTypeChanged(
                        TransactionType.EXPENSE
                    )
                },
                shape = SegmentedButtonDefaults.itemShape(
                    index = 0,
                    count = 2
                ),
                label = { Text("Expense") }
            )

            SegmentedButton(
                selected =
                    uiState.type == TransactionType.INCOME,
                onClick = {
                    onTypeChanged(
                        TransactionType.INCOME
                    )
                },
                shape = SegmentedButtonDefaults.itemShape(
                    index = 1,
                    count = 2
                ),
                label = { Text("Income") }
            )
        }

        OutlinedTextField(
            value = uiState.title,
            onValueChange = onTitleChanged,
            modifier = Modifier
                .fillMaxWidth(),
            label = { Text("Title") },
            placeholder = { Text("Required. e.g. Lunch") },
            singleLine = true,
            isError = uiState.titleError != null,
            supportingText = {
                uiState.titleError?.let { error -> Text(error) }
            }
        )

        OutlinedTextField(
            value = uiState.amount,
            onValueChange = onAmountChanged,
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Amount") },
            placeholder = { Text("Required") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            isError = uiState.amountError != null,
            supportingText = {
                uiState.amountError?.let { error ->
                    Text(error)
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
            placeholder = { Text("Required") },
            singleLine = true,
            isError = uiState.categoryError != null,
            supportingText = {
                uiState.categoryError?.let { error ->
                    Text(error)
                }
            }
        )

        var showDatePicker by rememberSaveable {
            mutableStateOf(false)
        }
        Box(
            modifier = Modifier.fillMaxWidth()
        ) {
            OutlinedTextField(
                value = uiState.date,
                onValueChange = {},
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        showDatePicker = true
                    },
                label = {
                    Text("Date")
                },
                singleLine = true,
                readOnly = true,
                enabled = true,
                isError = uiState.dateError != null,
                supportingText = {
                    uiState.dateError?.let { error ->
                        Text(error)
                    }
                },
                trailingIcon = {
                    IconButton(
                        onClick = {
                            showDatePicker = true
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.DateRange,
                            contentDescription = "Select date"
                        )
                    }
                }
            )
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .clickable {
                        showDatePicker = true
                    }
            )
        }

        if (showDatePicker) {
            val datePickerState = rememberDatePickerState()

            DatePickerDialog(
                onDismissRequest = {
                    showDatePicker = false
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            datePickerState.selectedDateMillis?.let { millis ->
                                onDateChanged(
                                    millisToDateString(millis)
                                )
                            }
                            showDatePicker = false
                        }
                    ) {
                        Text("OK")
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = {
                            showDatePicker = false
                        }
                    ) {
                        Text("Cancel")
                    }
                }
            ) {
                DatePicker(
                    state = datePickerState
                )
            }
        }

        OutlinedTextField(
            value = uiState.note,
            onValueChange = onNoteChanged,
            modifier = Modifier.fillMaxWidth(),
            label = {
                Text("Note")
            },
            placeholder = { Text("Optional") },
            minLines = 3,
            maxLines = 5,
            isError = uiState.noteError != null,
            supportingText = {
                uiState.noteError?.let { error ->
                    Text(error)
                }
            }
        )

        Button(
            onClick = onSaveClick,
            modifier = Modifier.fillMaxWidth(),
            enabled = !uiState.isSaving
        ) {
            if (uiState.isSaving) {
                CircularProgressIndicator(
                    modifier = Modifier.size(20.dp),
                    strokeWidth = 2.dp
                )
            } else {
                Text(
                    text =
                        when (mode) {
                            TransactionFormMode.Add -> "Save Transaction"
                            is TransactionFormMode.Edit -> "Save Changes"
                        }
                )
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun TransactionFormScreenPreview() {
    SpendWiseTheme {
        TransactionFormScreen(
            mode = TransactionFormMode.Add,
            viewModel = previewTransactionFormViewModel(),
            onSaved = {},
            onBackClick = {}
        )
    }
}
