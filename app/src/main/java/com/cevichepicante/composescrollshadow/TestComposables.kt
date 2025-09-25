package com.cevichepicante.composescrollshadow

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

/**
 * Test Composable 1
 *
 * 1..50 LazyColumn 위에 Text
 * 목록 스크롤 상태일 떄 Text 배경에 Shadow 표시
 */

@Composable
fun TextShadowWhenListScrolling() {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
    ) {
        val listState = rememberLazyListState()
        TestBackgroundList(listState)
        ShadowIndicatedScaffold(
            hidingShadowIndex = HidingPositionIndex.FIRST,
            listState = listState,
            shadowSettings = AdvancedShadowSettings(
                shape = RoundedCornerShape(8.dp),
                color = Color.Gray,
                blurDp = 15.dp,
                offsetX = 4,
                offsetY = 7,
                clipToShape = false
            )
        ) {
            ShadowSetText()
        }
    }
}

@Composable
fun ShadowSetText() {
    Text(
        text = "hello",
        modifier = Modifier
            .background(
                color = Color.Yellow,
                shape = RoundedCornerShape(8.dp)
            )
            .padding(horizontal = 15.dp)
    )
}

@Composable
fun TestBackgroundList(listState: LazyListState) {
    val list = IntRange(1, 50).toList()
    LazyColumn(
        state = listState
    ) {
        items(
            items = list
        ) {
            Text(
                text = it.toString(),
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 20.dp)
            )
            HorizontalDivider(
                thickness = 1.dp,
                color = Color.Gray
            )
        }
    }
}

/**
 * Test Composable 2
 *
 * 최하단 입력창, 나머지 영역 LazyColumn
 * LazyColumn 마지막 항목 scrolled 처음 상태
 */
@Composable
fun LazyColumnLastItemVisibleWithTextField() {
    val listState = rememberLazyListState()

    Column {
        LazyColumnLastItemVisible(
            listState = listState,
            modifier = Modifier.weight(1f)
        )
        ShadowIndicatedScaffold(
            hidingShadowIndex = HidingPositionIndex.LAST,
            listState = listState,
            shadowSettings = AdvancedShadowSettings(
                shape = RectangleShape,
                color = Color.Gray,
                blurDp = 10.dp,
                offsetX = 0,
                offsetY = -8,
                clipToShape = true
            )
        ) {
            TextField(
                modifier = Modifier.fillMaxWidth(),
                value = "",
                onValueChange = {}
            )
        }
    }
}

@Composable
fun LazyColumnLastItemVisible(
    modifier: Modifier = Modifier,
    listState: LazyListState
) {
    val list = IntRange(1, 50).toList()

    LaunchedEffect(listState) {
        listState.scrollToItem(list.lastIndex)
    }

    LazyColumn(
        state = listState,
        modifier = modifier
    ) {
        items(
            items = list
        ) {
            Text(
                text = it.toString(),
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 20.dp)
            )
            HorizontalDivider(
                thickness = 1.dp,
                color = Color.Gray
            )
        }
    }
}

/**
 *  Test Composable 3
 *
 *  최우측 버튼, 그 좌측으로 나머지 영역에 LazyRow
 *  LazyRow 스크롤 상태인 경우 버튼과 목록 경계에 shadow 표시
 */
@Composable
fun LazyRowWithRightButton() {
    val listState = rememberLazyListState()
    Row(
        modifier = Modifier
            .background(Color.White)
    ) {
        TestLazyRow(
            listState = listState,
            modifier = Modifier.weight(1f)
        )
        ShadowIndicatedScaffold(
            hidingShadowIndex = HidingPositionIndex.LAST,
            listState = listState,
            shadowSettings = AdvancedShadowSettings(
                shape = RectangleShape,
                color = Color.Gray,
                blurDp = 15.dp,
                offsetX = -8,
                offsetY = 0,
                clipToShape = true
            ),
        ) {
            Text(
                text = "검색",
                modifier = Modifier.background(Color.White)
                    .wrapContentWidth()
                    .padding(5.dp)
            )
        }
    }
}

@Composable
fun TestLazyRow(
    listState: LazyListState,
    modifier: Modifier
) {
    val list = listOf("아우터", "상의", "하의", "신발", "가방", "양말")
    LazyRow(
        modifier = modifier,
        state = listState,
        contentPadding = PaddingValues(start = 20.dp)
    ) {
        items(
            items = list
        ) {
            Text(
                text = it,
                modifier = Modifier
                    .wrapContentSize()
                    .background(
                        color = Color.Yellow,
                        shape = RoundedCornerShape(100.dp)
                    )
                    .padding(vertical = 5.dp, horizontal = 15.dp)
            )
            Spacer(
                modifier = Modifier.width(5.dp)
            )
        }
    }
}

/**
 * Test Composable 4
 *
 * LazyColumn with buttons at the each side vertically
 * Shadows appear at the top and also bottom
 */
@Composable
fun LazyColumnWithButtonsVertically() {
    val list = IntRange(1, 50).toList()
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier.width(40.dp)
    ) {
        ShadowIndicatedScaffold(
            hidingShadowIndex = HidingPositionIndex.FIRST,
            listState = listState,
            shadowSettings = AdvancedShadowSettings(
                shape = RectangleShape,
                color = Color.Gray,
                blurDp = 15.dp,
                offsetX = 0,
                offsetY = 8,
                clipToShape = true
            )
        ) {
            ScrollListButton(
                toUp = true,
                onClick = {
                    coroutineScope.launch {
                        listState.animateScrollToItem(0)
                    }
                }
            )
        }

        LazyColumnBetweenOfButtons(
            list = list,
            listState = listState,
            modifier = Modifier.weight(1f)
        )

        ShadowIndicatedScaffold(
            hidingShadowIndex = HidingPositionIndex.LAST,
            listState = listState,
            shadowSettings = AdvancedShadowSettings(
                shape = RectangleShape,
                color = Color.Gray,
                blurDp = 15.dp,
                offsetX = 0,
                offsetY = -8,
                clipToShape = true
            )
        ) {
            ScrollListButton(
                toUp = false,
                onClick = {
                    coroutineScope.launch {
                        listState.animateScrollToItem(list.lastIndex)
                    }
                }
            )
        }
    }
}

@Composable
fun ScrollListButton(
    toUp: Boolean,
    onClick: () -> Unit
) {
    Text(
        text = "^",
        textAlign = TextAlign.Center,
        modifier = Modifier
            .clickable {
                onClick()
            }
            .fillMaxWidth()
            .background(Color.Yellow)
            .padding(vertical = 5.dp)
            .rotate(
                if(toUp) {
                    0.0f
                } else {
                    180.0f
                }
            )
    )
}

@Composable
fun LazyColumnBetweenOfButtons(
    list: List<Any>,
    listState: LazyListState,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        state = listState,
        modifier = modifier
    ) {
        items(
            items = list
        ) {
            Text(
                text = it.toString(),
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 5.dp)
            )
            HorizontalDivider(
                thickness = 1.dp,
                color = Color.Gray
            )
        }
    }
}