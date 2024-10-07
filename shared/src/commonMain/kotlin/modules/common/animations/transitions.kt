package modules.common.animations

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandIn
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.shrinkOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp

object Transition {
	class SlideInSlideOut(density: Density) {
		val enter = slideInVertically {
			// Slide in from 40 dp from the top.
			with(density) { -40.dp.roundToPx() }
		} + expandVertically(
			// Expand from the top.
			expandFrom = Alignment.Top
		) + fadeIn(
			// Fade in with the initial alpha of 0.3f.
			initialAlpha = 0.3f
		)
		val exit = slideOutVertically() + shrinkVertically() + fadeOut(targetAlpha = 1.0f)
	}

	object ScaleInOut {
		val enter = scaleIn() + expandVertically(expandFrom = Alignment.CenterVertically)

		val exit = scaleOut() + shrinkVertically(shrinkTowards = Alignment.CenterVertically)
	}

	object SimpleSlideInOut {
		val enter = slideInHorizontally(animationSpec = tween(500))
		val exit = slideOutHorizontally(animationSpec = tween(500))
	}

	class FadeInOut(duration: Int = 500) {
		val enter = fadeIn(animationSpec = tween(duration))
		val exit = fadeOut(animationSpec = tween(duration))
	}

	object ExpandShrink {
		fun enter(size: Int) = expandIn(
			expandFrom = Alignment.Center,
			animationSpec = tween(100),
			initialSize = { IntSize(size,size) }
		)
		fun exit(size: Int) = shrinkOut(
			shrinkTowards = Alignment.Center,
			animationSpec = tween(1000),
			targetSize = { IntSize(size,size) }
		)

	}

	object HeartTransition {
		fun enter(toggle: Boolean, size: Int) = if (toggle) expandIn(
			expandFrom = Alignment.Center,
			animationSpec = tween(1000),
			initialSize = { IntSize(size,size) }
		) else EnterTransition.None
		fun exit(toggle: Boolean, size: Int) = if (toggle) shrinkOut(
			shrinkTowards = Alignment.Center,
			animationSpec = tween(1000),
			targetSize = { IntSize(size,size) }
		) else ExitTransition.None

	}
}