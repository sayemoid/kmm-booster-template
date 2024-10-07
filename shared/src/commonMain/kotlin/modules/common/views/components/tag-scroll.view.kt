package modules.common.views.components

import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgeDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import modules.common.views.components.expected.ImageLoader
import modules.common.views.dimensions.Paddings

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Tags(
	modifier: Modifier = Modifier,
	tags: List<Tag>,
	selectedTag: Tag? = null,
	badgeContainerColor: Color = BadgeDefaults.containerColor,
	badgeContentColor: Color? = null,
	enabled: Boolean = true,
	tagCloud: Boolean = false,
	onClick: (tag: Tag) -> Unit
) {
	var selected by remember { mutableStateOf(selectedTag) }
	TagRow(
		modifier = Modifier
			.then(modifier),
		tags = tags,
		tagCloud = tagCloud
	) { tag ->
		FilterChip(
			modifier = Modifier.padding(
				Paddings.Internal.SmallObjects.tiny,
				0.dp
			),
			enabled = enabled,
			onClick = {
				selected = tag
				onClick(tag)
			},
			label = {
				WLabel(
					text = tag.name,
					color = MaterialTheme.colorScheme.primary
				)
			},
			selected = selected?.id == tag.id,
			leadingIcon = {
				tag.icon?.let {
					ImageLoader(
						modifier = Modifier
							.size(AssistChipDefaults.IconSize),
						url = it,
						scale = ContentScale.Inside,
						contentDescription = tag.name
					)
				} ?: tag.vectorIcon?.let {
					Icon(
						imageVector = it,
						contentDescription = "Icon for ${tag.name}"
					)
				}
			},
			trailingIcon = {
				tag.badgeText?.let {
					Badge(
						containerColor = badgeContainerColor,
						contentColor = badgeContentColor ?: Color.White
					) {
						Text(
							it
						)
					}
				}
			}
		)
		if (tags.lastOrNull()?.id != tag.id) {
			Spacer(modifier = Modifier.width(8.dp))
		}
	}
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun TagRow(
	modifier: Modifier = Modifier,
	tags: List<Tag> = listOf(),
	tagCloud: Boolean = false,
	itemView: @Composable (tag: Tag) -> Unit
) {
	if (!tagCloud) {
		LazyRow(
			modifier = Modifier
				.then(modifier)
		) {
			items(tags.size) { index ->
				val tag = tags[index]
				itemView(tag)
			}
		}
	} else {
		FlowRow(modifier = Modifier.padding(8.dp)) {
			tags.forEach { tag ->
				itemView(tag)
			}
		}
	}
}

data class Tag(
	val id: Long,
	val icon: String? = null,
	val vectorIcon: ImageVector? = null,
	val badgeText: String? = null,
	val name: String,
)