package com.research.android.spendwise.view.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun TransactionItem(
    transaction: TransactionUiModel
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = transaction.title,
                style = MaterialTheme.typography.bodyLarge
            )

            Text(
                text = transaction.category,
                style = MaterialTheme.typography.bodySmall
            )
        }

        Text(
            text = if (transaction.isIncome) {
                "+$${"%,.2f".format(transaction.amount)}"
            } else {
                "-$${"%,.2f".format(transaction.amount)}"
            },
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun TransactionItemPreview() {
    MaterialTheme {
        TransactionItem(
            transaction = TransactionUiModel(
                id = 1,
                title = "Groceries",
                amount = 85.40,
                category = "Food",
                isIncome = false
            )
        )
    }
}