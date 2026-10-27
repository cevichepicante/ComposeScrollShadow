package com.cevichepicante.composescrollshadow

import android.graphics.BlurMaskFilter
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.ClipOp
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.addOutline
import androidx.compose.ui.graphics.drawOutline
import androidx.compose.ui.graphics.drawscope.ContentDrawScope
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.node.DrawModifierNode
import androidx.compose.ui.node.ModifierNodeElement
import androidx.compose.ui.platform.InspectorInfo
import androidx.compose.ui.unit.Dp
import com.cevichepicante.composescrollshadow.data.ShadowSideDirection
import com.cevichepicante.composescrollshadow.data.ShadowSideType
import kotlinx.coroutines.launch

private const val SHADOW_FADE_DURATION_MILLIS = 200

internal fun Modifier.advancedShadow(
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
        node.shape = shape
        node.shadowColor = color
        node.blurDp = blurDp
        node.sideType = sideType
        node.visible = visible
    }

    override fun InspectorInfo.inspectableProperties() {
        name = "advancedShadow"
        properties["shape"] = shape
        properties["color"] = color
        properties["blurDp"] = blurDp
        properties["sideType"] = sideType
        properties["visible"] = visible
    }
}

private class AdvancedShadowModifierNode(
    var shape: Shape,
    var shadowColor: Color,
    var blurDp: Dp,
    var sideType: ShadowSideType,
    visible: Boolean
): Modifier.Node(), DrawModifierNode {

    private val blurPaint = Paint()
    private var appliedBlurPx = -1f
    private val contentPath = Path()
    private val shiftedPath = Path()
    private val shadowAlpha = Animatable(if(visible) 1f else 0f)

    var visible: Boolean = visible
        set(value) {
            if(field == value) {
                return
            }
            field = value
            if(isAttached) {
                coroutineScope.launch {
                    shadowAlpha.animateTo(
                        targetValue = if(value) 1f else 0f,
                        animationSpec = tween(SHADOW_FADE_DURATION_MILLIS)
                    )
                }
            }
        }

    override fun onAttach() {
        coroutineScope.launch {
            shadowAlpha.snapTo(if(visible) 1f else 0f)
        }
    }

    override fun ContentDrawScope.draw() {
        val sideType = sideType
        val shadowInner = sideType is ShadowSideType.SingleSide && sideType.drawInner
        val shadowShown = shadowAlpha.value > 0f
        if(shadowShown && shadowInner) {
            drawContent()
            drawShadow()
        } else if(shadowShown) {
            drawShadow()
            drawContent()
        } else {
            drawContent()
        }
    }

    private fun animatedShadowColor(): Color {
        return shadowColor.copy(alpha = shadowColor.alpha * shadowAlpha.value)
    }

    private fun ContentDrawScope.drawShadow() {
        when(val sideType = sideType) {
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
        val gradientColor = animatedShadowColor()

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
                    colors = listOf(gradientColor, Color.Transparent),
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
                    colors = listOf(gradientColor, Color.Transparent),
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
                    colors = listOf(gradientColor, Color.Transparent),
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
                    colors = listOf(gradientColor, Color.Transparent),
                    startY = size.height,
                    endY = endOffsetY
                )
            }
        }

        val outline = shape.createOutline(size, layoutDirection, this)
        contentPath.rewind()
        contentPath.addOutline(outline)

        if(sideStyle.drawInner) {
            clipPath(contentPath) {
                drawRect(
                    brush = gradientBrush,
                    size = Size(width, height),
                    topLeft = topLeft
                )
            }
        } else {
            val shiftOffset = when(sideDirection) {
                ShadowSideDirection.Left -> Offset(-(blurPx), 0f)
                ShadowSideDirection.Right -> Offset(blurPx, 0f)
                ShadowSideDirection.Top -> Offset(0f, -(blurPx))
                ShadowSideDirection.Bottom -> Offset(0f, blurPx)
            }
            shiftedPath.rewind()
            shiftedPath.addOutline(outline)
            shiftedPath.translate(shiftOffset)

            clipPath(contentPath, ClipOp.Difference) {
                clipPath(shiftedPath) {
                    drawRect(
                        brush = gradientBrush,
                        size = Size(width, height),
                        topLeft = topLeft
                    )
                }
            }
        }
    }

    private fun ContentDrawScope.drawBlurMask(sideInfo: ShadowSideType.AllSide) {
        val shadowWidth = size.width
        val shadowHeight = size.height

        if(shadowWidth <= 0f || shadowHeight <= 0f) {
            return
        }

        val shadowSize = Size(shadowWidth, shadowHeight)
        val shadowOutline = shape.createOutline(shadowSize, layoutDirection, this)
        val paintColor = animatedShadowColor()
        if(blurPaint.color != paintColor) {
            blurPaint.color = paintColor
        }

        val blurPx = blurDp.toPx()
        if(blurPx != appliedBlurPx) {
            blurPaint.asFrameworkPaint().maskFilter = if(blurPx > 0f) {
                BlurMaskFilter(blurPx, BlurMaskFilter.Blur.NORMAL)
            } else {
                null
            }
            appliedBlurPx = blurPx
        }

        drawIntoCanvas {
            it.save()
            it.translate(sideInfo.offsetX.toPx(), sideInfo.offsetY.toPx())
            it.drawOutline(shadowOutline, blurPaint)
            it.restore()
        }
    }
}