package modules.common.features.preferences

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey

data class PrefItem<T>(
	val title: String,
	val icon: ImageVector,
	val key: Preferences.Key<T>,
	val value: T,
	val color: Color
)

object PrefKeys {
	val theme = stringPreferencesKey("pref:app-theme")
	val dynamicColorScheme = booleanPreferencesKey("pref:dynamic-color-scheme")
	val firebaseTokenKey = stringPreferencesKey("firebase-token-key")
	val appIdentifierKey = stringPreferencesKey("app-identifier")
}

