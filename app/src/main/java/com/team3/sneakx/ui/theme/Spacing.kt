package com.team3.sneakx.ui.theme

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * 4 dp grid for consistent padding and gaps. Prefer these over raw dp literals in new UI.
 */
object SneakSpacing {
    val xs: Dp = 4.dp
    val sm: Dp = 8.dp
    val md: Dp = 12.dp
    val lg: Dp = 16.dp
    val xl: Dp = 24.dp
    val xxl: Dp = 32.dp

    /** Standard horizontal padding for full-width screens and scrolling lists. */
    val screenPadding: Dp = lg

    /** Vertical or horizontal gap between list rows or related controls. */
    val listItemGap: Dp = sm

    /** Inner padding for cards and image tiles. */
    val cardPadding: Dp = md

    /** Space below fixed headers before scrollable content. */
    val sectionGap: Dp = md

    /** Extra bottom inset for content above nav bars / FABs. */
    val bottomContentInset: Dp = 88.dp
}
