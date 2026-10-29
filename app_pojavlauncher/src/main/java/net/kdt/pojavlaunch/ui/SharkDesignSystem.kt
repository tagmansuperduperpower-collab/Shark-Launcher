package net.kdt.pojavlaunch.ui

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/** Shared sizing contract. Screens must use this instead of hard-coded pixel-like values. */
data class SharkUiMetrics(
    val screenPadding: Dp,
    val sectionGap: Dp,
    val cardPadding: Dp,
    val iconBox: Dp,
    val actionHeight: Dp,
    val playWidth: Dp,
    val playHeight: Dp
)

val CompactSharkMetrics = SharkUiMetrics(14.dp, 12.dp, 14.dp, 36.dp, 76.dp, 132.dp, 96.dp)
val ExpandedSharkMetrics = SharkUiMetrics(24.dp, 16.dp, 18.dp, 42.dp, 86.dp, 150.dp, 108.dp)

@Composable
fun rememberSharkMetrics(expanded: Boolean): SharkUiMetrics = remember(expanded) {
    if (expanded) ExpandedSharkMetrics else CompactSharkMetrics
}

/** One scroll owner for settings/file-like screens, preventing nested-scroll conflicts. */
@Composable
fun SharkLazyColumn(
    modifier: Modifier = Modifier,
    state: LazyListState = rememberLazyListState(),
    contentPadding: PaddingValues = PaddingValues(0.dp),
    content: LazyListScope.() -> Unit
) {
    LazyColumn(
        modifier = modifier,
        state = state,
        contentPadding = contentPadding,
        verticalArrangement = Arrangement.spacedBy(12.dp),
        content = content
    )
}

fun sharkEnter(): EnterTransition = fadeIn(tween(280, easing = SharkMotion.standard)) + slideInVertically(tween(320, easing = SharkMotion.standard)) { -24 }
fun sharkExit(): ExitTransition = fadeOut(tween(220, easing = SharkMotion.standard)) + slideOutVertically(tween(260, easing = SharkMotion.standard)) { 24 }
