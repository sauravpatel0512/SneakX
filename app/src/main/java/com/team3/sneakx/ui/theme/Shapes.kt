package com.team3.sneakx.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.unit.dp

/**
 * SneakX shapes: pills, cards, hero blocks per design spec.
 */
val SneakShapes = Shapes(
    extraSmall = RoundedCornerShape(4.dp),
    small = RoundedCornerShape(8.dp),
    medium = RoundedCornerShape(12.dp),
    large = RoundedCornerShape(16.dp),
    extraLarge = RoundedCornerShape(20.dp),
)

/** Input fields / stadium chips */
val SneakFieldShape = RoundedCornerShape(16.dp)

/** Primary buttons (52dp height target) */
val SneakButtonShape = RoundedCornerShape(18.dp)

/** Hero / dark promo cards */
val SneakHeroShape = RoundedCornerShape(24.dp)

/** Bottom nav outer pill */
val SneakNavShape = RoundedCornerShape(28.dp)

/** Active tab inner pill inside bottom nav */
val SneakNavActiveShape = RoundedCornerShape(22.dp)
