package modules.common.views.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.InlineTextContent
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cognito_kmm_template.shared.generated.resources.Res
import cognito_kmm_template.shared.generated.resources.charu_chandan_bold
import cognito_kmm_template.shared.generated.resources.charu_chandan_light
import cognito_kmm_template.shared.generated.resources.charu_chandan_regular
import cognito_kmm_template.shared.generated.resources.solaimanlipi
import kotlinx.coroutines.delay
import modules.common.views.dimensions.Paddings

import org.jetbrains.compose.resources.Font
import utils.expected.RemoteUrl

@Composable
fun HyperlinkedText(
	modifier: Modifier = Modifier,
	text: String,
	url: String,
	color: Color = Color.Blue,
	fontSize: TextUnit = TextUnit.Unspecified,
	fontStyle: FontStyle? = null,
	fontWeight: FontWeight? = null,
	fontFamily: FontFamily? = null,
	letterSpacing: TextUnit = TextUnit.Unspecified,
	textDecoration: TextDecoration? = null,
	textAlign: TextAlign? = null,
	lineHeight: TextUnit = TextUnit.Unspecified,
	overflow: TextOverflow = TextOverflow.Clip,
	softWrap: Boolean = true,
	maxLines: Int = Int.MAX_VALUE,
	inlineContent: Map<String, InlineTextContent> = mapOf(),
	onTextLayout: (TextLayoutResult) -> Unit = {},
	style: TextStyle = LocalTextStyle.current,
	onClick: (url: String) -> Unit
) {
	val remoteUrl = RemoteUrl()
	remoteUrl.create()
	Text(
		text = AnnotatedString(text),
		modifier = modifier.clickable(onClick = {
			remoteUrl.open(url)
			onClick(url)
		}),
		color = color,
		fontSize = fontSize,
		fontStyle = fontStyle,
		fontWeight = fontWeight,
		fontFamily = fontFamily,
		letterSpacing = letterSpacing,
		textDecoration = textDecoration ?: TextDecoration.Underline,
		textAlign = textAlign,
		lineHeight = lineHeight,
		overflow = overflow,
		softWrap = softWrap,
		maxLines = maxLines,
		inlineContent = inlineContent,
		onTextLayout = onTextLayout,
		style = style
	)
}


@Composable
fun WTitleText(
	text: String,
	modifier: Modifier = Modifier,
	color: Color = MaterialTheme.colorScheme.primary,
	textAlign: TextAlign = TextAlign.Center,
	style: TextStyle = MaterialTheme.typography.titleMedium,
	maxLines: Int = 2,
	letterSpacing: TextUnit = TextUnit.Unspecified,
	onClick: () -> Unit = {}
) {
	AutoResizeTextV2(
		modifier = modifier,
		text = text,
		style = style,
		color = color,
		textAlign = textAlign,
		overflow = TextOverflow.Ellipsis,
		letterSpacing = letterSpacing,
		maxLines = maxLines,
		fontStyle = style.fontStyle,
		fontWeight = style.fontWeight,
		fontFamily = FontFamily(
			Font(
				resource = Res.font.charu_chandan_bold,
				weight = FontWeight.ExtraBold,
				style = FontStyle.Normal
			)
		),
		fontSizeRange = FontSizeRange(
			min = MaterialTheme.typography.titleSmall.fontSize,
			max = MaterialTheme.typography.titleLarge.fontSize
		),
		onClick = onClick
	)
}

@Composable
fun WSubtitleText(
	text: String,
	modifier: Modifier = Modifier,
	color: Color = MaterialTheme.colorScheme.onPrimary,
	textAlign: TextAlign = TextAlign.Center,
	style: TextStyle = MaterialTheme.typography.titleMedium,
	maxLines: Int = 2,
) {
	AutoResizeText(
		modifier = modifier,
		text = text,
		style = style,
		color = color,
		textAlign = textAlign,
		overflow = TextOverflow.Ellipsis,
		maxLines = maxLines,
		fontFamily = FontFamily(
			Font(
				resource = Res.font.charu_chandan_regular,
				weight = style.fontWeight ?: FontWeight.Normal,
				style = style.fontStyle ?: FontStyle.Normal
			)
		),
		fontSizeRange = FontSizeRange(
			min = MaterialTheme.typography.labelSmall.fontSize,
			max = MaterialTheme.typography.labelLarge.fontSize
		)
	)
}


