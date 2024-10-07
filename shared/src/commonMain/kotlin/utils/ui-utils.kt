package utils

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.graphics.Color
import arrow.core.none
import configs.AppThemes
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import modules.common.features.preferences.PrefKeys
import modules.common.features.preferences.PrefVM
import kotlin.math.abs
import kotlin.math.roundToInt
import kotlin.random.Random
import kotlin.random.nextInt

fun Pair<SnackbarHostState, CoroutineScope>.show(
	message: String,
	actionLabel: String? = null,
	withDismissAction: Boolean = false,
	duration: SnackbarDuration = if (withDismissAction) {
		SnackbarDuration.Indefinite
	} else {
		SnackbarDuration.Short
	},
	dismissAction: () -> Unit = {},
	onAction: () -> Unit = {}
) {
	val (snackbar, scope) = this
	scope.launch {
		val result = snackbar.showSnackbar(
			message = message,
			actionLabel = actionLabel,
			withDismissAction = withDismissAction,
			duration = duration
		)

		when (result) {
			SnackbarResult.ActionPerformed -> onAction()
			SnackbarResult.Dismissed -> {
				dismissAction()
			}
		}
	}
}

fun avatarUrl(str: String, size: Int = 128) = "https://ui-avatars.com/api/?name=$str&size=$size"

fun parseColor(color: String) = try {
	Color(color.removePrefix("#").toLong(16) or 0x00000000FF000000)
} catch (e: NumberFormatException) {
	Color.Gray
}

@Composable
fun PrefVM.isDarkTheme(fetchBlocking: Boolean = false) =
	if (!fetchBlocking) {
		this.getPref(PrefKeys.theme).collectAsState(none()).value
	} else {
		this.getPrefBlocking(PrefKeys.theme)
	}.fold({ isSystemInDarkTheme() },
		{ AppThemes.DARK.name == it })

val randomColors = arrayOf(
	"#F44336", "#E91E63", "#9C27B0", "#673AB7", "#3F51B5",
	"#2196F3", "#03A9F4", "#00BCD4", "#009688", "#4CAF50",
	"#8BC34A", "#CDDC39", "#FFEB3B", "#FFC107", "#FF9800",
	"#FF5722", "#795548", "#9E9E9E", "#607D8B", "#B71C1C",
	"#880E4F", "#4A148C", "#311B92", "#1A237E", "#0D47A1",
	"#01579B", "#006064", "#004D40", "#1B5E20", "#33691E",
	"#827717", "#F57F17", "#FF6F00", "#E65100", "#BF360C",
	"#3E2723", "#212121", "#263238", "#D32F2F", "#C2185B",
	"#7B1FA2", "#512DA8", "#303F9F", "#1976D2", "#0288D1",
	"#0097A7", "#00796B", "#388E3C", "#689F38", "#AFB42B",
	"#FBC02D", "#FFA000", "#F57C00", "#E64A19", "#5D4037"
)

fun randomColorFromList(num: Int? = null) = num?.let {
	randomColors[num % randomColors.size]
} ?: randomColors[Random.nextInt(randomColors.indices)]

fun randomVividColor(number: Float? = null): Color {
	// Generate a random hue from 0 to 360
	val num = number ?: Random.nextFloat()
	val hue = num * 360

	// Set high saturation. For vivid colors, saturation is often near 1.0
	val saturation = 0.8f + num * 0.2f // 80% to 100%

	// Set lightness. Avoid going too high to maintain vividness
	val lightness = 0.5f + num * 0.3f // 50% to 80%

	// Convert HSL to RGB
	return hslToRgb(hue, saturation, lightness)
}

fun numberToMediumRangeColor(number: Int): Color {
	// Normalize the number to a hue value (0-360)
	val hue = (number % 360).toFloat()

	// Set medium saturation and lightness values
	val saturation = 0.5f // 50% saturation for medium vividness
	val lightness = 0.5f // 50% lightness for medium brightness/darkness

	// Convert HSL to RGB
	return hslToRgb(hue, saturation, lightness)
}

fun hslToRgb(hue: Float, saturation: Float, lightness: Float): Color {
	val c = (1 - abs(2 * lightness - 1)) * saturation
	val x = c * (1 - abs((hue / 60.0) % 2 - 1)).toFloat()
	val m = lightness - c / 2

	val (r, g, b) = when {
		hue < 60 -> Triple(c, x, 0f)
		hue < 120 -> Triple(x, c, 0f)
		hue < 180 -> Triple(0f, c, x)
		hue < 240 -> Triple(0f, x, c)
		hue < 300 -> Triple(x, 0f, c)
		else -> Triple(c, 0f, x)
	}

	return Color(
		red = ((r + m) * 255).roundToInt(),
		green = ((g + m) * 255).roundToInt(),
		blue = ((b + m) * 255).roundToInt()
	)
}

fun randomColor(): Color {
	return Color(
		red = Random.nextFloat(),
		green = Random.nextFloat(),
		blue = Random.nextFloat(),
		alpha = 1f // Full opacity
	)
}

fun getColorFromNumber(number: Int): Color {
	// Use the number to generate a hex color code.
	// We use `0xFFFFFF` (16777215 in decimal) to ensure the number fits within the RGB color range.
	val hexColor = 0xFFFFFF and number

	// Convert the hex color to a Color value.
	return Color(
		red = (hexColor shr 16) and 0xFF,
		green = (hexColor shr 8) and 0xFF,
		blue = hexColor and 0xFF,
		alpha = 0xFF // Full opacity
	)
}