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
    abstract val offsetX: Float
    abstract val offsetY: Float

    data class AllSide(
        override val offsetX: Float,
        override val offsetY: Float
    ): ShadowSideType()

    data class SingleSide(
        override val offsetX: Float,
        override val offsetY: Float,
        val direction: ShadowSideDirection,
        val drawInner: Boolean,
        val clipToBounds: Boolean
    ): ShadowSideType()
}

enum class ShadowSideDirection {
    Left, Top, Right, Bottom
}