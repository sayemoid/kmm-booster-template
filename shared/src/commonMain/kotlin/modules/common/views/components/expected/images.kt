package modules.common.views.components.expected

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale

@Composable
expect fun ImageLoader(
	url: String,
	contentDescription: String? = null,
	modifier: Modifier = Modifier,
	placeholder: Painter? = null,
	scale: ContentScale = ContentScale.Crop,
	content: @Composable () -> Unit = {}
)

expect class ImagePicker {
	@Composable
	fun registerPicker(onImagePicked: (ByteArray) -> Unit)

	fun pickImage()
}

@Composable
expect fun createPicker(): ImagePicker