@Composable
fun WParagraph(
	text: String,
	modifier: Modifier = Modifier,
	color: Color = MaterialTheme.colorScheme.onPrimary,
	style: TextStyle = MaterialTheme.typography.bodySmall,
	textAlign: TextAlign = TextAlign.Left,
	maxLines: Int = 2,
	onClick: () -> Unit = {}
) {
	ExpandableText(
		text = text,
		fontFamily = FontFamily(
			Font(
				resource = Res.font.solaimanlipi,
				weight = style.fontWeight ?: FontWeight.Normal,
				style = style.fontStyle ?: FontStyle.Normal
			)
		),
		color = color,
		modifier = modifier,
		textAlign = textAlign,
		maxLines = maxLines,
		onClick = onClick
	)
}

@Composable
fun DetailDialogText(
	title: String,
	text: String,
	modifier: Modifier = Modifier,
	color: Color = MaterialTheme.colorScheme.onSurface,
	dialogTextColor: Color = MaterialTheme.colorScheme.onSurface,
	backgroundColor: Color = MaterialTheme.colorScheme.surfaceVariant,
	style: TextStyle = MaterialTheme.typography.bodySmall,
	textAlign: TextAlign = TextAlign.Left,
	maxLines: Int = 1,
	confirmAction: (() -> Unit)? = null,
) {
	var showDialog by remember { mutableStateOf(false) }
	var hasOverflow by remember { mutableStateOf(false) }

	Column(
		modifier = modifier
	) {
		Text(
			text = text,
			fontFamily = FontFamily(
				Font(
					resource = Res.font.solaimanlipi,
					weight = style.fontWeight ?: FontWeight.Normal,
					style = style.fontStyle ?: FontStyle.Normal
				)
			),
			color = color,
			textAlign = textAlign,
			maxLines = maxLines,
			modifier = modifier.animateContentSize(),
			onTextLayout = {
				hasOverflow = it.hasVisualOverflow
			}
		)

		if (hasOverflow && !showDialog) {
			Text(
				modifier = Modifier.clickable {
					showDialog = true
				},
				fontFamily = FontFamily(
					Font(
						resource = Res.font.solaimanlipi,
						weight = style.fontWeight ?: FontWeight.Normal,
						style = style.fontStyle ?: FontStyle.Normal
					)
				),
				color = MaterialTheme.colorScheme.primary,
				text = "See more",
			)
		}

		if (showDialog) {
			WAlertDialog(
				title = title,
				body = text,
				textContentColor = dialogTextColor,
				containerColor = backgroundColor,
				onConfirm = confirmAction,
				onDismissed = { showDialog = false },
			)
		}
	}

}

@Composable
fun ExpandableText(
	text: String,
	modifier: Modifier = Modifier,
	color: Color = MaterialTheme.colorScheme.onPrimary,
	fontFamily: FontFamily,
	textAlign: TextAlign = TextAlign.Justify,
	maxLines: Int = 3,
	onClick: () -> Unit = {}
) {
	var isExpanded by remember { mutableStateOf(false) }
	var hasOverflow by remember { mutableStateOf(false) }

	Column(
		modifier = modifier.clickable {
			isExpanded = !isExpanded
			onClick()
		}
	) {
		Text(
			text = text,
			fontFamily = fontFamily,
			color = color,
			textAlign = textAlign,
			maxLines = if (isExpanded) Int.MAX_VALUE else maxLines,
			modifier = modifier.animateContentSize(),
			onTextLayout = {
				hasOverflow = it.hasVisualOverflow
			}
		)

		if (hasOverflow && !isExpanded) {
			Text(
				modifier = Modifier,
				fontFamily = fontFamily,
				color = MaterialTheme.colorScheme.primary,
				text = "See more"
			)
		}
	}
}

