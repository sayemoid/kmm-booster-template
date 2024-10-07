package modules.exampleModule.theme

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color

const val stronglyDeemphasizedAlpha = 0.6f
const val slightlyDeemphasizedAlpha = 0.87f
val md_pantone = Color(245, 50, 89) // #f53259
val md_theme_light_transparent = Color(0x00000000)

val deemphasizedGray =
	{ isDarkTheme: Boolean -> if (isDarkTheme) Color.DarkGray else Color.LightGray }
val colorSuccess = Color(0xFF228B22)
val colorWarning = Color(0xFFFFBF00)
/*
These colors can be replaced from theme generator
 */

val md_theme_light_primary = Color(0xFF3D6A00)
val md_theme_light_onPrimary = Color(0xFFFFFFFF)
val md_theme_light_primaryContainer = Color(0xFFB0F665)
val md_theme_light_onPrimaryContainer = Color(0xFF0F2000)
val md_theme_light_secondary = Color(0xFF566500)
val md_theme_light_onSecondary = Color(0xFFFFFFFF)
val md_theme_light_secondaryContainer = Color(0xFFD6EE64)
val md_theme_light_onSecondaryContainer = Color(0xFF181E00)
val md_theme_light_tertiary = Color(0xFF386663)
val md_theme_light_onTertiary = Color(0xFFFFFFFF)
val md_theme_light_tertiaryContainer = Color(0xFFBBECE8)
val md_theme_light_onTertiaryContainer = Color(0xFF00201F)
val md_theme_light_error = Color(0xFFBA1A1A)
val md_theme_light_errorContainer = Color(0xFFFFDAD6)
val md_theme_light_onError = Color(0xFFFFFFFF)
val md_theme_light_onErrorContainer = Color(0xFF410002)
val md_theme_light_background = Color(0xFFFDFCF5)
val md_theme_light_onBackground = Color(0xFF1B1C18)
val md_theme_light_surface = Color(0xFFFDFCF5)
val md_theme_light_onSurface = Color(0xFF1B1C18)
val md_theme_light_surfaceVariant = Color(0xFFE1E4D5)
val md_theme_light_onSurfaceVariant = Color(0xFF44483D)
val md_theme_light_outline = Color(0xFF75796C)
val md_theme_light_inverseOnSurface = Color(0xFFF2F1EA)
val md_theme_light_inverseSurface = Color(0xFF30312C)
val md_theme_light_inversePrimary = Color(0xFF95D94C)
val md_theme_light_shadow = Color(0xFF000000)
val md_theme_light_surfaceTint = Color(0xFF3D6A00)
val md_theme_light_outlineVariant = Color(0xFFC4C8BA)
val md_theme_light_scrim = Color(0xFF000000)

val md_theme_dark_primary = Color(0xFF95D94C)
val md_theme_dark_onPrimary = Color(0xFF1D3700)
val md_theme_dark_primaryContainer = Color(0xFF2C5000)
val md_theme_dark_onPrimaryContainer = Color(0xFFB0F665)
val md_theme_dark_secondary = Color(0xFFBAD14B)
val md_theme_dark_onSecondary = Color(0xFF2C3400)
val md_theme_dark_secondaryContainer = Color(0xFF404C00)
val md_theme_dark_onSecondaryContainer = Color(0xFFD6EE64)
val md_theme_dark_tertiary = Color(0xFFA0CFCC)
val md_theme_dark_onTertiary = Color(0xFF003735)
val md_theme_dark_tertiaryContainer = Color(0xFF1F4E4C)
val md_theme_dark_onTertiaryContainer = Color(0xFFBBECE8)
val md_theme_dark_error = Color(0xFFFFB4AB)
val md_theme_dark_errorContainer = Color(0xFF93000A)
val md_theme_dark_onError = Color(0xFF690005)
val md_theme_dark_onErrorContainer = Color(0xFFFFDAD6)
val md_theme_dark_background = Color(0xFF1B1C18)
val md_theme_dark_onBackground = Color(0xFFE3E3DB)
val md_theme_dark_surface = Color(0xFF1B1C18)
val md_theme_dark_onSurface = Color(0xFFE3E3DB)
val md_theme_dark_surfaceVariant = Color(0xFF44483D)
val md_theme_dark_onSurfaceVariant = Color(0xFFC4C8BA)
val md_theme_dark_outline = Color(0xFF8E9285)
val md_theme_dark_inverseOnSurface = Color(0xFF1B1C18)
val md_theme_dark_inverseSurface = Color(0xFFE3E3DB)
val md_theme_dark_inversePrimary = Color(0xFF3D6A00)
val md_theme_dark_shadow = Color(0xFF000000)
val md_theme_dark_surfaceTint = Color(0xFF95D94C)
val md_theme_dark_outlineVariant = Color(0xFF44483D)
val md_theme_dark_scrim = Color(0xFF000000)


