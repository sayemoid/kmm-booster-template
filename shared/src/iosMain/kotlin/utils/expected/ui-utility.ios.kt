package utils.expected

import androidx.compose.runtime.Composable
import platform.Foundation.NSURL
import platform.UIKit.UIApplication

actual class RemoteUrl actual constructor() {

	@Composable
	actual fun create() {
	}

	actual fun open(url: String) {
		NSURL.URLWithString(url)?.let {
			UIApplication.sharedApplication.openURL(it)
		}
	}
}