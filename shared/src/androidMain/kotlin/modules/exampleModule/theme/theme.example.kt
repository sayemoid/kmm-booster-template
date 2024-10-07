package modules.exampleModule.theme

import android.app.Activity
import android.os.Build
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

@Composable
actual fun ExampleAppTheme(
	useDarkTheme: Boolean,
	dynamicColorScheme: Boolean,
	content: @Composable () -> Unit
) {
	val context = LocalContext.current
	val colors = when {
		dynamicColorScheme && (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) -> {
			if (useDarkTheme) dynamicDarkColorScheme(context)
			else dynamicLightColorScheme(context)
		}

		useDarkTheme -> darkColors
		else -> lightColors
	}

	// Add primary status bar color from chosen color scheme.
	val view = LocalView.current
	if (!view.isInEditMode) {
		SideEffect {
			val window = (view.context as Activity).window
			window.statusBarColor = colors.primary.toArgb()
			WindowCompat
				.getInsetsController(window, view)
				.isAppearanceLightStatusBars = useDarkTheme
		}
	}

	MaterialTheme(
		colorScheme = colors,
		shapes = shapes,
		typography = typography,
		content = content,
	)
}
