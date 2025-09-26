package com.cevichepicante.composescrollshadow

import android.util.Log
import androidx.annotation.FloatRange
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.layout.boundsInRoot
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

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
//        list (470.0, 84.0, 611.0, 2205.0)
//        content (728.0, 1111.0, 916.0, 1179.0)
            val listHorizontalRange = listRect.left.toDouble().rangeTo(listRect.right.toDouble())
            val listVerticalRange = listRect.top.toDouble().rangeTo(listRect.bottom.toDouble())

            val onHorizontalAxis = floatingContentRect!!.left in listHorizontalRange || floatingContentRect!!.right in listHorizontalRange
            val onVerticalAxis = floatingContentRect!!.top in listVerticalRange || floatingContentRect!!.bottom in listVerticalRange
            Log.d("JSY", "contentOnAxis? hor: $onHorizontalAxis ver: $onVerticalAxis")

            if(onHorizontalAxis && onVerticalAxis) {
                alignedOnAxis = true
            } else {
                //  content 너비 및 높이가 list 보다 클 수 경우 고려
                floatingContentRect?.let {
                    val horizontalRange = it.left.toDouble().rangeTo(it.right.toDouble())
                    val verticalRange = it.top.toDouble().rangeTo(it.bottom.toDouble())
                    val listOnHorizontalAxis = listRect.left in horizontalRange || listRect.right in horizontalRange
                    val listOnVerticalAxis = listRect.top in verticalRange || listRect.bottom in verticalRange

                    Log.d("JSY", "listOnAxis? hor: $listOnHorizontalAxis ver: $listOnVerticalAxis")
                    alignedOnAxis = listOnHorizontalAxis || listOnVerticalAxis
                    return@LaunchedEffect
                }
                alignedOnAxis = false
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
            key2 = floatingContentRect
        ) {
            Log.d("JSY", "Aligned on axis")
            snapshotFlow {
                listState.firstVisibleItemScrollOffset
            }.map {
                /*
                    LazyList 에 contentPadding 이 있는 경우
                    first visible item offset 계산 시 포함
                 */
                val visibleList = listState.layoutInfo.visibleItemsInfo
                visibleList.firstOrNull()?.let { firstVisible ->
                    val topPaddingPx = listState.layoutInfo.beforeContentPadding
                    val offset = if(topPaddingPx > 0) {
                        firstVisible.offset.plus(topPaddingPx).let {
                            if (it < 0) {
                                0
                            } else {
                                it
                            }
                        }
                    } else {
                        firstVisible.offset
                    }
                    return@map offset
                }
                null
            }.collect { offset ->

                if(offset == null) {
                    overLayered = false
                    return@collect
                }

                floatingContentRect?.let {
                    val inVerticalRange = (offset > it.top) && (offset < it.bottom)

                    if(inVerticalRange) {
                        overLayered = true
                    } else {
                        //  list 의 first visible item 이 타겟 composable 보다 앞에 있는지 확인
                        val isListAhead = offset < it.top
                        if(isListAhead) {
                            val itemSize = listState.layoutInfo.visibleItemsInfo.firstOrNull()?.size?: 0
                            val firstVisibleIndex = listState.layoutInfo.visibleItemsInfo.firstOrNull()?.index?: 0
                            val backwardItemCount = listState.layoutInfo.totalItemsCount.minus(firstVisibleIndex.plus(1))
                            val distanceFirstOffsetToContent = it.top.minus(offset)
                            val listIsStillLong = (backwardItemCount * itemSize) > distanceFirstOffsetToContent

                            Log.d(
                                "JSY",
                                "(distance: $distanceFirstOffsetToContent) " +
                                        "(items: ${backwardItemCount.times(itemSize)} = $itemSize * ${backwardItemCount}개)"
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