@Composable
fun ReemphasizedMeta(
	modifier: Modifier = Modifier,
	text: String,
	style: TextStyle = MaterialTheme.typography.bodySmall
) {
	WMetaText(
		modifier = modifier,
		text = text,
		color = Color.Gray,
		style = style
	)
}

@Composable
fun WMetaText(
	text: String,
	modifier: Modifier = Modifier,
	color: Color = MaterialTheme.colorScheme.onSurface,
	style: TextStyle = MaterialTheme.typography.bodySmall,
	textAlign: TextAlign = TextAlign.Center,
	maxLines: Int = 3,
	onClick: () -> Unit = {}
) {
	AutoResizeTextV2(
		text = text,
		color = color,
		modifier = modifier,
		textAlign = textAlign,
		overflow = TextOverflow.Ellipsis,
		maxLines = maxLines,
		style = style,
		fontFamily = FontFamily(
			Font(
				resource = Res.font.charu_chandan_light,
				weight = style.fontWeight ?: FontWeight.ExtraLight,
				style = style.fontStyle ?: FontStyle.Normal
			)
		),
		fontSizeRange = FontSizeRange(
			min = MaterialTheme.typography.bodySmall.fontSize,
			max = MaterialTheme.typography.bodyMedium.fontSize
		),
		onClick = onClick
	)
}

@Composable
fun WLabel(
	modifier: Modifier = Modifier,
	text: String,
	color: Color = MaterialTheme.colorScheme.tertiary,
	style: TextStyle = MaterialTheme.typography.labelMedium,
	textAlign: TextAlign = TextAlign.Center,
	maxLines: Int = 2,
	fontStyle: FontStyle? = null,
	fontWeight: FontWeight? = null,
	iconLeft: ImageVector? = null
) {
	Row(
		modifier = Modifier,
		verticalAlignment = Alignment.CenterVertically,
		horizontalArrangement = Arrangement.SpaceEvenly
	) {
		iconLeft?.let {
			Icon(
				painter = rememberVectorPainter(iconLeft),
				contentDescription = "$text icon",
				tint = color
			)
			Spacer(modifier = Modifier.padding(Paddings.Internal.SmallObjects.horizontal))
		}
		AutoResizeText(
			modifier = modifier,
			text = text,
			color = color,
			textAlign = textAlign,
			overflow = TextOverflow.Ellipsis,
			maxLines = maxLines,
			fontStyle = fontStyle,
			fontFamily = FontFamily(
				Font(
					resource = Res.font.charu_chandan_regular,
					weight = style.fontWeight ?: FontWeight.Light,
					style = style.fontStyle ?: FontStyle.Normal
				)
			),
			fontWeight = fontWeight,
			fontSizeRange = FontSizeRange(
				min = MaterialTheme.typography.labelSmall.fontSize,
				max = MaterialTheme.typography.labelLarge.fontSize
			)
		)
	}
}


@Composable
fun WPrefText(
	text: String,
	modifier: Modifier = Modifier,
	color: Color = MaterialTheme.colorScheme.onPrimary
) {
	Text(
		text = text,
		style = MaterialTheme.typography.titleMedium,
		color = color,
		modifier = modifier,
		textAlign = TextAlign.Center,
		overflow = TextOverflow.Clip,
		maxLines = 2
	)
}

