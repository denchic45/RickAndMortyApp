package com.denchic45.rickandmorty.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.dp
import com.denchic45.rickandmorty.ui.theme.disabledColor

@Composable
fun EmptyContent(modifier: Modifier = Modifier, painter: Painter, text: String) {
    EmptyContentBox(modifier) {
        Icon(
            painter,
            null,
            modifier = Modifier.size(64.dp),
            tint = disabledColor()
        )
        Spacer(Modifier.height(8.dp))
        Text(text)
    }
}

@Composable
fun EmptyContentBox(modifier: Modifier = Modifier, content: @Composable ColumnScope.() -> Unit) {
    Box(modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            content()
        }
    }
}