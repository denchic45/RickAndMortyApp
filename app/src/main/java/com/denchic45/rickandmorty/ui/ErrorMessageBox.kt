package com.denchic45.rickandmorty.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material.icons.filled.SignalWifiConnectedNoInternet4
import androidx.compose.material.icons.outlined.Dns
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.unit.dp
import com.denchic45.rickandmorty.domain.model.ApiError
import com.denchic45.rickandmorty.domain.model.Failure
import com.denchic45.rickandmorty.domain.model.NoConnection

@Composable
fun ResultFailedBox(
    modifier: Modifier = Modifier,
    failure: Failure,
    onRetryClick: (() -> Unit)? = null
) {
    when (failure) {
        NoConnection -> NoConnectionBox(modifier = modifier, onRetryClick = onRetryClick)
        is ApiError -> ApiErrorBox(modifier = modifier, failure)
        else -> UnknownErrorBox(modifier = modifier, onRetryClick = onRetryClick)
    }
}

@Composable
fun ApiErrorBox(modifier: Modifier, error: ApiError) {
    when (error.code) {
        404 -> ErrorBoxContent(
            modifier = modifier,
            painter = rememberVectorPainter(Icons.Outlined.Search),
            message = "Nothing found",
        )

        in 500..599 -> ErrorBoxContent(
            modifier = modifier,
            painter = rememberVectorPainter(Icons.Outlined.Dns),
            message = "Server error",
        )

        else -> ErrorBoxContent(
            modifier = modifier,
            painter = rememberVectorPainter(Icons.Default.ErrorOutline),
            "Unhandled error: ${error.body}"
        )
    }
}

@Composable
fun NoConnectionBox(
    modifier: Modifier = Modifier,
    onRetryClick: (() -> Unit)?
) {
    ErrorBoxContent(modifier) {
        Icon(
            Icons.Default.SignalWifiConnectedNoInternet4,
            null,
            modifier = Modifier.size(56.dp)
        )
        Text("No internet connection")
        onRetryClick?.let {
            Button(it) {
                Text("Retry")
            }
        }
    }
}

@Composable
fun UnknownErrorBox(
    modifier: Modifier = Modifier,
    onRetryClick: (() -> Unit)?
) {
    ErrorBoxContent(modifier) {
        Icon(
            Icons.Default.ErrorOutline,
            null,
            modifier = Modifier.size(56.dp)
        )
        Text("Unknown error")
        onRetryClick?.let {
            Button(it) {
                Text("Retry")
            }
        }
    }
}

@Composable
fun ErrorBoxContent(
    modifier: Modifier = Modifier,
    painter: Painter,
    message: String,
    onRetryClick: (() -> Unit)? = null
) {
    ErrorBoxContent(modifier) {
        Icon(
            painter,
            null,
            modifier = Modifier.size(56.dp)
        )
        Text(message)
        onRetryClick?.let {
            Button(it) {
                Text("Retry")
            }
        }
    }
}

@Composable
private fun ErrorBoxContent(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    Box(modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally, content = content)
    }
}