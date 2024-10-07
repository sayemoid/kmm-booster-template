package modules.common.views.components.expected

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.interop.LocalUIViewController
import platform.UIKit.UIActivityViewController
import platform.UIKit.UIApplication
import platform.UIKit.UIViewController


actual class ShareData(
	private val rootController: UIViewController
) {
	actual fun shareContent(text: String) {
		// Use iOS sharing functionality
		val activityItems = listOf(text)
		val activityController = UIActivityViewController(activityItems, null)
		val context = UIApplication.sharedApplication.keyWindow?.rootViewController
		context?.presentViewController(activityController, true, null)
	}
}

@Composable
actual fun createShareSheet(): ShareData {
	val uiViewController = LocalUIViewController.current
	return remember { ShareData(uiViewController) }
}