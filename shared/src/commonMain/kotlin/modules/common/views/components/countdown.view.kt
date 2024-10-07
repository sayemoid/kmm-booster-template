package modules.common.views.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Circle
import androidx.compose.material.icons.outlined.Timelapse
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import modules.common.views.dimensions.Paddings

import utils.secondsToTime
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

@Composable
fun CountDownView(
	activeFrom: Instant,
	activeUntil: Instant?,
	colorHappening: Color? = null,
	colorScheduled: Color? = null,
	colorExpired: Color? = null
) {

	CountDown(activeFrom - Clock.System.now()) { seconds ->
		if (seconds > 0) {
			Icon(
				modifier = Modifier.size(
					MaterialTheme.typography.labelLarge.fontSize.value.dp
				),
				imageVector = Icons.Outlined.Timelapse,
				contentDescription = "Timer Icon",
				tint = colorScheduled ?: MaterialTheme.colorScheme.tertiary,
			)
			Spacer(
				modifier = Modifier.padding(
					Paddings.Internal.SmallObjects.tiny,
					0.dp,
				)
			)
			WLabel(
				textAlign = TextAlign.Center,
				text = "Scheduled\n${secondsToTime(seconds)}",
				color = colorScheduled ?: MaterialTheme.colorScheme.tertiary,
				style = MaterialTheme.typography.labelSmall
			)
		} else {
			activeUntil?.let { activeUntil ->
				val dur = activeUntil - Clock.System.now()
				CountDown(dur) { secs ->
					if (secs > 0) {
						Column {
							Row(
								verticalAlignment = Alignment.CenterVertically,
								horizontalArrangement = Arrangement.SpaceBetween
							) {
								val number = 100
								val delay = 5L
								HeartBeat(
									number = number,
									delay = delay,
									bidirectional = true
								) { count, directionFlag ->
									val bidirectionalRotation = ((count * 180) / number)
									val rotation = if (directionFlag) bidirectionalRotation
									else 180 - bidirectionalRotation
									Icon(
										modifier = Modifier
											.size(MaterialTheme.typography.labelLarge.fontSize.value.dp)
											.padding((count / 20).dp)
											.rotate(rotation.toFloat()),
										imageVector = Icons.Outlined.Circle,
										contentDescription = "Timer Icon",
										tint = colorHappening ?: MaterialTheme.colorScheme.primary,
									)
								}
								Spacer(
									modifier = Modifier.padding(
										Paddings.Internal.SmallObjects.tiny,
										0.dp,
									)
								)
								WLabel(
									textAlign = TextAlign.Center,
									text = "Happening Now!",
									color = colorHappening ?: MaterialTheme.colorScheme.primary,
									style = MaterialTheme.typography.labelSmall
								)
							}

							WLabel(
								textAlign = TextAlign.Center,
								text = "${secondsToTime(secs)} left",
								color = MaterialTheme.colorScheme.primary,
								style = MaterialTheme.typography.labelSmall
							)
						}
					} else {
						Icon(
							modifier = Modifier.size(
								MaterialTheme.typography.labelLarge.fontSize.value.dp
							),
							imageVector = Icons.Outlined.Timelapse,
							contentDescription = "Timer Icon",
							tint = colorExpired ?: MaterialTheme.colorScheme.error,
						)
						Spacer(
							modifier = Modifier.padding(
								Paddings.Internal.SmallObjects.tiny,
								0.dp,
							)
						)
						WLabel(
							textAlign = TextAlign.Center,
							text = "Expired",
							color = colorExpired ?: MaterialTheme.colorScheme.error,
							style = MaterialTheme.typography.labelSmall
						)
					}
				}
			} ?: Row(
				verticalAlignment = Alignment.CenterVertically
			) {
				val number = 100
				val delay = 5L
				HeartBeat(
					number = number,
					delay = delay,
					bidirectional = true
				) { count, directionFlag ->
					val bidirectionalRotation = ((count * 180) / number)
					val rotation = if (directionFlag) bidirectionalRotation
					else 180 - bidirectionalRotation

					val padding = (count / 20)

//					val color = colorHappening ?: MaterialTheme.colorScheme.primary
//					var colorConditional by remember { mutableStateOf(color) }
//					if (count == 0) {
//						colorConditional = parseColor(randomColorFromList())
//					}

					Icon(
						modifier = Modifier
							.size(MaterialTheme.typography.labelLarge.fontSize.value.dp)
							.padding(padding.dp)
							.rotate(rotation.toFloat()),
						imageVector = Icons.Outlined.Circle,
						contentDescription = "Timer Icon",
						tint = colorHappening ?: MaterialTheme.colorScheme.primary,
					)
				}

				Spacer(
					modifier = Modifier.padding(
						Paddings.Internal.SmallObjects.tiny,
						0.dp,
					)
				)
				WLabel(
					textAlign = TextAlign.Center,
					text = "Happening Now!",
					color = colorHappening ?: MaterialTheme.colorScheme.primary,
					style = MaterialTheme.typography.labelSmall
				)
			}
		}
	}
}

@Composable
fun HeartBeat(
	number: Int,
	delay: Long,
	bidirectional: Boolean = false,
	content: @Composable (count: Int, directionFlag: Boolean) -> Unit
) {
	var count by remember { mutableStateOf(number) }
	var directionFlag by remember { mutableStateOf(false) }

	suspend fun repeater() {
		repeat(count) {
			delay(delay)
			count--
			if (count <= 0) {
				count = number
				directionFlag = !directionFlag
				repeater()
			}
		}
	}
	LaunchedEffect(Unit) {
		coroutineScope {
			launch {
				repeater()
			}
		}
	}
	if (bidirectional) {
		val num = if (directionFlag) number - count else count
		content(num, directionFlag)
	} else
		content(count, directionFlag)
}

@Composable
fun CountDown(
	duration: Duration,
	content: @Composable (secondsLeft: Long) -> Unit
) {
	var timer by remember { mutableStateOf(duration.inWholeSeconds) }

	LaunchedEffect(Unit) {
		coroutineScope {
			repeat(timer.toInt()) {
				delay(1.seconds)
				timer--
			}
		}

	}

	content(timer)
}