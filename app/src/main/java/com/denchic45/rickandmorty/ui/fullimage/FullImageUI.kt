package com.denchic45.rickandmorty.ui.fullimage

import android.content.res.Resources
import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.zIndex
import coil3.compose.AsyncImage
import kotlinx.coroutines.launch
import net.engawapg.lib.zoomable.rememberZoomState
import net.engawapg.lib.zoomable.zoomable

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FullScreenImage(url: String, onDismissRequest: () -> Unit) {
    val haptic = LocalHapticFeedback.current
    val coroutineScope = rememberCoroutineScope()
    val screenHeightDp = Resources.getSystem().configuration.screenHeightDp.dp
    val imageYOffset = remember {
        Animatable(0f)
    }
    val scaleFactor = remember {
        Animatable(1f)
    }
    val minOffsetToClose = screenHeightDp / 6
    val swipeToCloseGesture = Modifier.pointerInput(Unit) {
        detectDragGestures(
            onDrag = { _, dragAmount ->
                coroutineScope.launch {
                    launch {
                        imageYOffset.animateTo(imageYOffset.value + dragAmount.y)
                    }
                    launch {
                        scaleFactor.animateTo(
                            (1f - (imageYOffset.value / 1000f)).coerceIn(
                                0.5f,
                                1f
                            )
                        )
                    }
                }
            },
            onDragEnd = {
                if (imageYOffset.value.dp >= minOffsetToClose) {
                    onDismissRequest()
                } else {
                    coroutineScope.launch {
                        launch {
                            imageYOffset.animateTo(0f)
                        }
                        launch {
                            scaleFactor.animateTo(1f)
                        }
                    }
                }
            })
    }

    LaunchedEffect(imageYOffset.value.dp >= minOffsetToClose) {
        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
    }

    Dialog(
        onDismissRequest = onDismissRequest,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {

        Column {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent
                ),
                navigationIcon = {
                    IconButton(onClick = { onDismissRequest() }) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            null,
                            tint = Color.White
                        )
                    }
                },
                title = {}
            )
            Box(
                modifier = Modifier
                    .zIndex(10F)
                    .fillMaxSize()
                    .clickable(interactionSource = null, indication = null) {
                        onDismissRequest()
                    },
                contentAlignment = Alignment.Center
            ) {

                LocalContext.current
                val zoomState = rememberZoomState()
                AsyncImage(
                    model = url,
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .zoomable(zoomState)
                        .graphicsLayer {
                            scaleX = scaleFactor.value
                            scaleY = scaleFactor.value
                        }
                        .offset(0.dp, (imageYOffset.value * 1.8).dp)
                        .then(swipeToCloseGesture)
                        .background(Color.Blue),
                    contentScale = ContentScale.Fit
                )
            }
        }
    }
}