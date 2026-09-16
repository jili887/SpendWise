package com.research.android.spendwise.view.transaction

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.research.android.spendwise.view.home.TransactionUiModel

@Composable
fun AddTransactionScreen(
    onSave: (TransactionUiModel) -> Unit,
    onBackClick: () -> Unit
) {
    var type by remember {
        mutableStateOf(TransactionType.EXPENSE)
    }

    var amount by remember {
        mutableStateOf("")
    }

    var category by remember {
        mutableStateOf("")
    }

    var note by remember {
        mutableStateOf("")
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
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
                text = "Add Transaction",
                style = MaterialTheme.typography.headlineSmall
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Type",
            style = MaterialTheme.typography.titleMedium
        )

        Row {
            FilterChip(
                selected = type == TransactionType.EXPENSE,
                onClick = {
                    type = TransactionType.EXPENSE
                },
                label = {
                    Text("Expense")
                }
            )

            Spacer(modifier = Modifier.width(8.dp))

            FilterChip(
                selected = type == TransactionType.INCOME,
                onClick = {
                    type = TransactionType.INCOME
                },
                label = {
                    Text("Income")
                }
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        OutlinedTextField(
            value = amount,
            onValueChange = {
                amount = it
            },
            label = {
                Text("Amount")
            },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = category,
            onValueChange = {
                category = it
            },
            label = {
                Text("Category")
            },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = note,
            onValueChange = {
                note = it
            },
            label = {
                Text("Note")
            },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                val parsedAmount = amount.toDoubleOrNull()

                if (parsedAmount != null && category.isNotBlank()) {
                    onSave(
                        TransactionUiModel(
                            id = System.currentTimeMillis(),
                            title = category,
                            amount = parsedAmount,
                            category = category,
                            isIncome = type == TransactionType.INCOME
                        )
                    )
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Add Transaction")
        }
    }
}

@Preview(showBackground = true, showSystemUi = true, name = "Light Mode")
@Composable
fun AddTransactionScreenPreview() {
    MaterialTheme {
        Surface {
            AddTransactionScreen(
                onSave = {},
                onBackClick = {}
            )
        }
    }
}