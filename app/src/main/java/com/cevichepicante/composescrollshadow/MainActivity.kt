package com.cevichepicante.composescrollshadow

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.boundsInRoot
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cevichepicante.composescrollshadow.ui.theme.ComposeScrollShadowTheme

const val density = 2.8125

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ComposeScrollShadowTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .padding(innerPadding)
                            .fillMaxSize()
                    ) {
                        var listRect by remember {
                            mutableStateOf<Rect?>(null)
                        }
                        val passingListInfo by remember(listRect) {
                            mutableStateOf(
                                PassingListInfo(Orientation.Horizontal, listRect)
                            )
                        }
                        val listState = rememberLazyListState()

                        TestLazyRow(
                            listState = listState,
                            modifier = Modifier
                                .wrapContentHeight()
                                .onGloballyPositioned {
                                    listRect = it.parentCoordinates?.boundsInRoot()
                                }
                        )
                        ShadowIndicatedFloatingScaffold(
                            listState = listState,
                            passingListInfo = passingListInfo,
                            modifier = Modifier.padding(top = 50.dp),
                            shadowSettings = AdvancedShadowSettings(
                                shape = RectangleShape,
                                color = Color.Gray,
                                blurDp = 15.dp,
                                offsetX = 4,
                                offsetY = 4,
                                clipToShape = false
                            )
                        ) {
                            ShadowSetText()
                        }
                    }
                }
            }
        }
    }
}