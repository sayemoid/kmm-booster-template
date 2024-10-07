@file:OptIn(ExperimentalMaterial3Api::class)

package modules.common.views.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppBarTop(
	modifier: Modifier = Modifier,
	logo: @Composable ()->Unit,
	scrollBehavior: TopAppBarScrollBehavior? = null,
	onLogoClicked: () -> Unit = { },
	title: @Composable () -> Unit,
	actions: @Composable RowScope.() -> Unit = {}
) {
	CenterAlignedTopAppBar(
		modifier = modifier,
		actions = actions,
		title = title,
		scrollBehavior = scrollBehavior,
		navigationIcon = {
			HeaderAppLogo(
				modifier = Modifier
					.size(64.dp)
					.clickable(onClick = onLogoClicked)
					.padding(16.dp),
				logo = logo,
				contentDescription = "Open Drawer"
			)
		}
	)
}

@Composable
fun HeaderAppLogo(
	modifier: Modifier = Modifier,
	logo: @Composable ()->Unit,
	contentDescription: String?
) {

	val semantics = contentDescription?.let {
		Modifier.semantics {
			this.contentDescription = contentDescription
			this.role = Role.Image
		}
	} ?: Modifier

	Box(modifier = modifier.then(semantics)) {
		logo()
	}
}
