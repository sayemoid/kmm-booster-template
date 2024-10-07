package modules.exampleModule.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Login
import androidx.compose.material.icons.automirrored.outlined.Logout
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.ColorLens
import androidx.compose.material.icons.outlined.Colorize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.datastore.preferences.core.stringPreferencesKey
import arrow.core.none
import cafe.adriel.voyager.koin.koinScreenModel
import configs.AppThemes
import kotlinx.coroutines.delay
import modules.common.features.auth.AuthVM
import modules.common.features.auth.authentication
import modules.common.features.preferences.PrefItem
import modules.common.features.preferences.PrefItemView
import modules.common.features.preferences.PrefKeys
import modules.common.features.preferences.PrefSection
import modules.common.features.preferences.PrefType
import modules.common.features.preferences.PrefVM
import modules.common.views.components.LoadingView
import modules.common.views.components.WAlertDialog
import modules.common.views.screens.AppScreen
import modules.exampleModule.screens.layouts.ExampleAppLayout
import modules.exampleModule.theme.colorSuccess
import modules.exampleModule.theme.colorWarning
import utils.expected.Platforms
import utils.expected.platform
import utils.isDarkTheme
import utils.show

object SettingsScreen : AppScreen {

	@Composable
	override fun Content() {
		val authVM = koinScreenModel<AuthVM>()
		val prefVM = koinScreenModel<PrefVM>()

		var visible by remember { mutableStateOf(true) }
		LaunchedEffect(visible) {
			delay(500)
			visible = true
		}

		if (!visible) {
			LoadingView(
				modifier = Modifier.fillMaxWidth()
			)
		} else {
			ExampleAppLayout { paddingValues, snackbar ->
				val authentication = authentication(authVM)
				val defaultPrefContentColor = MaterialTheme.colorScheme.primary

				val prefDefinitions = object {
					val profile = authentication.auth.fold(
						{
							PrefItem(
								title = "Profile",
								icon = Icons.Outlined.AccountCircle,
								key = stringPreferencesKey("pref:user-name"),
								value = "user-name",
								color = defaultPrefContentColor
							)
						},
						{
							PrefItem(
								title = it.name,
								icon = Icons.Outlined.AccountCircle,
								key = stringPreferencesKey("pref:user-name"),
								value = "user-name",
								color = defaultPrefContentColor
							)
						}
					)

					val theme = PrefItem(
						title = "Theme",
						icon = Icons.Outlined.ColorLens,
						key = PrefKeys.theme,
						value = prefVM.getPref(PrefKeys.theme).collectAsState(none()).value
							.fold({ AppThemes.DEFAULT.name }, { it }),
						color = MaterialTheme.colorScheme.onSurface
					)

					val dynamicColorScheme = PrefItem(
						title = "Dynamic Theme Colors",
						icon = Icons.Outlined.Colorize,
						key = PrefKeys.dynamicColorScheme,
						value = prefVM.getPref(PrefKeys.dynamicColorScheme)
							.collectAsState(none()).value
							.fold({ false }, { it }),
						color = MaterialTheme.colorScheme.onSurface
					)


					val login = PrefItem(
						title = "Login",
						icon = Icons.AutoMirrored.Outlined.Login,
						key = stringPreferencesKey("pref:login"),
						value = "login",
						color = colorSuccess
					)

					val logout = PrefItem(
						title = "Logout",
						icon = Icons.AutoMirrored.Outlined.Logout,
						key = stringPreferencesKey("pref:logout"),
						value = "logout",
						color = colorWarning
					)
				}

				Column(
					modifier = Modifier.padding(paddingValues),
					verticalArrangement = Arrangement.Top
				) {

					PrefSection(
						title = "PROFILE",
						isDarkTheme = prefVM.isDarkTheme()
					) {
						PrefItemView(
							pref = prefDefinitions.profile
						) {
							snackbar.show("Profile Clicked!")
						}
					}
					PrefSection(
						title = "SETTINGS",
						isDarkTheme = prefVM.isDarkTheme()
					) {

						PrefItemView(
							pref = prefDefinitions.theme,
							prefType = PrefType.DROPDOWN,
							values = setOf(
								AppThemes.DEFAULT.name,
								AppThemes.DARK.name,
								AppThemes.LIGHT.name
							),
							onPrefSelected = {
								prefVM.updatePref(it)
								visible = false
							}
						)

						if (platform() == Platforms.ANDROID) {
							PrefItemView(
								pref = prefDefinitions.dynamicColorScheme,
								prefType = PrefType.DROPDOWN,
								values = setOf(
									true, false
								),
								onPrefSelected = {
									prefVM.updatePref(it)
									visible = false
								}
							)
						}
					}

					PrefSection(
						title = "ACCOUNT",
						isDarkTheme = prefVM.isDarkTheme()
					) {
						if (authentication.authenticated) {
							var showConfirmDialog by remember { mutableStateOf(false) }

							if (showConfirmDialog) {
								WAlertDialog(
									title = "Are you sure?",
									body = "Are you sure you want to logout?",
									onDismissed = { showConfirmDialog = false },
									onConfirm = {
										authVM.logout()
										showConfirmDialog = false
									}
								)
							}

							PrefItemView(
								pref = prefDefinitions.logout
							) {
								showConfirmDialog = true
							}


						} else {
							PrefItemView(
								pref = prefDefinitions.login
							) {
								authentication.require {}
							}
						}
					}
				}
			}

		}
	}
}