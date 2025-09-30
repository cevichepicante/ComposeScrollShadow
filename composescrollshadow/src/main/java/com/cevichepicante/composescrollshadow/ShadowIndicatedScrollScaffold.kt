package com.cevichepicante.composescrollshadow

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.cevichepicante.composescrollshadow.data.ShadowSettingData

enum class HidingShadowPosition {
    FIRST, LAST,
}

/**
 * This scaffold adds [content] shadow layer depends on list's scrolling state.
 *
 * @param hidingShadowIndex is the part of LazyList where the list reached to make shadow invisible
 * @param shadowSettings for style of shadow layer
 * @param content Composable which will be drew with shadow
 *
 */
@Composable
fun ShadowIndicatedScrollScaffold(
    hidingShadowIndex: HidingShadowPosition,
    listState: LazyListState,
    shadowSettings: ShadowSettingData,
    content: @Composable () -> Unit
) {
    val showShadow by remember(
        hidingShadowIndex,
        listState
    ) {
        derivedStateOf {
            when(hidingShadowIndex) {
                HidingShadowPosition.FIRST -> {
                    val index = listState.firstVisibleItemIndex
                    val offset = listState.firstVisibleItemScrollOffset

                    if(offset != 0) {
                        true
                    } else if(index != 0) {
                        true
                    } else {
                        false
                    }
                } HidingShadowPosition.LAST -> {
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
                sideType = shadowSettings.sideType,
                visible = showShadow
            )
    ) {
        content()
    }
}