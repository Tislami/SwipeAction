package com.zeroone.swipeaction.swipe_action

import android.util.Log
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
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


@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun HorizontalSwipeAction(
    modifier: Modifier,
    trailingContentThresholds : Dp?=null,
    leadingContentThresholds : Dp?=null,
    contentAlignment: Alignment = Alignment.CenterEnd,
    scaledBackground: Boolean = true,
    leadingContentBackgroundColor: Color? = Color.Green,
    trailingContentBackgroundColor: Color? = Color.Green,
    trailingContent: (@Composable () -> Unit)? = null,
    leadingContent: (@Composable () -> Unit)? = null,
    content: @Composable () -> Unit,
) {

    val density = LocalDensity.current
    var scopeHeight by remember { mutableStateOf(Dp.Unspecified) }
    var scopeHeightPx by remember { mutableStateOf(Float.NaN ) }
    var scopeWidth by remember { mutableStateOf(Dp.Unspecified) }
    var scopeWidthPx by remember { mutableStateOf(Float.NaN) }
    var trailingScopeWidth by remember { mutableStateOf(Dp.Unspecified) }
    var leadingScopeWidth by remember { mutableStateOf(Dp.Unspecified) }
    var thresholdsValuePx by remember { mutableStateOf(Float.NaN) }

    var startPosition by remember { mutableStateOf(Offset.Zero) }
    var currentPosition by remember { mutableStateOf(Offset.Zero) }

    var isDragging by remember { mutableStateOf(false) }
    var isTrailing by remember { mutableStateOf(false) }
    var isLeading by remember { mutableStateOf(false) }

    //var trailingScopeWidthAnimationValue = animateDpAsState(targetValue = )
    var offsetXAnimationValue = animateFloatAsState(targetValue = thresholdsValuePx)


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
                            currentPosition = Offset(x = thresholdsValuePx, y = 0.0f)
                            isDragging = false
                            isLeading = true
                            leadingScopeWidth = leadingContentThresholds ?: (scopeWidth / 2)
                        } else if (currentPosition.x < -thresholdsValuePx) {
                            currentPosition = Offset(x = -thresholdsValuePx, y = 0.0f)
                            isDragging = false
                            isTrailing = true
                            trailingScopeWidth = trailingContentThresholds ?: (scopeWidth / 2)
                        } else {
                            currentPosition = Offset.Zero
                            isDragging = false
                            isTrailing = false
                            isLeading = false
                            trailingScopeWidth = 0.dp
                            leadingScopeWidth = 0.dp
                            Log.d("SwipeActionTag", "HorizontalSwipeAction: nan ")
                        }
                    },
                    onHorizontalDrag = { change, value ->
                        if (change.isConsumed) {
                            startPosition = change.previousPosition - currentPosition
                            currentPosition = change.position - startPosition
                            Log.d("SwipeActionTag", "${trailingContent!=null}  ${leadingContent != null}  ")

                        }

                        if (trailingContent != null) {
                            Log.d("SwipeActionTag", "trailingContent:  ${currentPosition.x}")
                            if (currentPosition.x < 0) {
                                Log.d("SwipeActionTag", "trailingContent:  ${currentPosition.x}")
                                isDragging = true
                                currentPosition = change.position - startPosition
                                thresholdsValuePx =
                                    trailingContentThresholds?.toPx() ?: (scopeWidth / 2).toPx()
                                trailingScopeWidth -= value.toDp()
                            }
                        }
                        if (leadingContent != null) {
                            Log.d("SwipeActionTag", "leadingContent:  ${currentPosition.x}")
                            if (currentPosition.x > 0) {
                                Log.d("SwipeActionTag", "leadingContent:  ${currentPosition.x}")
                                isDragging = true
                                currentPosition = change.position - startPosition
                                thresholdsValuePx =
                                    leadingContentThresholds?.toPx() ?: (scopeWidth / 2).toPx()
                                leadingScopeWidth += value.toDp()
                            }
                        }


                    }
                )
            }
            .background(trailingContentBackgroundColor ?: Color.Transparent),
        propagateMinConstraints = false,
        contentAlignment = contentAlignment
    ) {

        Box(
            contentAlignment = Alignment.CenterStart,
            content = { content() },
            modifier = Modifier
                .graphicsLayer(translationX = currentPosition.x)
                .zIndex(zIndex = 0.1f)
        )

        if (trailingContent != null) {
            if (isDragging || isTrailing) {
                Box(
                    content = { trailingContent() },
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .heightIn(max = if (scaledBackground) scopeHeight else Dp.Unspecified)
                        .widthIn(max = if (scaledBackground) scopeWidth else Dp.Unspecified)
                        .width(trailingScopeWidth)
                        .graphicsLayer(translationX = scopeWidthPx + currentPosition.x)
                        .background(leadingContentBackgroundColor ?: Color.Unspecified)
                )
            }
        }


        if (leadingContent != null) {
            if (isDragging || isLeading) {
                Box(
                    content = { leadingContent() },
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .heightIn(max = if (scaledBackground) scopeHeight else Dp.Unspecified)
                        .widthIn(max = if (scaledBackground) scopeWidth else Dp.Unspecified)
                        .width(leadingScopeWidth)
                        .background(leadingContentBackgroundColor ?: Color.Unspecified)
                )
            }
        }

    }
}