package modules.exampleModule.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable

@Composable
actual fun ExampleAppTheme(
	useDarkTheme: Boolean,
	dynamicColorScheme: Boolean,
	content: @Composable () -> Unit
) {
	val colors = when {
		useDarkTheme -> darkColors
		else -> lightColors
	}

	MaterialTheme(
		colorScheme = colors,
		shapes = shapes,
		typography = typography,
		content = content,
	)
}
