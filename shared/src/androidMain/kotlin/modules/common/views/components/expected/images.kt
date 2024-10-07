package modules.common.views.components.expected

import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.decode.SvgDecoder
import coil.request.ImageRequest

@Composable
actual fun ImageLoader(
	url: String,
	contentDescription: String?,
	modifier: Modifier,
	placeholder: Painter?,
	scale: ContentScale,
	content: @Composable () -> Unit
) {
	Box(modifier = modifier) {
		var imageLoading by remember { mutableStateOf(true) }

		val isSvg = url.endsWith(".svg")
		val imageBuilder = ImageRequest.Builder(LocalContext.current)
			.data(url)
			.crossfade(true)
		if (isSvg) {
			imageBuilder.decoderFactory(SvgDecoder.Factory())
		}
		AsyncImage(
			modifier = Modifier
				.fillMaxSize()
				.align(Alignment.Center),
			model = imageBuilder
				.build(),
			contentDescription = contentDescription,
			onLoading = { imageLoading = true },
			onSuccess = { imageLoading = false },
			onError = { imageLoading = false },
			contentScale = scale,
		)
		if (imageLoading) {
			CircularProgressIndicator(
				modifier = Modifier
					.align(Alignment.Center)
					.padding(10.dp)
			)
		}

		content()
	}
}

actual class ImagePicker(
	private val activity: ComponentActivity
) {
	private lateinit var getContent: ActivityResultLauncher<String>

	@Composable
	actual fun registerPicker(onImagePicked: (ByteArray) -> Unit) {
		getContent = rememberLauncherForActivityResult(
			ActivityResultContracts.GetContent()
		) { uri ->
			uri?.let {
				activity.contentResolver.openInputStream(uri)?.use {
					onImagePicked(it.readBytes())
				}
			}
		}
	}

	actual fun pickImage() {
		getContent.launch("image/*")
	}
}

@Composable
actual fun createPicker(): ImagePicker {
	val activity = LocalContext.current as ComponentActivity
	return remember(activity) {
		ImagePicker(activity)
	}
}
