package com.cevichepicante.composescrollshadow

import android.graphics.BlurMaskFilter
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.drawOutline
import androidx.compose.ui.graphics.drawscope.ContentDrawScope
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.node.DrawModifierNode
import androidx.compose.ui.node.ModifierNodeElement
import androidx.compose.ui.unit.Dp
import com.cevichepicante.composescrollshadow.data.ShadowSideDirection
import com.cevichepicante.composescrollshadow.data.ShadowSideType

@Composable
fun Modifier.advancedShadow(
    shape: Shape,
    color: Color,
    blurDp: Dp,
    sideType: ShadowSideType,
    visible: Boolean
): Modifier {
    return this.then(
        AdvancedShadowElement(
            shape = shape,
            color = color,
            blurDp = blurDp,
            sideType = sideType,
            visible = visible
        )
    )
}

private data class AdvancedShadowElement(
    val shape: Shape,
    val color: Color,
    val blurDp: Dp,
    val sideType: ShadowSideType,
    val visible: Boolean
): ModifierNodeElement<AdvancedShadowModifierNode>() {
    override fun create(): AdvancedShadowModifierNode {
        return AdvancedShadowModifierNode(
            shape = shape,
            shadowColor = color,
            blurDp = blurDp,
            sideType = sideType,
            visible = visible
        )
    }

    override fun update(node: AdvancedShadowModifierNode) {
        node.visible = visible
    }
}

private class AdvancedShadowModifierNode(
    private val shape: Shape,
    private val shadowColor: Color,
    private val blurDp: Dp,
    private val sideType: ShadowSideType,
    var visible: Boolean
): Modifier.Node(), DrawModifierNode {

    override fun ContentDrawScope.draw() {
        val shadowInner = sideType is ShadowSideType.SingleSide && sideType.drawInner
        if(visible && shadowInner) {
            drawContent()
            drawShadow()
        } else if(visible) {
            drawShadow()
            drawContent()
        } else {
            drawContent()
        }
    }

    private fun ContentDrawScope.drawShadow() {
        when(sideType) {
            is ShadowSideType.AllSide -> drawBlurMask(sideType)
            is ShadowSideType.SingleSide -> drawGradient(sideType)
        }
    }

    private fun ContentDrawScope.drawGradient(sideStyle: ShadowSideType.SingleSide) {
        if(size.width <= 0f || size.height <= 0f) {
            return
        }

        val sideDirection = sideStyle.direction
        val blurPx = blurDp.toPx()

        var topLeft = Offset.Zero
        val width: Float
        val height: Float

        if(sideStyle.drawInner) {
            width = size.width
            height = size.height
        } else {
            width = size.width.let {
                when(sideDirection) {
                    ShadowSideDirection.Left,
                    ShadowSideDirection.Right -> {
                        it.plus(blurPx)
                    }
                    else -> it
                }
            }
            height = size.height.let {
                when(sideDirection) {
                    ShadowSideDirection.Top,
                    ShadowSideDirection.Bottom -> {
                        it.plus(blurPx)
                    }
                    else -> it
                }
            }
        }

        val gradientBrush = when(sideDirection) {
            ShadowSideDirection.Left -> {
                val endOffsetX = if(sideStyle.drawInner) {
                    blurPx
                } else {
                    topLeft = Offset(-(blurPx), 0f)
                    -(blurPx)
                }
                Brush.linearGradient(
                    colors = listOf(shadowColor, Color.Transparent),
                    start = Offset(0f, 0f),
                    end = Offset(endOffsetX, 0f)
                )
            }
            ShadowSideDirection.Right -> {
                val endOffsetX = if(sideStyle.drawInner) {
                    size.width.minus(blurPx)
                } else {
                    size.width.plus(blurPx)
                }
                Brush.linearGradient(
                    colors = listOf(shadowColor, Color.Transparent),
                    start = Offset(size.width, 0f),
                    end = Offset(endOffsetX, 0f)
                )
            }
            ShadowSideDirection.Top -> {
                val endOffsetY = if(sideStyle.drawInner) {
                    blurPx
                } else {
                    topLeft = Offset(0f, -(blurPx))
                    -(blurPx)
                }
                Brush.verticalGradient(
                    colors = listOf(shadowColor, Color.Transparent),
                    startY = 0f,
                    endY = endOffsetY
                )
            }
            ShadowSideDirection.Bottom -> {
                val endOffsetY = if(sideStyle.drawInner) {
                    size.height.minus(blurPx)
                } else {
                    size.height.plus(blurPx)
                }
                Brush.verticalGradient(
                    colors = listOf(shadowColor, Color.Transparent),
                    startY = size.height,
                    endY = endOffsetY
                )
            }
        }

        drawRect(
            brush = gradientBrush,
            size = Size(width, height),
            topLeft = topLeft
        )
    }

    private fun ContentDrawScope.drawBlurMask(sideInfo: ShadowSideType.AllSide) {
        val shadowWidth = size.width
        val shadowHeight = size.height

        if(shadowWidth <= 0f || shadowHeight <= 0f) {
            return
        }

        val shadowSize = Size(shadowWidth, shadowHeight)
        val shadowOutline = shape.createOutline(shadowSize, layoutDirection, this)
        val paint = Paint().apply {
            this.color = shadowColor

            val blurPx = blurDp.toPx()
            if(blurPx > 0f) {
                asFrameworkPaint().maskFilter = BlurMaskFilter(blurPx, BlurMaskFilter.Blur.NORMAL)
            }
        }

        drawIntoCanvas {
            it.save()
            it.translate(sideInfo.offsetX, sideInfo.offsetY)
            it.drawOutline(shadowOutline, paint)
            it.restore()
        }
    }
}