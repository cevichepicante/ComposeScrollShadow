package com.cevichepicante.composescrollshadow.data

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp

data class ShadowSettings(
    val shape: Shape,
    val color: Color,
    val blurDp: Dp,
    val sideType: ShadowSideType
)

/**
 * The shadow type defined based on the side where the shadow appears.
 */
sealed class ShadowSideType {
    /**
     * Shadow will appear surrounding the content.
     */
    data class AllSide(
        val offsetX: Float,
        val offsetY: Float
    ): ShadowSideType()

    /**
     * Shadow will appear on the only side.
     *
     * @param direction the direction of the side where shadow will be showed.
     * @param drawInner When `True` shadow will be positioned within content size bounds
     * and drawn over the content.
     *
     */
    data class SingleSide(
        val direction: ShadowSideDirection,
        val drawInner: Boolean,
    ): ShadowSideType()
}

enum class ShadowSideDirection {
    Left, Top, Right, Bottom
}