@Composable
fun QuoteView(
	modifier: Modifier = Modifier,
	quote: String, author: String
) {
	Card(
		modifier = modifier
	) {
		Column(
			modifier = Modifier.padding(Paddings.General.surround)
		) {
			// Display the quote with custom styling
			Text(
				text = "\"$quote\"",
				style = MaterialTheme.typography.headlineSmall.copy(
					fontSize = 18.sp,
					fontWeight = FontWeight.Bold,
					color = Color.Black
				),
				modifier = Modifier.padding(bottom = 8.dp)
			)

			// Display the author's name with lighter styling
			Text(
				text = "- $author",
				style = MaterialTheme.typography.bodyMedium.copy(
					fontSize = 16.sp,
					color = Color.Gray
				)
			)
		}
	}
}

//@Composable
//fun LiveUpdateTextView(
//	modifier: Modifier = Modifier, text: String,
//	color: Color = MaterialTheme.colorScheme.primary,
//	highlightColor: Color = MaterialTheme.colorScheme.tertiary,
//	highlightDurationMillis: Long = 1000
//) {
//	var textColor by remember { mutableStateOf(color) }
//	val animatedColor by animateColorAsState(
//		targetValue = textColor,
//		animationSpec = tween(durationMillis = highlightDurationMillis.toInt()) // Adjust duration as needed
//	)
//
//	LaunchedEffect(text) {
//		textColor = highlightColor
//		delay(highlightDurationMillis)
//		textColor = color
//	}
//
//	// TextView with animated color
//	BasicText(
//		modifier = modifier,
//		text = text,
//		style = TextStyle(color = animatedColor, fontSize = 18.sp)
//	)
//
//}


@Composable
fun LiveUpdateTextView(
	modifier: Modifier = Modifier,
	text: String,
	flip: Boolean = true,
	color: Color = MaterialTheme.colorScheme.primary,
	highlightColor: Color = MaterialTheme.colorScheme.tertiary,
	highlightDurationMillis: Long = 1000,
	textAlign: TextAlign = TextAlign.Center,
	textStyle: TextStyle = MaterialTheme.typography.labelMedium
) {
	var data by remember { mutableStateOf(text) }
	var fliped by remember { mutableStateOf(false) }
	val rotationX by animateFloatAsState(targetValue = if (fliped) 360f else 0f)

	var textColor by remember { mutableStateOf(color) }
	val animatedColor by animateColorAsState(
		targetValue = textColor,
		animationSpec = tween(durationMillis = highlightDurationMillis.toInt()) // Adjust duration as needed
	)

	LaunchedEffect(text) {
		textColor = highlightColor
		delay(highlightDurationMillis) // Halfway through the flip
		data = text
		textColor = color
		if (flip) {
			fliped = !fliped
		}
	}

	Box(modifier = Modifier.graphicsLayer {
		this.rotationX = rotationX
		cameraDistance = 12 * density // Adjust for more pronounced 3D effect
	}) {
		Text(
			modifier = modifier,
			text = data,
			textAlign = textAlign,
			color = animatedColor,
			style = textStyle
		)
	}

}


@Composable
fun DisappearingLiveUpdateTextView(
	modifier: Modifier = Modifier, text: String,
	color: Color = MaterialTheme.colorScheme.primary,
	highlightDurationMillis: Int = 1000,
	textAlign: TextAlign = TextAlign.Center,
	textStyle: TextStyle = MaterialTheme.typography.labelMedium
) {
	var visible by remember { mutableStateOf(false) }
	var displayText by remember { mutableStateOf(text) }

	LaunchedEffect(text) {
		displayText = text
		visible = true
		delay(highlightDurationMillis.toLong())
		visible = false
	}

	AnimatedVisibility(
		visible = visible,
		enter = fadeIn(animationSpec = tween(highlightDurationMillis / 3)),
		exit = fadeOut(animationSpec = tween(highlightDurationMillis / 3))
	) {
		Text(
			modifier = modifier,
			text = displayText,
			color = color,
			textAlign = textAlign,
			style = textStyle
		)
	}
}