val seed = Color(0xFF589600)

/*
	End generated theme colors
 */

val lightColors = lightColorScheme(
	primary = md_theme_light_primary,
	onPrimary = md_theme_light_onPrimary,
	primaryContainer = md_theme_light_primaryContainer,
	onPrimaryContainer = md_theme_light_onPrimaryContainer,
	secondary = md_theme_light_secondary,
	onSecondary = md_theme_light_onSecondary,
	secondaryContainer = md_theme_light_secondaryContainer,
	onSecondaryContainer = md_theme_light_onSecondaryContainer,
	tertiary = md_theme_light_tertiary,
	onTertiary = md_theme_light_onTertiary,
	tertiaryContainer = md_theme_light_tertiaryContainer,
	onTertiaryContainer = md_theme_light_onTertiaryContainer,
	error = md_theme_light_error,
	errorContainer = md_theme_light_errorContainer,
	onError = md_theme_light_onError,
	onErrorContainer = md_theme_light_onErrorContainer,
	background = md_theme_light_background,
	onBackground = md_theme_light_onBackground,
	surface = md_theme_light_surface,
	onSurface = md_theme_light_onSurface,
	surfaceVariant = md_theme_light_surfaceVariant,
	onSurfaceVariant = md_theme_light_onSurfaceVariant,
	outline = md_theme_light_outline,
	inverseOnSurface = md_theme_light_inverseOnSurface,
	inverseSurface = md_theme_light_inverseSurface,
	inversePrimary = md_theme_light_inversePrimary,
	surfaceTint = md_theme_light_surfaceTint,
	outlineVariant = md_theme_light_outlineVariant,
	scrim = md_theme_light_scrim,
)


val darkColors = darkColorScheme(
	primary = md_theme_dark_primary,
	onPrimary = md_theme_dark_onPrimary,
	primaryContainer = md_theme_dark_primaryContainer,
	onPrimaryContainer = md_theme_dark_onPrimaryContainer,
	secondary = md_theme_dark_secondary,
	onSecondary = md_theme_dark_onSecondary,
	secondaryContainer = md_theme_dark_secondaryContainer,
	onSecondaryContainer = md_theme_dark_onSecondaryContainer,
	tertiary = md_theme_dark_tertiary,
	onTertiary = md_theme_dark_onTertiary,
	tertiaryContainer = md_theme_dark_tertiaryContainer,
	onTertiaryContainer = md_theme_dark_onTertiaryContainer,
	error = md_theme_dark_error,
	errorContainer = md_theme_dark_errorContainer,
	onError = md_theme_dark_onError,
	onErrorContainer = md_theme_dark_onErrorContainer,
	background = md_theme_dark_background,
	onBackground = md_theme_dark_onBackground,
	surface = md_theme_dark_surface,
	onSurface = md_theme_dark_onSurface,
	surfaceVariant = md_theme_dark_surfaceVariant,
	onSurfaceVariant = md_theme_dark_onSurfaceVariant,
	outline = md_theme_dark_outline,
	inverseOnSurface = md_theme_dark_inverseOnSurface,
	inverseSurface = md_theme_dark_inverseSurface,
	inversePrimary = md_theme_dark_inversePrimary,
	surfaceTint = md_theme_dark_surfaceTint,
	outlineVariant = md_theme_dark_outlineVariant,
	scrim = md_theme_dark_scrim,
)