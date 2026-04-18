package com.team3.sneakx.ui.theme

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Spec scale: 4 · 8 · 12 · 16 · 20 · 24 · 32 · 40
 */
object SneakSpacing {
    val xs: Dp = 4.dp
    val sm: Dp = 8.dp
    val md: Dp = 12.dp
    val lg: Dp = 16.dp
    val xl: Dp = 20.dp
    val xxl: Dp = 24.dp
    val xxxl: Dp = 32.dp
    val huge: Dp = 40.dp

    /** Horizontal padding for full-width screens */
    val screenPadding: Dp = 20.dp

    /** Top padding below status bar (content start) */
    val screenTop: Dp = 28.dp

    /** Gap between major sections */
    val sectionGap: Dp = 24.dp

    /** Inner padding for cards */
    val cardPadding: Dp = 16.dp

    /** Vertical gap between list rows */
    val listItemGap: Dp = 12.dp

    /** 2-column grid gutter */
    val gridGutterH: Dp = 12.dp
    val gridGutterV: Dp = 24.dp

    val controlGap: Dp = 16.dp

    val iconTextGap: Dp = 8.dp

    /** Content above floating bottom nav */
    val bottomContentInset: Dp = 96.dp

    /** Legacy alias used in some screens */
    val mdLegacy: Dp = md
}
