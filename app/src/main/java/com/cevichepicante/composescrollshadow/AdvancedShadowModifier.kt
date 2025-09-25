package com.cevichepicante.composescrollshadow

import android.graphics.BlurMaskFilter
import android.util.Log
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp

@Composable
internal fun Modifier.advancedShadow(
    shape: Shape,
    color: Color,
    blurDp: Dp,
    offsetX: Int,
    offsetY: Int,
    clipToShape: Boolean,
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
            offsetX = offsetX,
            offsetY = offsetY,
            blurDp = blurDp,
            shape = shape,
            clipToShape = clipToShape,
            visible = visible
        )
    )
}

private data class AdvancedShadowElement(
    val paint: Paint,
    val shape: Shape,
    val blurDp: Dp,
    val offsetX: Int,
    val offsetY: Int,
    val clipToShape: Boolean,
    val visible: Boolean
): ModifierNodeElement<AdvancedShadowModifierNode>() {
    override fun create(): AdvancedShadowModifierNode {
        return AdvancedShadowModifierNode(
            paint = paint,
            shape = shape,
            blurDp = blurDp,
            offsetX = offsetX,
            offsetY = offsetY,
            clipToShape = clipToShape,
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
    private val offsetX: Int,
    private val offsetY: Int,
    private val clipToShape: Boolean,
    var visible: Boolean
): Modifier.Node(), DrawModifierNode {

    override fun ContentDrawScope.draw() {
        if(visible) {
            drawShadow()
        }
        drawContent()
    }

    private fun ContentDrawScope.drawShadow() {
        val offsetXPx = offsetX.times(density)
        val offsetYPx = offsetY.times(density)
        val shadowWidth = size.width
        val shadowHeight = size.height

        if(shadowWidth <= 0f || shadowHeight <= 0f) {
            return
        }

        val shadowSize = Size(shadowWidth, shadowHeight)
        val shadowOutline = shape.createOutline(shadowSize, layoutDirection, this)

        drawIntoCanvas {
            it.save()
            it.translate(offsetXPx, offsetYPx)
            if(clipToShape) {
                it.clipRect(getShadowRect(density, size))
            }
            it.drawOutline(shadowOutline, paint)
            it.restore()
        }
    }

    private fun getShadowRect(density: Float, size: Size): Rect {
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