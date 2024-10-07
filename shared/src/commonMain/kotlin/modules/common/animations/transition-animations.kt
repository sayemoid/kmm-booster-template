package modules.common.animations

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.fadeIn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity

@Composable
fun EnterAnimation(content: @Composable () -> Unit) {
	AnimatedVisibility(
		visibleState = MutableTransitionState(
			initialState = false
		).apply { targetState = true },
		modifier = Modifier,
		enter = Transition.FadeInOut(1000).enter,
		exit = Transition.FadeInOut(500).exit,
	) {
		content()
	}
}

@Composable
fun LoadingAnimation(
	visible: Boolean,
	modifier: Modifier = Modifier,
	content: @Composable() AnimatedVisibilityScope.() -> Unit
) =
	AnimatedVisibility(
		visible = visible,
		modifier = Modifier.then(modifier),
		enter = Transition.FadeInOut().enter,
		exit = Transition.FadeInOut().exit,
		content = content
	)