package com.cevichepicante.composescrollshadow

import android.util.Log
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.layout.boundsInRoot
import androidx.compose.ui.layout.onGloballyPositioned
import kotlinx.coroutines.flow.map

data class PassingListInfo(
    val orientation: Orientation,
    val listRect: Rect?
)

@Composable
fun ShadowIndicatedFloatingScaffold(
    listState: LazyListState,
    passingListInfo: PassingListInfo,
    shadowSettings: AdvancedShadowSettings,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    val listItemsCount by remember(listState) {
        mutableIntStateOf(listState.layoutInfo.totalItemsCount)
    }
    var floatingContentRect by remember {
        mutableStateOf<Rect?>(null)
    }
    var overLayered by remember {
        mutableStateOf(false)
    }
    var alignedOnAxis by remember {
        mutableStateOf(false)
    }

    /*
        LazyList 와 floating composable 이
        서로 겹칠 수 있는 축에 있는지 확인
     */
    LaunchedEffect(
        key1 = passingListInfo,
        key2 = floatingContentRect
    ) {
        val listRect = passingListInfo.listRect
        if(listRect != null && floatingContentRect != null) {
            val contentOnHorizontalAxis = floatingContentRect!!.isInRange(
                listRect,
                OffsetRangeOrientation.Horizontal
            )
            val contentOnVerticalAxis = floatingContentRect!!.isInRange(
                listRect,
                OffsetRangeOrientation.Vertical
            )

            if(contentOnHorizontalAxis && contentOnVerticalAxis) {
                alignedOnAxis = true
            } else {
                floatingContentRect!!.let {
                    val listOnHorizontalAxis = listRect.isInRange(
                        it,
                        OffsetRangeOrientation.Horizontal
                    )
                    val listOnVerticalAxis = listRect.isInRange(
                        it,
                        OffsetRangeOrientation.Vertical
                    )

                    alignedOnAxis = listOnHorizontalAxis || listOnVerticalAxis
                }
            }
        } else {
            alignedOnAxis = false
        }
    }

    /*
        겹쳐진 상태 계산하여 shadow visibility 갱신
     */
    if(alignedOnAxis) {
        LaunchedEffect(
            key1 = listState,
            key2 = floatingContentRect,
            key3 = listItemsCount
        ) {
            snapshotFlow {
                listState.firstVisibleItemScrollOffset
            }.map {
                /*
                    LazyList 에 contentPadding 이 있는 경우
                    first visible item offset 계산 시 포함
                 */
                val firstVisible = listState.layoutInfo.visibleItemsInfo.firstOrNull()
                if(firstVisible != null) {
                    val topPaddingPx = listState.layoutInfo.beforeContentPadding
                    val offset = if(topPaddingPx > 0) {
                        firstVisible.offset.plus(topPaddingPx).coerceAtLeast(0)
                    } else {
                        firstVisible.offset
                    }
                    Pair(firstVisible.index, offset)
                } else {
                    null
                }
            }.collect { pair ->

                if(pair == null) {
                    overLayered = false
                    return@collect
                }

                val offset = pair.second
                val index = pair.first

                floatingContentRect?.let {
                    val verticalRange = it.getOffsetRange(OffsetRangeOrientation.Vertical)
                    if(offset.toDouble() in verticalRange) {
                        overLayered = true
                    } else {
                        val isListAhead = offset < it.top
                        if(isListAhead) {
                            val itemHeight = listState.layoutInfo.visibleItemsInfo.firstOrNull()?.size?: 0
                            val backwardItemCount = listItemsCount.minus(index.plus(1))
                            val gap = it.top.minus(offset)
                            val listIsStillLong = (backwardItemCount * itemHeight) > gap

                            Log.d(
                                "JSY",
                                "(distance: $gap) " +
                                        "(items: ${backwardItemCount.times(itemHeight)} " +
                                        "= $itemHeight * ${backwardItemCount}개)"
                            )
                            overLayered = listIsStillLong
                        } else {
                            overLayered = false
                        }
                    }
                }
            }
        }
    }

    Box(
        modifier = modifier
            .wrapContentSize()
            .onGloballyPositioned {
                //  자신의 offset 확인
                floatingContentRect = it.parentCoordinates?.boundsInRoot()
            }
            .advancedShadow(
                shape = shadowSettings.shape,
                color = shadowSettings.color,
                blurDp = shadowSettings.blurDp,
                offsetX = shadowSettings.offsetX,
                offsetY = shadowSettings.offsetY,
                clipToShape = shadowSettings.clipToShape,
                visible = overLayered
            )
    ) {
        content()
    }
}

enum class OffsetRangeOrientation {
    Horizontal, Vertical
}

private fun Rect.isInRange(rect: Rect, orientation: OffsetRangeOrientation): Boolean {
    val horizontalRange = rect.getOffsetRange(OffsetRangeOrientation.Horizontal)
    val verticalRange = rect.getOffsetRange(OffsetRangeOrientation.Vertical)
    return when(orientation) {
        OffsetRangeOrientation.Horizontal -> {
            left in horizontalRange || right in horizontalRange
        }
        OffsetRangeOrientation.Vertical -> {
            top in verticalRange || bottom in verticalRange
        }
    }
}

private fun Rect.getOffsetRange(orientation: OffsetRangeOrientation): ClosedFloatingPointRange<Double> {
    return when(orientation) {
        OffsetRangeOrientation.Horizontal -> {
            left.toDouble().rangeTo(right.toDouble())
        }
        OffsetRangeOrientation.Vertical -> {
            top.toDouble().rangeTo(bottom.toDouble())
        }
    }
}