package modules.exampleModule.screens.layouts

import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.currentOrThrow
import cognito_kmm_template.shared.generated.resources.Res
import cognito_kmm_template.shared.generated.resources.compose_multiplatform
import configs.AppThemes
import kotlinx.coroutines.CoroutineScope
import modules.common.di.getKoinInstance
import modules.common.features.preferences.PrefKeys
import modules.common.features.preferences.PrefVM
import modules.common.views.layouts.BasicLayout
import modules.exampleModule.screens.Screens
import modules.exampleModule.screens.SettingsScreen
import modules.exampleModule.theme.ExampleAppTheme
import org.jetbrains.compose.resources.painterResource

@Composable
fun ExampleAppLayout(
	fabVisibility: Boolean = false,
	fabText: String? = null,
	contentView: @Composable (
		paddingValues: PaddingValues, snackbar: Pair<SnackbarHostState, CoroutineScope>
	) -> Unit
) {
	val navigator = LocalNavigator.currentOrThrow
	val navItems = listOf(
		Screens.Main,
		Screens.Todo
	)

	val prefVM = getKoinInstance<PrefVM>()
	val (themePref, dynamicColorScheme) = prefVM.getPrefsBlocking(
		PrefKeys.theme,
		PrefKeys.dynamicColorScheme
	)
	val theme = themePref.fold({ AppThemes.DEFAULT }, { AppThemes.valueOf(it) })
	ExampleAppTheme(
		dynamicColorScheme = dynamicColorScheme.fold({ false }, { it }),
		useDarkTheme = when (theme) {
			AppThemes.DEFAULT -> isSystemInDarkTheme()
			AppThemes.LIGHT -> false
			AppThemes.DARK -> true
		}
	) {
		BasicLayout(
			logo = {
				Icon(
					modifier = Modifier.clickable {
						navigator.popAll()
						navigator.replaceAll(Screens.Main.screen)
					},
					painter = painterResource(Res.drawable.compose_multiplatform),
					contentDescription = null,
					tint = MaterialTheme.colorScheme.primary
				)
			},
			title = {
				Text(
					modifier = Modifier,
					text = "Example",
					style = MaterialTheme.typography.titleMedium,
					color = MaterialTheme.colorScheme.primary
				)
			},
			fabVisibility = fabVisibility,
			fabText = fabText ?: "",
			fabClick = {
			},
			actions = actions(),
			bottomNavigation = {
				AppBottomNavigation(
					items = navItems,
					navigator = navigator
				)
			},
			contentView = contentView
		)
	}
}

@Composable
private fun actions(): @Composable (RowScope.() -> Unit) {
	val navigator = LocalNavigator.currentOrThrow
	return {
		Icon(
			imageVector = Icons.Outlined.Search,
			tint = MaterialTheme.colorScheme.primary,
			modifier = Modifier
				.clickable {
					// Open search page
				}
				.padding(horizontal = 12.dp, vertical = 16.dp)
				.height(24.dp),
			contentDescription = "Search"
		)
		Icon(
			imageVector = Screens.Settings.iconRes,
			tint = MaterialTheme.colorScheme.primary,
			modifier = Modifier
				.clickable {
					navigator.push(SettingsScreen)
				}
				.padding(horizontal = 12.dp, vertical = 16.dp)
				.height(24.dp),
			contentDescription = "Settings"
		)
	}
}

@Composable
private fun AppBottomNavigation(
	items: List<Screens>,
	navigator: Navigator
) {
	NavigationBar(
		containerColor = MaterialTheme.colorScheme.background,
		contentColor = MaterialTheme.colorScheme.primary,
		tonalElevation = 4.dp
	) {
		val currentScreen = navigator.lastItem
		items.forEach { screen ->
			NavigationBarItem(
				colors = NavigationBarItemDefaults.colors(),
				icon = {
					Icon(
						screen.iconRes,
						screen.name,
						tint = MaterialTheme.colorScheme.primary
					)
				},
				label = { Text(screen.name) },
				selected = currentScreen == screen.screen,
				alwaysShowLabel = false, // This hides the title for the unselected items
				onClick = {
					// This if check gives us a "singleTop" behavior where we do not create a
					// second instance of the composable if we are already on that destination
					if (currentScreen != screen.screen) {
						navigator.replace(screen.screen)
					}
				}
			)
		}
	}
}
