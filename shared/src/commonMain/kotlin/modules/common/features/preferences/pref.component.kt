package modules.common.features.preferences

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowForwardIos
import androidx.compose.material.icons.outlined.ArrowForwardIos
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import modules.common.views.components.WLabel
import modules.common.views.components.WMetaText
import modules.common.views.components.WPrefText
import modules.common.views.dimensions.Paddings

@Composable
fun PrefSection(
	modifier: Modifier = Modifier,
	title: String,
	isDarkTheme: Boolean,
	content: @Composable ColumnScope.() -> Unit
) {
	val borderColor = if (isDarkTheme) Color.DarkGray else Color.LightGray
	Surface(
		modifier = Modifier.fillMaxWidth().wrapContentHeight()
			.background(MaterialTheme.colorScheme.background)
			.padding(10.dp)
			.then(modifier)
	) {
		Column(
			modifier = Modifier,
			horizontalAlignment = Alignment.CenterHorizontally,
			verticalArrangement = Arrangement.SpaceEvenly
		) {
			WLabel(
				modifier = Modifier.fillMaxWidth()
					.background(MaterialTheme.colorScheme.surfaceVariant)
					.padding(5.dp),
				text = title,
				color = MaterialTheme.colorScheme.primary,
				textAlign = TextAlign.Start
			)
			Column {
				content()
				Spacer(
					modifier = Modifier.height(1.dp)
						.fillMaxWidth()
						.background(borderColor)
				)
			}
		}
	}
}

enum class PrefType {
	DROPDOWN, GENERIC_ACTION
}

@Composable
fun <T> PrefItemView(
	pref: PrefItem<T>,
	values: Set<T> = setOf(),
	prefType: PrefType = PrefType.GENERIC_ACTION,
	onPrefSelected: (PrefItem<T>) -> Unit = {},
	onClick: (pref: PrefItem<T>) -> Unit = {}
) {
	var showDropdown by remember { mutableStateOf(false) }

	Row(
		modifier = Modifier.fillMaxWidth().wrapContentHeight()
//			.border(BorderStroke(.2.dp, Color.LightGray))
			.clickable {
				when (prefType) {
					PrefType.GENERIC_ACTION -> onClick(pref)
					PrefType.DROPDOWN -> {
						showDropdown = true
					}
				}
				onClick(pref)
			}
			.padding(16.dp),
		verticalAlignment = Alignment.CenterVertically,
		horizontalArrangement = Arrangement.Start
	) {
		Icon(
			modifier = Modifier.size(16.dp),
			imageVector = pref.icon,
			contentDescription = pref.title,
			tint = pref.color
		)
		Spacer(Modifier.width(16.dp))
		Row(
			modifier = Modifier.fillMaxWidth(),
			verticalAlignment = Alignment.CenterVertically,
			horizontalArrangement = Arrangement.SpaceBetween
		) {
			WPrefText(
				text = pref.title,
				color = pref.color
			)
			Spacer(Modifier.width(Paddings.Internal.innerPadding))
			Row(
				verticalAlignment = Alignment.CenterVertically,
				horizontalArrangement = Arrangement.SpaceBetween
			) {
				if (prefType == PrefType.DROPDOWN) {
					WLabel(
						text = pref.value.toString().toHumanInterpreted(),
						color = pref.color
					)
				}
				PrefSelector(
					items = values,
					showDropdown,
					onDismiss = { showDropdown = false }
				) {
					showDropdown = false
					onPrefSelected(pref.copy(value = it))
				}

				Spacer(Modifier.width(Paddings.Internal.SmallObjects.horizontal))
				Icon(
					modifier = Modifier.size(16.dp),
					imageVector = Icons.AutoMirrored.Outlined.ArrowForwardIos,
					contentDescription = pref.title,
					tint = pref.color
				)
			}
		}
	}
}

@Composable

fun <T> PrefItemViewCard(
	pref: PrefItem<T>,
	contentColor: Color = MaterialTheme.colorScheme.primary,
	icon: ImageVector = pref.icon,
	values: Set<T> = setOf(),
	prefType: PrefType = PrefType.GENERIC_ACTION,
	onPrefSelected: (PrefItem<T>) -> Unit = {},
	onClick: (pref: PrefItem<T>) -> Unit = {}
) {
	var showDropdown by remember { mutableStateOf(false) }

	Column(
		modifier = Modifier.fillMaxWidth().wrapContentHeight()
//			.border(BorderStroke(.2.dp, Color.LightGray))
			.clickable {
				when (prefType) {
					PrefType.GENERIC_ACTION -> onClick(pref)
					PrefType.DROPDOWN -> {
						showDropdown = true
					}
				}
				onClick(pref)
			}
			.padding(16.dp),
		horizontalAlignment = Alignment.CenterHorizontally,
		verticalArrangement = Arrangement.SpaceEvenly
	) {
		Icon(
			modifier = Modifier.size(72.dp),
			imageVector = icon,
			contentDescription = pref.title,
			tint = contentColor
		)
		Spacer(Modifier.height(16.dp))
		Column(
			modifier = Modifier.fillMaxWidth(),
			horizontalAlignment = Alignment.CenterHorizontally,
			verticalArrangement = Arrangement.SpaceBetween
		) {
			WPrefText(
				text = pref.title,
				color = contentColor
			)
			Spacer(Modifier.height(Paddings.Internal.innerPadding))
			Column(
				horizontalAlignment = Alignment.CenterHorizontally
			) {
				if (prefType == PrefType.DROPDOWN) {
					WLabel(
						text = pref.value.toString().toHumanInterpreted(),
						color = pref.color
					)
				}
				PrefSelector(
					items = values,
					showDropdown,
					onDismiss = { showDropdown = false }
				) {
					showDropdown = false
					onPrefSelected(pref.copy(value = it))
				}

				Spacer(Modifier.width(Paddings.Internal.SmallObjects.horizontal))
				Icon(
					modifier = Modifier.size(16.dp).rotate(90f),
					imageVector = Icons.AutoMirrored.Outlined.ArrowForwardIos,
					contentDescription = pref.title,
					tint = contentColor
				)
			}
		}
	}
}

private fun String.toHumanInterpreted() = when (this) {
	"true" -> "ON"
	"false" -> "OFF"
	else -> this
}

@Composable
fun <T> PrefSelector(
	items: Set<T>,
	expanded: Boolean = false,
	onDismiss: () -> Unit,
	onSelected: (value: T) -> Unit
) {
	DropdownMenu(
		expanded = expanded,
		onDismissRequest = onDismiss
	) {
		items.forEach { item ->
			DropdownMenuItem(
				text = {
					WMetaText(
						text = item.toString()
							.toHumanInterpreted(),
						onClick = { onSelected(item) }
					)
				},
				onClick = { onSelected(item) }
			)
		}

	}
}