@Composable
fun AutoResizeText(
	text: String,
	fontSizeRange: FontSizeRange,
	modifier: Modifier = Modifier,
	color: Color = Color.Unspecified,
	fontStyle: FontStyle? = null,
	fontWeight: FontWeight? = null,
	fontFamily: FontFamily? = null,
	letterSpacing: TextUnit = TextUnit.Unspecified,
	textDecoration: TextDecoration? = null,
	textAlign: TextAlign? = null,
	lineHeight: TextUnit = TextUnit.Unspecified,
	overflow: TextOverflow = TextOverflow.Clip,
	softWrap: Boolean = true,
	maxLines: Int = Int.MAX_VALUE,
	style: TextStyle = LocalTextStyle.current,
	onReachedMinimumFontSize: () -> Unit = {}
) {
	var fontSizeValue by remember { mutableStateOf(fontSizeRange.max.value) }
	var readyToDraw by remember { mutableStateOf(false) }

	Text(
		text = text,
		color = color,
		maxLines = maxLines,
		fontStyle = fontStyle,
		fontWeight = fontWeight,
		fontFamily = fontFamily,
		letterSpacing = letterSpacing,
		textDecoration = textDecoration,
		textAlign = textAlign,
		lineHeight = lineHeight,
		overflow = overflow,
		softWrap = softWrap,
		style = style,
		fontSize = fontSizeValue.sp,
		onTextLayout = {
//			Timber.d("onTextLayout")
			if (it.didOverflowHeight && !readyToDraw) {
//				Timber.d("Did Overflow height, calculate next font size value")
				val nextFontSizeValue = fontSizeValue - fontSizeRange.step.value
				if (nextFontSizeValue <= fontSizeRange.min.value) {
					// Reached minimum, set minimum font size and it's readToDraw
					fontSizeValue = fontSizeRange.min.value
					readyToDraw = true
					onReachedMinimumFontSize()
				} else {
					// Text doesn't fit yet and haven't reached minimum text range, keep decreasing
					fontSizeValue = nextFontSizeValue
				}
			} else {
				// Text fits before reaching the minimum, it's readyToDraw
				readyToDraw = true
			}
		},
		modifier = modifier.drawWithContent { if (readyToDraw) drawContent() }
	)
}

@Composable
fun AutoResizeTextV2(
	text: String,
	fontSizeRange: FontSizeRange,
	modifier: Modifier = Modifier,
	color: Color = Color.Unspecified,
	fontStyle: FontStyle? = null,
	fontWeight: FontWeight? = null,
	fontFamily: FontFamily? = null,
	letterSpacing: TextUnit = TextUnit.Unspecified,
	textDecoration: TextDecoration? = null,
	textAlign: TextAlign? = null,
	lineHeight: TextUnit = TextUnit.Unspecified,
	overflow: TextOverflow = TextOverflow.Clip,
	softWrap: Boolean = true,
	maxLines: Int = Int.MAX_VALUE,
	style: TextStyle = LocalTextStyle.current,
	onClick: () -> Unit = {}
) {
	var expanded by remember { mutableStateOf(false) }
	var hasOverflow by remember { mutableStateOf(false) }

	AutoResizeText(
		text = text,
		fontSizeRange = fontSizeRange,
		modifier = modifier.clickable {
			expanded = !expanded
			onClick()
		},
		color = color,
		fontStyle = fontStyle,
		fontWeight = fontWeight,
		fontFamily = fontFamily,
		letterSpacing = letterSpacing,
		textDecoration = textDecoration,
		textAlign = textAlign,
		lineHeight = lineHeight,
		overflow = overflow,
		softWrap = softWrap,
		maxLines = if (expanded) Int.MAX_VALUE else maxLines,
		style = style,
		onReachedMinimumFontSize = {
			hasOverflow = true
		}
	)
}

data class FontSizeRange(
	val min: TextUnit,
	val max: TextUnit,
	val step: TextUnit = DEFAULT_TEXT_STEP,
) {
	init {
		require(min < max) { "min should be less than max, $this" }
		require(step.value > 0) { "step should be greater than 0, $this" }
	}

	companion object {
		private val DEFAULT_TEXT_STEP = 1.sp
	}
}