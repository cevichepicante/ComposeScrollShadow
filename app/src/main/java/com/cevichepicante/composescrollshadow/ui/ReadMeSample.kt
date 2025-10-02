package com.cevichepicante

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import com.cevichepicante.composescrollshadow.HidingShadowPosition
import com.cevichepicante.shadowindicatedlist.R
import com.cevichepicante.composescrollshadow.ShadowIndicatedScrollScaffold
import com.cevichepicante.composescrollshadow.advancedShadow
import com.cevichepicante.composescrollshadow.data.ShadowSettings
import com.cevichepicante.composescrollshadow.data.ShadowSideDirection
import com.cevichepicante.composescrollshadow.data.ShadowSideType
import com.cevichepicante.composescrollshadow.ui.theme.ChatScreenContainer
import com.cevichepicante.composescrollshadow.ui.theme.ChatScreenMyMessage
import com.cevichepicante.composescrollshadow.ui.theme.ChatScreenMyMessageBox
import com.cevichepicante.composescrollshadow.ui.theme.ChatScreenShadow
import com.cevichepicante.composescrollshadow.ui.theme.ChatScreenUserMessage
import com.cevichepicante.composescrollshadow.ui.theme.ChatScreenUserMessageBox
import com.cevichepicante.composescrollshadow.ui.theme.ChatScreenUserText

@Composable
fun ReadMeSampleChatScreen(
    modifier: Modifier = Modifier
) {
    val listState = rememberLazyListState()
    val list = listOf(
        "What's your fav taco",
        "For me tripa",
        "What about birilla",
        "Ohh",
        "Birilla with culantro on it",
        "And soaking it with soup",
        "Taco for dinner?",
        "Cool",
        "Let's get them",
        "See you at 8",
        "Where?",
        "Where you at now?",
        "I'm working",
        "Text me when you leaving",
        "Yeah I'll call you",
        "Good"
    )

    LaunchedEffect(listState) {
        listState.scrollToItem(list.lastIndex)
    }

    Column(
        modifier = modifier
            .background(ChatScreenContainer)
    ) {
        ChatUser(
            modifier = Modifier
                .height(65.dp)
        )
        ChatList(
            list = list,
            listState = listState,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        )
        ChatInput(
            listState = listState,
            modifier = Modifier.height(60.dp)
        )
    }
}

@Composable
fun ChatUser(
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .background(ChatScreenContainer)
            .padding(horizontal = 20.dp, vertical = 10.dp)
    ) {
        Text(
            text = "U",
            color = Color.Black.copy(alpha = 0.5f),
            fontWeight = FontWeight(600),
            modifier = Modifier
                .size(35.dp)
                .background(Color.Blue.copy(alpha = 0.2f), CircleShape)
                .padding(horizontal = 12.dp, vertical = 6.dp)
        )
        Text(
            text = "Uber",
            fontWeight = FontWeight(500),
            fontSize = 20.asDp(),
            color = ChatScreenUserText,
            modifier = Modifier
                .padding(start = 10.dp)
        )
    }
}

@Composable
fun ChatList(
    list: List<String>,
    listState: LazyListState,
    modifier: Modifier = Modifier
) {
    LaunchedEffect(listState) {
        listState.scrollToItem(0)
    }

    ShadowIndicatedScrollScaffold(
        hidingShadowIndex = HidingShadowPosition.FIRST,
        listState = listState,
        modifier = modifier,
        shadowSettings = ShadowSettings(
            shape = RectangleShape,
            color = ChatScreenShadow,
            blurDp = 15.dp,
            sideType = ShadowSideType.SingleSide(
                direction = ShadowSideDirection.Top,
                drawInner = true
            )
        )
    ) {
        LazyColumn(
            state = listState,
            contentPadding = PaddingValues(bottom = 15.dp),
            modifier = modifier
                .background(ChatScreenContainer)
                .padding(horizontal = 20.dp)
        ) {
            itemsIndexed(
                list
            ) { pos, item ->
                val myMsg = pos % 2 == 0
                Spacer(
                    modifier = Modifier.height(20.dp)
                )
                Column (
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = if(myMsg) {
                        Alignment.End
                    } else {
                        Alignment.Start
                    }
                ) {
                    Text(
                        text = item,
                        textAlign = if(myMsg) {
                            TextAlign.End
                        } else {
                            TextAlign.Start
                        },
                        color = if(myMsg) {
                            ChatScreenMyMessage
                        } else {
                            ChatScreenUserMessage
                        },
                        fontSize = 15.asDp(),
                        modifier = Modifier
                            .widthIn(10.dp, 220.dp)
                            .background(
                                shape = RoundedCornerShape(10.dp),
                                color = if(myMsg) {
                                    ChatScreenMyMessageBox
                                } else {
                                    ChatScreenUserMessageBox
                                }
                            )
                            .padding(vertical = 2.dp, horizontal = 10.dp)
                    )
                    val minute = if(pos < 5) {
                        38
                    } else {
                        39
                    }
                    Text(
                        text = "7:$minute PM",
                        color = Color.Black.copy(alpha = 0.6f),
                        fontSize = 10.asDp(),
                        lineHeight = 18.asDp(),
                        modifier = Modifier
                            .padding(horizontal = 5.dp)
                    )
                }

            }
        }
    }
}

