package com.macoou.feed.ui

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlin.math.ln

/**
 * Get the any color at an elevation. This is the same algorithm that google uses so we can get
 * get the same colors for our custom elements.
 *
 * @param color The color to be elevated.
 * @param elevation The height of the elevation in DP.
 */
@Composable
fun colorAtElevation(color: Color, elevation: Dp): Color {
    if (elevation == 0.dp) return color
    val alpha = ((4.5f * ln(elevation.value + 1)) + 2f) / 100f
    return MaterialTheme.colorScheme.primary.copy(alpha = alpha).compositeOver(color)
}