package modules.common.views.components.expected

import android.content.Intent
import androidx.activity.ComponentActivity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext


actual class ShareData(
	private val activity: ComponentActivity
) {
	actual fun shareContent(text: String) {
		val sendIntent: Intent = Intent().apply {
			action = Intent.ACTION_SEND
			putExtra(Intent.EXTRA_TEXT, text)
			type = "text/plain"
		}
		activity.startActivity(Intent.createChooser(sendIntent, null))
	}
}

@Composable
actual fun createShareSheet(): ShareData {
	val activity = LocalContext.current as ComponentActivity
	return remember(activity) { ShareData(activity) }
}