package com.zeroone.swipeaction.swipe_action

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.*
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.*
import androidx.compose.ui.zIndex
import kotlinx.coroutines.launch
import kotlin.math.abs


@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun HorizontalSwipeAction(
    modifier: Modifier = Modifier,
    trailingContentSize: Dp? = null,
    leadingContentSize: Dp? = null,
    leadingContentBackgroundColor: Color? = null,
    trailingContentBackgroundColor: Color? = null,
    trailingContent: (@Composable () -> Unit)? = null,
    leadingContent: (@Composable () -> Unit)? = null,
    content: @Composable () -> Unit,
) {

    val coroutineScope = rememberCoroutineScope()
    val density = LocalDensity.current
    var scopeHeight by remember { mutableStateOf(Dp.Unspecified) }
    var scopeHeightPx by remember { mutableStateOf(Float.NaN) }
    var scopeWidth by remember { mutableStateOf(Dp.Unspecified) }
    var scopeWidthPx by remember { mutableStateOf(Float.NaN) }

    var trailingScopeWidth by remember { mutableStateOf(Dp.Unspecified) }
    var leadingScopeWidth by remember { mutableStateOf(Dp.Unspecified) }
    var thresholdsValuePx by remember { mutableStateOf(Float.NaN) }

    var offsetX by remember { mutableStateOf(Dp.Unspecified) }
    var offsetXPx by remember { mutableStateOf(Float.NaN) }

    var startPosition by remember { mutableStateOf(Offset.Zero) }
    var currentPosition by remember { mutableStateOf(Offset.Zero) }

    var isDraggingLeadingSide by remember { mutableStateOf(false) }
    var isDraggingTrailingSide by remember { mutableStateOf(false) }

    var backgroundColor by remember { mutableStateOf(Color.Unspecified) }

    BoxWithConstraints(
        modifier = modifier
            .onGloballyPositioned { coordinates ->
                scopeHeight = with(density, block = { coordinates.size.height.toDp() })
                scopeWidth = with(density) { coordinates.size.width.toDp() }
                scopeWidthPx = coordinates.size.width.toFloat()
                scopeHeightPx = coordinates.size.height.toFloat()
            }
            .pointerInput(Unit) {
                detectHorizontalDragGestures(
                    onDragEnd = {
                        if (currentPosition.x > thresholdsValuePx) {
                            coroutineScope.launch {
                                animate(
                                    initialValue = currentPosition.x,
                                    targetValue = thresholdsValuePx,
                                    animationSpec = tween(
                                        durationMillis = 500,
                                        easing = FastOutSlowInEasing
                                    ),
                                    block = { val1, _ ->
                                        currentPosition = Offset(x = val1, y = 0.0f)
                                    }
                                )
                            }
                            isDraggingLeadingSide = true
                            leadingScopeWidth = leadingContentSize ?: (scopeWidth / 2)
                        } else if (currentPosition.x < -thresholdsValuePx) {
                            coroutineScope.launch {
                                animate(
                                    initialValue = currentPosition.x,
                                    targetValue = -thresholdsValuePx,
                                    animationSpec = tween(
                                        durationMillis = 500,
                                        easing = FastOutSlowInEasing
                                    ),
                                    block = { val1, _ ->
                                        currentPosition = Offset(x = val1, y = 0.0f)
                                    }
                                )
                            }
                            isDraggingTrailingSide = true
                            trailingScopeWidth = trailingContentSize ?: (scopeWidth / 2)
                        } else {
                            coroutineScope.launch {
                                animate(
                                    initialValue = currentPosition.x,
                                    targetValue = 0.0f,
                                    animationSpec = tween(
                                        durationMillis = 500,
                                        easing = FastOutSlowInEasing
                                    ),
                                    block = { val1, _ ->
                                        currentPosition = Offset(x = val1, y = 0.0f)
                                    }
                                )
                            }

                            trailingScopeWidth = 0.dp
                            leadingScopeWidth = 0.dp
                            backgroundColor = Color.Unspecified
                            isDraggingLeadingSide = false
                            isDraggingTrailingSide = false
                        }
                    },
                    onHorizontalDrag = { change, value ->
                        if (change.isConsumed) {
                            startPosition = change.previousPosition - currentPosition
                            currentPosition = change.position - startPosition
                        }

                        if (currentPosition.x < 0) {
                            isDraggingLeadingSide = false
                            if (trailingContent != null) {
                                backgroundColor =
                                    trailingContentBackgroundColor ?: Color.Unspecified
                                currentPosition = change.position - startPosition
                                isDraggingTrailingSide = true
                                thresholdsValuePx =
                                    trailingContentSize?.toPx() ?: (scopeWidth / 2).toPx()
                                trailingScopeWidth -= value.toDp()
                                offsetX += abs(value).toDp()
                                offsetXPx += abs(value)
                            }
                        }
                        if (currentPosition.x > 0) {
                            isDraggingTrailingSide = false
                            if (leadingContent != null) {
                                backgroundColor =
                                    leadingContentBackgroundColor ?: Color.Unspecified
                                currentPosition = change.position - startPosition
                                isDraggingLeadingSide = true
                                thresholdsValuePx =
                                    leadingContentSize?.toPx() ?: (scopeWidth / 2).toPx()
                                leadingScopeWidth += value.toDp()
                            }
                        }
                    }
                )
            }
            .background(backgroundColor),
    ) {
        Box(
            contentAlignment = Alignment.CenterStart,
            content = { content() },
            modifier = Modifier
                .graphicsLayer(translationX = currentPosition.x)
                .zIndex(zIndex = 0.1f)
        )

        if (trailingContent != null) {
            if (isDraggingTrailingSide) {
                Box(
                    content = { trailingContent() },
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .heightIn(max = scopeHeight)
                        .widthIn(max = scopeWidth)
                        .width(trailingScopeWidth)
                        .graphicsLayer(translationX = scopeWidthPx + currentPosition.x)
                        .background(trailingContentBackgroundColor ?: Color.Unspecified)
                )
            }
        }

        if (leadingContent != null) {
            if (isDraggingLeadingSide) {
                Box(
                    content = { leadingContent() },
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .heightIn(max = scopeHeight)
                        .widthIn(max = scopeWidth)
                        .width(leadingScopeWidth)
                        .background(leadingContentBackgroundColor ?: Color.Unspecified)
                )
            }
        }
    }
}