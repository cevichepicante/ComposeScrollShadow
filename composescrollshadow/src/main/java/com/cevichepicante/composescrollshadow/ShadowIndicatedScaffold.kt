package com.cevichepicante.composescrollshadow

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp

enum class HidingPositionIndex {
    FIRST, LAST,
}

data class AdvancedShadowSettings(
    val shape: Shape,
    val color: Color,
    val blurDp: Dp,
    val offsetX: Int,
    val offsetY: Int,
    val clipToShape: Boolean
)

@Composable
fun ShadowIndicatedScaffold(
    hidingShadowIndex: HidingPositionIndex,
    listState: LazyListState,
    shadowSettings: AdvancedShadowSettings,
    content: @Composable () -> Unit
) {
    val showShadow by remember(
        hidingShadowIndex,
        listState
    ) {
        derivedStateOf {
            when(hidingShadowIndex) {
                HidingPositionIndex.FIRST -> {
                    listState.firstVisibleItemScrollOffset != 0
                } HidingPositionIndex.LAST -> {
                    listState.canScrollForward
                }
            }
        }
    }

    Box(
        modifier = Modifier
            .wrapContentSize()
            .advancedShadow(
                shape = shadowSettings.shape,
                color = shadowSettings.color,
                blurDp = shadowSettings.blurDp,
                offsetX = shadowSettings.offsetX,
                offsetY = shadowSettings.offsetY,
                clipToShape = shadowSettings.clipToShape,
                visible = showShadow
            )
    ) {
        content()
    }
}