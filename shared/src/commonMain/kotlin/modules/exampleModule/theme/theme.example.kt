package modules.exampleModule.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable

@Composable
expect fun ExampleAppTheme(
	useDarkTheme: Boolean = isSystemInDarkTheme(),
	dynamicColorScheme: Boolean = false,
	content: @Composable () -> Unit
)