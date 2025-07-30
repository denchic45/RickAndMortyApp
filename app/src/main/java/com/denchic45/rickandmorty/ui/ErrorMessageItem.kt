package com.denchic45.rickandmorty.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.denchic45.rickandmorty.domain.model.Failure
import com.denchic45.rickandmorty.domain.model.NoConnection

@Composable
fun ResultFailedItem(
    modifier: Modifier = Modifier,
    failure: Failure,
    onRetryClick: () -> Unit
) {
    when (failure) {
        NoConnection -> NoConnectionItem(modifier = modifier, onRetryClick = onRetryClick)
        else -> UnknownErrorItem(modifier = modifier, onRetryClick = onRetryClick)
    }
}

@Composable
fun NoConnectionItem(modifier: Modifier = Modifier, onRetryClick: () -> Unit) {
    ErrorItem(
        modifier = modifier,
        text = "Check internet connection",
        onRetryClick = onRetryClick
    )
}

@Composable
fun UnknownErrorItem(modifier: Modifier = Modifier, onRetryClick: () -> Unit) {
    ErrorItem(
        modifier = modifier,
        text = "Unknown error",
        onRetryClick = onRetryClick
    )
}

@Composable
fun ErrorItem(modifier: Modifier = Modifier, text: String, onRetryClick: () -> Unit) {
    ListItem(
        modifier = modifier,
        headlineContent = { Text(text, style = MaterialTheme.typography.titleSmall) },
        leadingContent = {
            Icon(
                Icons.Default.Warning,
                null,
                tint = MaterialTheme.colorScheme.error
            )
        },
        trailingContent = { FilledTonalButton(onRetryClick) { Text("Retry") } }
    )
}
