package modules.exampleModule

import androidx.compose.ui.window.ComposeUIViewController
import cafe.adriel.voyager.navigator.Navigator
import modules.exampleModule.screens.MainScreen

fun MainViewController() = ComposeUIViewController {
	Navigator(MainScreen)
}