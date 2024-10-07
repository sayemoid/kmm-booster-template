package modules.common.views.layouts

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineScope
import modules.common.animations.EnterAnimation
import modules.common.views.components.AppBarTop
import modules.common.views.components.AutoResizeText
import modules.common.views.components.FontSizeRange

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BasicLayout(
	logo: @Composable () -> Unit,
	title: @Composable () -> Unit,
	actions: @Composable RowScope.() -> Unit = {},
	fabVisibility: Boolean = false,
	fabText: String = "",
	fabClick: () -> Unit = {},
	fabIcon: ImageVector= Icons.Default.Add,
	bottomNavigation: @Composable () -> Unit = {},
	contentView: @Composable (paddingValues: PaddingValues, snackbar: Pair<SnackbarHostState, CoroutineScope>) -> Unit
) {
	val snackbarHostState = remember { SnackbarHostState() }
	Scaffold(
		snackbarHost = {
			SnackbarHost(hostState = snackbarHostState)
		},
		topBar = {
			AppBarTop(
				logo = logo,
				scrollBehavior = TopAppBarDefaults
					.pinnedScrollBehavior(rememberTopAppBarState()),
				title = { title() },
				actions = actions
			)
		},
		bottomBar = bottomNavigation,
		floatingActionButtonPosition = FabPosition.End,
		floatingActionButton = {
			if (fabVisibility) {
				if (fabText.isNotBlank()) {
					ExtendedFloatingActionButton(
						onClick = fabClick,
						containerColor = MaterialTheme.colorScheme.secondaryContainer,
						contentColor = MaterialTheme.colorScheme.secondary
					) {
						Icon(fabIcon, fabText)
						Text(fabText)
					}
				} else {
					FloatingActionButton(
						onClick = fabClick,
						containerColor = MaterialTheme.colorScheme.secondaryContainer,
						contentColor = MaterialTheme.colorScheme.secondary
					) {
						Icon(fabIcon, "")
					}
				}
			}
		}
	) {
		EnterAnimation {
			contentView(it, Pair(snackbarHostState, rememberCoroutineScope()))
		}
	}
}


data class NavDef(
	val name: String,
	val icon: ImageVector
)

@Composable
fun GenericNavigation(
	modifier: Modifier = Modifier,
	navItems: Set<NavDef>,
	initialSelection: NavDef,
	onItemSelected: (item: NavDef) -> Unit
) {
	NavigationBar(
		modifier = modifier,
		containerColor = MaterialTheme.colorScheme.background,
		contentColor = MaterialTheme.colorScheme.primary,
		tonalElevation = 4.dp
	) {
		var selected by remember { mutableStateOf(initialSelection) }
		navItems.forEach { item ->
			NavigationBarItem(
				colors = NavigationBarItemDefaults.colors(),
				icon = {
					Icon(
						imageVector = item.icon,
						contentDescription = null,
						tint = MaterialTheme.colorScheme.secondary
					)
				},
				label = {
					AutoResizeText(
						text = item.name,
						color = MaterialTheme.colorScheme.secondary,
						fontSizeRange = FontSizeRange(
							min = MaterialTheme.typography.labelSmall.fontSize,
							max = MaterialTheme.typography.labelLarge.fontSize
						),
						maxLines = 1,
						textAlign = TextAlign.Center
					)
				},
				selected = selected == item,
				alwaysShowLabel = false, // This hides the title for the unselected items
				onClick = {
					// This if check gives us a "singleTop" behavior where we do not create a
					// second instance of the composable if we are already on that destination
					if (selected != item) {
						selected = item
						onItemSelected(item)
					}
				}
			)

		}
	}
}