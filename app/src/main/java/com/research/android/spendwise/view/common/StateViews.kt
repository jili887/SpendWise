package com.research.android.spendwise.view.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.research.android.spendwise.R
import com.research.android.spendwise.ui.theme.spacing
import com.research.android.spendwise.ui.theme.SpendWiseTheme

@Composable
fun LoadingState(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(MaterialTheme.spacing.xxxl),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        CircularProgressIndicator()

        Text(
            text = stringResource(R.string.loading),
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(top = MaterialTheme.spacing.md)
        )
    }
}

@Composable
fun EmptyState(
    title: String,
    message: String,
    actionLabel: String? = null,
    onActionClick: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(MaterialTheme.spacing.xxxl),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Default.ReceiptLong,
            contentDescription = null
        )

        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(top = MaterialTheme.spacing.md)
        )

        Text(
            text = message,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(top = MaterialTheme.spacing.sm)
        )

        if (actionLabel != null && onActionClick != null) {
            Button(
                onClick = onActionClick,
                modifier = Modifier.padding(top = MaterialTheme.spacing.lg)
            ) {
                Text(actionLabel)
            }
        }
    }
}

@Composable
fun ErrorState(
    message: String,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(MaterialTheme.spacing.xxxl),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Default.ErrorOutline,
            contentDescription = null
        )

        Text(
            text = stringResource(R.string.something_went_wrong),
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(top = MaterialTheme.spacing.md)
        )

        Text(
            text = message,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(top = MaterialTheme.spacing.sm)
        )

        Button(
            onClick = onRetry,
            modifier = Modifier.padding(top = MaterialTheme.spacing.lg)
        ) {
            Text(stringResource(R.string.retry))
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun LoadingStatePreview() {
    SpendWiseTheme {
        LoadingState()
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun EmptyStatePreview() {
    SpendWiseTheme {
        EmptyState(
            title = stringResource(R.string.no_data_yet),
            message = stringResource(R.string.no_data_message),
            actionLabel = stringResource(R.string.add_transaction),
            onActionClick = {}
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ErrorStatePreview() {
    SpendWiseTheme {
        ErrorState(
            message = stringResource(R.string.no_data_message),
            onRetry = {}
        )
    }
}
