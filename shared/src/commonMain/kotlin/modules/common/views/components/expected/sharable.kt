package modules.common.views.components.expected

import androidx.compose.runtime.Composable

expect class ShareData {
	fun shareContent(text: String)
}

@Composable
expect fun createShareSheet(): ShareData