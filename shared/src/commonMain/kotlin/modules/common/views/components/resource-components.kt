package modules.common.views.components

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.DefaultAlpha
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale

@Composable
fun WImage(
	modifier: Modifier = Modifier,
	painter: Painter,
	contentDescription: String?,
	alignment: Alignment = Alignment.Center,
	contentScale: ContentScale = ContentScale.Fit,
	alpha: Float = DefaultAlpha,
	colorFilter: ColorFilter? = null
) {
	Image(
		painter = painter,
		contentDescription = contentDescription,
		modifier = modifier,
		alignment = alignment,
		contentScale = contentScale,
		alpha = alpha,
		colorFilter = colorFilter
	)
}