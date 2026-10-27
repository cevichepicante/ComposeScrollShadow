package com.cevichepicante.composescrollshadow

import androidx.compose.foundation.gestures.ScrollableState
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.cevichepicante.composescrollshadow.data.ShadowSettings

enum class HidingShadowPosition {
    FIRST, LAST,
}

/**
 * This scaffold adds [content] shadowed layer depends on scrolling state.
 *
 * @param hidingShadowIndex is the part of scrollable content where it reached to hide shadow
 * @param scrollableState state of the scrollable content such as
 * LazyListState, LazyGridState or ScrollState
 * @param shadowSettings for style of shadowed layer
 * @param content Composable which will be drew with shadow
 *
 */

@Composable
fun ShadowIndicatedScrollScaffold(
    hidingShadowIndex: HidingShadowPosition,
    scrollableState: ScrollableState,
    shadowSettings: ShadowSettings,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    val showShadow by remember(
        hidingShadowIndex,
        scrollableState
    ) {
        derivedStateOf {
            when(hidingShadowIndex) {
                HidingShadowPosition.FIRST -> {
                    scrollableState.canScrollBackward
                }
                HidingShadowPosition.LAST -> {
                    scrollableState.canScrollForward
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
                sideType = shadowSettings.sideType,
                visible = showShadow
            )
    ) {
        content()
    }
}