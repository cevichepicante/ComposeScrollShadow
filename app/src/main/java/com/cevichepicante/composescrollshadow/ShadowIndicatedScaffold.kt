package com.cevichepicante.composescrollshadow

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier

enum class HidingPositionIndex {
    FIRST, LAST,
}

@Composable
fun ShadowIndicatedScaffold(
    hidingShadowIndex: HidingPositionIndex,
    listState: LazyListState,
    shadowSettings: AdvancedShadowSettings,
    modifier: Modifier = Modifier,
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
        modifier = modifier
            .advancedShadow(
                shape = shadowSettings.shape,
                color = shadowSettings.color,
                blurDp = shadowSettings.blurDp,
                offsetX = shadowSettings.offsetX,
                offsetY = shadowSettings.offsetY,
                visible = showShadow
            )
    ) {
        content()
    }
}