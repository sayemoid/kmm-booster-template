package utils.expected

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat

actual class RemoteUrl actual constructor() {
	var context: Context? = null

	@Composable
	actual fun create() {
		context = LocalContext.current
	}

	actual fun open(url: String) {
		val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
		context?.let {
			ContextCompat.startActivity(it, intent, null)
		}
	}
}

