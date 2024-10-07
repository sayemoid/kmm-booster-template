package modules.exampleModule.screens

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.material.icons.outlined.Task
import androidx.compose.ui.graphics.vector.ImageVector
import modules.common.views.screens.AppScreen

sealed class Screens(val screen: AppScreen, val name: String, val iconRes: ImageVector) {

	data object Main : Screens(MainScreen, "HOME", Icons.Outlined.Home)
	data object Todo : Screens(MainScreen, "TODO", Icons.Outlined.Task)
	data object Settings : Screens(SettingsScreen, "SETTINGS", Icons.Outlined.Menu)
}
