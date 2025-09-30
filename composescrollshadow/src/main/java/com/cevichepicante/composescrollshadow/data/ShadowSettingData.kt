package com.cevichepicante.composescrollshadow.data

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp

data class ShadowSettingData(
    val shape: Shape,
    val color: Color,
    val blurDp: Dp,
    val sideType: ShadowSideType
)

sealed class ShadowSideType {
    data class AllSide(
        val offsetX: Float,
        val offsetY: Float
    ): ShadowSideType()

    data class SingleSide(
        val direction: ShadowSideDirection,
        val drawInner: Boolean,
        val clipToBounds: Boolean
    ): ShadowSideType()
}

enum class ShadowSideDirection {
    Left, Top, Right, Bottom
}