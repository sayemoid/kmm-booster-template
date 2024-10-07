package utils.expected

import androidx.compose.runtime.Composable

expect class RemoteUrl() {
	@Composable
	fun create()
	fun open(url: String)
}
