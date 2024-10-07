package modules.common.views.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import arrow.core.toOption
import modules.common.views.components.expected.ImageLoader
import modules.common.views.dimensions.Paddings


@Composable
fun FlexCard(
	modifier: Modifier = Modifier,
	data: FlexCardData,
	style: FlexCardStyle = FlexCardStyle.BackgroundImage,
	colors: CardColors = CardDefaults.cardColors(
		containerColor = data.backgroundColor,
		contentColor = data.color
	),
	click: (data: FlexCardData) -> Unit,
	descMaxLines: Int = 3,
	labelView: (@Composable () -> Unit) = {},
	metaInfoView: (@Composable () -> Unit) = {},
	actionButtons: (@Composable RowScope.() -> Unit) = {}
) {
	ElevatedCard(
		modifier = Modifier.clickable {
			click(data)
		},
		elevation = CardDefaults.cardElevation(
			defaultElevation = 6.dp
		),
		colors = colors,
		shape = RoundedCornerShape(style.cornerRadius)
	) {

		var m = Modifier.fillMaxSize()
		data.height?.let { m = m.height(it.dp) }
		m = m.then(modifier)
		ConditionalBackground(
			modifier = m,
			image = data.image,
			style = style,
			height = data.height
		) {

			val textStyle = when (style) {
				is FlexCardStyle.ImageTop -> TextStyle()
				FlexCardStyle.BackgroundImage -> data.image?.let {
					TextStyle(
						fontWeight = FontWeight.Bold,
						shadow = Shadow(
							color = data.backgroundColor,
							blurRadius = 10f,
							offset = Offset(5f, 5f)
						),
					)
				} ?: TextStyle()
			}

			Box(
				modifier = Modifier.fillMaxSize()
					.padding(
						Paddings.Card.horizontal,
						Paddings.Card.vertical
					)
			) {
				Column(
					Modifier
						.align(Alignment.TopStart)
				) {
					labelView()
					metaInfoView()
					WTitleText(
						modifier = Modifier.fillMaxWidth(),
						textAlign = TextAlign.Left,
						text = data.title,
						color = data.color,
						style = textStyle.copy(
							fontSize = MaterialTheme.typography.labelMedium.fontSize,
							lineHeight = 24.sp
						),
						maxLines = 3,
						onClick = {
							click(data)
						}
					)

					if (!data.description.isNullOrBlank()) {
						DetailDialogText(
							modifier = Modifier.fillMaxWidth()
								.padding(0.dp, Paddings.Card.internalVertical),
							title = data.title,
							text = data.description,
							color = data.color,
							style = textStyle.copy(
								fontSize = MaterialTheme.typography.bodyMedium.fontSize
							),
							maxLines = descMaxLines,
						)
					}
				}

				Row(modifier = Modifier.align(Alignment.BottomCenter)) {
					actionButtons()
				}

				data.icon?.let {
					ImageLoader(
						modifier = Modifier
							.align(Alignment.BottomEnd)
							.size(data.iconSize(style).dp),
						url = it,
						scale = ContentScale.Inside
					)
				}
			}
		}
	}
}

@Composable
fun ConditionalBackground(
	modifier: Modifier = Modifier,
	style: FlexCardStyle,
	height: Int?,
	image: String?,
	content: @Composable () -> Unit
) {
	var m = Modifier.fillMaxSize()
	height?.let { m = m.height(it.dp) }
	m = m.then(modifier)

	when (style) {
		FlexCardStyle.BackgroundImage -> {
			Box(
				modifier = m
			) {
				image?.let { image ->
					ImageLoader(
						modifier = Modifier.fillMaxSize()
							.blur(2.dp),
						url = image
					)
				}

				content()
			}
		}

		is FlexCardStyle.ImageTop -> {
			Column(
				modifier = m
			) {
				image?.let { image ->
					var imgMod = Modifier.fillMaxWidth()
					style.maxHeightFraction?.let { fraction ->
						imgMod = imgMod.fillMaxHeight(fraction)
					}
					imgMod.clip(RoundedCornerShape(style.cornerRadius))
					ImageLoader(
						modifier = imgMod,
						url = image,
					)
				}

				content()
			}
		}
	}
}

data class FlexCardData(

	val identifier: Long,

	val title: String,

	val description: String?,

	val icon: String?,

	val image: String?,

	val color: Color,

	val backgroundColor: Color,

	val height: Int?,
) {
	fun iconSize(style: FlexCardStyle) = when (style) {
		FlexCardStyle.BackgroundImage -> {
			image.toOption().fold(
				{
					val percentage = 20
					height?.let { h ->
						(h * percentage) / 100
					} ?: 50
				},
				{
					50
				}
			)
		}

		is FlexCardStyle.ImageTop -> 24
	}
}

sealed class FlexCardStyle(val cornerRadius: Int) {
	data object BackgroundImage : FlexCardStyle(6)
	data class ImageTop(val maxHeightFraction: Float?) : FlexCardStyle(6)
}