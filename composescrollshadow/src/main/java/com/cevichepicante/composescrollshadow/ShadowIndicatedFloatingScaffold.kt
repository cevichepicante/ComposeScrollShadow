package com.cevichepicante.composescrollshadow

import android.util.Log
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyListItemInfo
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
import com.cevichepicante.composescrollshadow.data.ShadowSettingData
import kotlinx.coroutines.flow.map

private enum class OffsetRangeOrientation {
    Horizontal, Vertical
}

data class PassingListInfo(
    /**
     * [Orientation.Vertical] for LazyColumn,
     * [Orientation.Horizontal] for LazyRow
     */
    val orientation: Orientation,

    /**
     * Bound of LazyList composable
     *
     * To get Rect of LazyList, there is one of many options below.
     * ```
     * Modifier
     * .width(100.dp)
     * .onGloballyPositioned {
     *      it.parentCoordinates?.boundsInRoot() // Here!
     * }
     * ```
     */
    val listRect: Rect?
)

/**
 * This scaffold adds [content] shadow layer when [content] is over-layered on LazyList.
 *
 * @param passingListInfo has orientation and Rect info of LazyList
 * @param shadowSettings for style of shadow layer
 * @param content Composable which will be drew with shadow
 *
 */
@Composable
fun ShadowIndicatedFloatingScaffold(
    listState: LazyListState,
    passingListInfo: PassingListInfo,
    shadowSettings: ShadowSettingData,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    val isVerticalList = passingListInfo.orientation == Orientation.Vertical

    var floatingContentRect by remember { mutableStateOf<Rect?>(null) }
    var overLayered by remember { mutableStateOf(false) }
    var alignedOnAxis by remember { mutableStateOf(false) }

    /**
     * Checks if [content] and LazyList which [listState] manages are aligned on
     * same axis horizontally and vertically.
     * If they are not on the same axis and never over-layered each other,
     * calculation of offsets of those two [Composable]s doesn't need to be executed.
     *
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

    if(alignedOnAxis) {

        /**
         * Calculates offset of LazyList when LazyList is scrolled, then check relation of offset
         * between LazyList and [content] to update shadow's visibility
         *
         */
        LaunchedEffect(
            key1 = listState,
            key2 = floatingContentRect,
        ) {
            snapshotFlow {
                listState.firstVisibleItemScrollOffset
            }.map {
                /**
                 * If LazyList has content padding, offset of first visible item is not accurate.
                 * Therefore, the offset is emitted as value with before padding value added to it.
                 *
                 */
                val firstVisible = listState.layoutInfo.visibleItemsInfo.firstOrNull()
                if(firstVisible != null) {
                    val headPaddingPx = listState.layoutInfo.beforeContentPadding
                    val offset = if(headPaddingPx > 0) {
                        firstVisible.offset.plus(headPaddingPx).coerceAtLeast(0)
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

                val index = pair.first
                val offset = pair.second

                floatingContentRect?.let {
                    val verticalRange = it.getOffsetRange(
                        if(isVerticalList) {
                            OffsetRangeOrientation.Vertical
                        } else {
                            OffsetRangeOrientation.Horizontal
                        }
                    )
                    if(offset.toDouble() in verticalRange) {
                        overLayered = true
                    } else {
                        val isListAhead = if(isVerticalList) {
                            offset < it.top
                        } else {
                            offset < it.left
                        }
                        if(isListAhead) {
                            /*
                                The itemSize is
                                for LazyColumn the height of item, for LazyRow the width of item.
                             */
                            val itemSize = listState.layoutInfo.visibleItemsInfo.firstOrNull()?.size?: 0
                            val totalItemCount = listState.layoutInfo.totalItemsCount
                            val backwardItemCount = totalItemCount.minus(index.plus(1))
                            val gap = if(isVerticalList) {
                                it.top.minus(offset)
                            } else {
                                it.left.minus(offset)
                            }
                            val listIsStillLong = (backwardItemCount * itemSize) > gap

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
                floatingContentRect = it.parentCoordinates?.boundsInRoot()
            }
            .advancedShadow(
                shape = shadowSettings.shape,
                color = shadowSettings.color,
                blurDp = shadowSettings.blurDp,
                sideType = shadowSettings.sideType,
                visible = overLayered
            )
    ) {
        content()
    }
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