@Composable
fun ChatInput(
    listState: LazyListState,
    modifier: Modifier = Modifier
) {
    ShadowIndicatedScrollScaffold(
        hidingShadowIndex = HidingShadowPosition.LAST,
        listState = listState,
        modifier = modifier,
        shadowSettings = ShadowSettings(
            shape = RectangleShape,
            color = ChatScreenShadow,
            blurDp = 15.dp,
            sideType = ShadowSideType.SingleSide(
                direction = ShadowSideDirection.Top,
                drawInner = false
            )
        )
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(15.dp),
            modifier = modifier
                .background(ChatScreenContainer)
                .padding(vertical = 13.dp, horizontal = 20.dp)
        ) {
            BasicTextField(
                value = "",
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .background(
                        color = ChatScreenUserMessageBox,
                        shape = RoundedCornerShape(20.dp)
                    ),
                onValueChange = {},
            )

            Text(
                text = "+",
                fontSize = 30.asDp(),
                color = Color.Gray
            )
        }
    }
}

@Composable
fun ReadMeSampleSwipingPhoto(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .padding(20.dp)
    ) {
        Row(
            modifier = Modifier.padding(top = 20.dp)
        ) {
            Image(
                painter = painterResource(R.drawable.back),
                contentDescription = null,
                modifier = Modifier.size(14.dp)
            )
            Text(
                text = "Wood",
                textAlign = TextAlign.Center,
                fontSize = 18.asDp(),
                fontWeight = FontWeight(500),
                modifier = Modifier.weight(1f)
            )
            Image(
                painter = painterResource(R.drawable.back),
                contentDescription = null,
                modifier = Modifier.size(14.dp)
                    .rotate(180f)
            )
        }
        SwipingPhotoArea(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 25.dp)
        )
        PhotoControl(
            modifier = Modifier
                .padding(horizontal = 25.dp)
        )
    }
}

@Composable
fun SwipingPhotoArea(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
    ) {
        Image(
            painter = painterResource(R.drawable.wood_furniture_sec),
            contentDescription = null,
            modifier = Modifier
                .rotate(180f)
                .padding(horizontal = 20.dp)
                .fillMaxWidth()
                .height(350.dp)
                .clip(RoundedCornerShape(18.dp)),
            contentScale = ContentScale.Crop
        )

        Box(
            modifier = Modifier
                .padding(top = 20.dp)
                .advancedShadow(
                    shape = RoundedCornerShape(18.dp),
                    color = Color.Black,
                    blurDp = 10.dp,
                    sideType = ShadowSideType.AllSide(0f, 0f),
                    visible = true
                )
        ) {
            Image(
                painter = painterResource(R.drawable.wood_furniture),
                contentDescription = null,
                modifier = Modifier
                    .height(350.dp)
                    .clip(RoundedCornerShape(18.dp)),
                contentScale = ContentScale.Crop
            )
        }
    }
}

@Composable
fun PhotoControl(
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .padding(bottom = 20.dp)
    ) {
        Text(
            text = "3/20",
            fontSize = 13.asDp(),
            color = Color.Gray
        )

        Column(
            modifier = Modifier.weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "HandCrafted Table + Chair",
                textAlign = TextAlign.Center,
                fontSize = 15.asDp(),
                modifier = Modifier.fillMaxWidth()
            )
            Text(
                text = "visit site",
                fontSize = 12.asDp(),
                lineHeight = 12.asDp(),
                textAlign = TextAlign.Center,
                color = Color.Gray,
                modifier = Modifier.wrapContentWidth()
            )
            HorizontalDivider(
                thickness = 1.dp,
                color = Color.Gray.copy(alpha = 0.3f),
                modifier = Modifier.width(50.dp)
            )
        }

        Image(
            painter = painterResource(R.drawable.bookmark),
            contentDescription = null,
            modifier = Modifier.width(20.dp)
        )
    }
}

@Composable
fun Int.asDp(): TextUnit {
    with(LocalDensity.current) {
        return this@asDp.dp.toSp()
    }
}