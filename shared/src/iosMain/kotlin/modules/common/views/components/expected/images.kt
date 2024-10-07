package modules.common.views.components.expected

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.interop.LocalUIViewController
import androidx.compose.ui.layout.ContentScale
import com.seiko.imageloader.rememberImagePainter
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.refTo
import platform.UIKit.UIImage
import platform.UIKit.UIImageJPEGRepresentation
import platform.UIKit.UIImagePickerController
import platform.UIKit.UIImagePickerControllerDelegateProtocol
import platform.UIKit.UIImagePickerControllerSourceType
import platform.UIKit.UINavigationControllerDelegateProtocol
import platform.UIKit.UIViewController
import platform.darwin.NSObject
import platform.posix.memcpy

@Composable
actual fun ImageLoader(
	url: String,
	contentDescription: String?,
	modifier: Modifier,
	placeholder: Painter?,
	scale: ContentScale,
	content: @Composable () -> Unit
) {
	val painter = rememberImagePainter(url)
	Box(
		modifier = modifier
	) {
		Image(
			modifier = Modifier.matchParentSize(),
			painter = painter,
			contentDescription = contentDescription,
			contentScale = scale,
		)

		content()
	}
}

actual class ImagePicker(
	private val rootController: UIViewController
) {
	private val imagePickerController = UIImagePickerController().apply {
		sourceType = UIImagePickerControllerSourceType.UIImagePickerControllerSourceTypePhotoLibrary
	}

	private var onImagePicked: (ByteArray) -> Unit = {}

	@OptIn(ExperimentalForeignApi::class)
	private val delegate = object : NSObject(), UIImagePickerControllerDelegateProtocol,
		UINavigationControllerDelegateProtocol {

		override fun imagePickerController(
			picker: UIImagePickerController,
			didFinishPickingImage: UIImage,
			editingInfo: Map<Any?, *>?
		) {
			val imageNsData = UIImageJPEGRepresentation(didFinishPickingImage, 1.0)
				?: return
			val bytes = ByteArray(imageNsData.length.toInt())
			memcpy(bytes.refTo(0), imageNsData.bytes, imageNsData.length)

			onImagePicked(bytes)

			picker.dismissViewControllerAnimated(true, null)
		}

		override fun imagePickerControllerDidCancel(picker: UIImagePickerController) {
			picker.dismissViewControllerAnimated(true, null)
		}
	}

	@Composable
	actual fun registerPicker(onImagePicked: (ByteArray) -> Unit) {
		this.onImagePicked = onImagePicked
	}

	actual fun pickImage() {
		rootController.presentViewController(imagePickerController, true) {
			imagePickerController.delegate = delegate
		}
	}
}

@Composable
actual fun createPicker(): ImagePicker {
	val uiViewController = LocalUIViewController.current
	return remember {
		ImagePicker(uiViewController)
	}
}