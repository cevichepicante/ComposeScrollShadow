package com.cevichepicante.composescrollshadow

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.cevichepicante.composescrollshadow.data.ShadowSettings

/**
 * This scaffold adds [content] shadowed layer.
 *
 * @param shadowSettings for style of shadow layer
 * @param content Composable which will be drew with shadow
 *
 */

@Composable
fun ShadowIndicatedScaffold(
    shadowSettings: ShadowSettings,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Box(
        modifier = modifier
            .advancedShadow(
                shape = shadowSettings.shape,
                color = shadowSettings.color,
                blurDp = shadowSettings.blurDp,
                sideType = shadowSettings.sideType,
                visible = true
            )
    ) {
        content()
    }
}