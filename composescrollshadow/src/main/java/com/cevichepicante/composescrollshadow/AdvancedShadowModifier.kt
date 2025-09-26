package com.cevichepicante.composescrollshadow

import android.graphics.BlurMaskFilter
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.drawOutline
import androidx.compose.ui.graphics.drawscope.ContentDrawScope
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.node.DrawModifierNode
import androidx.compose.ui.node.ModifierNodeElement
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp

/**
 * @param blurDp how much the shadow will be spread

 */

sealed class ShadowClipType {
    data object None: ShadowClipType()

    data class ClipToShape(
        val offsetX: Int,
        val offsetY: Int
    ): ShadowClipType()
}

data class AdvancedShadowSettings(
    val shape: Shape,
    val color: Color,
    val blurDp: Dp,
    val clipType: ShadowClipType
)

@Composable
internal fun Modifier.advancedShadow(
    shape: Shape,
    color: Color,
    blurDp: Dp,
    clipType: ShadowClipType,
    visible: Boolean
): Modifier {
    val density = LocalDensity.current
    val paint = remember(blurDp, color) {
        Paint().apply {
            this.color = color
            val blurPx = with(density) { blurDp.toPx() }
            if(blurPx > 0f) {
                asFrameworkPaint().maskFilter = BlurMaskFilter(blurPx, BlurMaskFilter.Blur.NORMAL)
            }
        }
    }

    return this.then(
        AdvancedShadowElement(
            paint = paint,
            blurDp = blurDp,
            shape = shape,
            clipType = clipType,
            visible = visible
        )
    )
}

private data class AdvancedShadowElement(
    val paint: Paint,
    val shape: Shape,
    val blurDp: Dp,
    val clipType: ShadowClipType,
    val visible: Boolean
): ModifierNodeElement<AdvancedShadowModifierNode>() {
    override fun create(): AdvancedShadowModifierNode {
        return AdvancedShadowModifierNode(
            paint = paint,
            shape = shape,
            blurDp = blurDp,
            clipType = clipType,
            visible = visible
        )
    }

    override fun update(node: AdvancedShadowModifierNode) {
        node.visible = visible
    }
}

private class AdvancedShadowModifierNode(
    private val paint: Paint,
    private val shape: Shape,
    private val blurDp: Dp,
    private val clipType: ShadowClipType,
    var visible: Boolean
): Modifier.Node(), DrawModifierNode {

    override fun ContentDrawScope.draw() {
        if(visible) {
            drawShadow()
        }
        drawContent()
    }

    private fun ContentDrawScope.drawShadow() {
        val shadowWidth = size.width
        val shadowHeight = size.height

        if(shadowWidth <= 0f || shadowHeight <= 0f) {
            return
        }

        val shadowSize = Size(shadowWidth, shadowHeight)
        val shadowOutline = shape.createOutline(shadowSize, layoutDirection, this)

        drawIntoCanvas {
            it.save()
            if(clipType is ShadowClipType.ClipToShape) {
                it.clipRect(getShadowRect(density, size, clipType.offsetX, clipType.offsetY))
            }
            it.drawOutline(shadowOutline, paint)
            it.restore()
        }
    }

    private fun getShadowRect(density: Float, size: Size, offsetX: Int, offsetY: Int): Rect {
        val left: Float
        val right: Float
        val top: Float
        val bottom: Float
        val blurPx = density.times(blurDp.value)

        offsetX.let {
            if(it < 0) {
                left = -(blurPx) * 2
                right = size.width
            } else if(it > 0) {
                left = 0f
                right = size.width.plus(blurPx * 2)
            } else {
                left = 0f
                right = size.width
            }
        }
        offsetY.let {
            if(it < 0) {
                top = -(blurPx) * 2
                bottom = size.height
            } else if(it > 0) {
                top = 0f
                bottom = size.height.plus(blurPx * 2)
            } else {
                top = 0f
                bottom = size.height
            }
        }

        return Rect(left, top, right, bottom)
    }
}