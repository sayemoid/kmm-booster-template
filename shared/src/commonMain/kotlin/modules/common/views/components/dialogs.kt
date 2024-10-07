package modules.common.views.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerColors
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atTime
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import modules.common.views.dimensions.Paddings

import utils.Tag
import utils.logD
import utils.toReadableDate
import utils.toReadableTime

@Composable
fun WDialog(
	modifier: Modifier = Modifier,
	onDismissRequest: () -> Unit,
	properties: DialogProperties = DialogProperties(),
	content: @Composable ColumnScope.() -> Unit
) {
	Dialog(
		onDismissRequest = { onDismissRequest() },
		properties = properties
	) {
		Card(
			modifier = Modifier
				.fillMaxWidth()
				.then(modifier),
			shape = RoundedCornerShape(10.dp),
		) {
			content()
		}
	}
}

@Composable
fun WAlertDialog(
	title: String,
	body: String,
	titleContentColor: Color = MaterialTheme.colorScheme.primary,
	textContentColor: Color = MaterialTheme.colorScheme.onSurface,
	containerColor: Color = MaterialTheme.colorScheme.surface,
	onDismissed: () -> Unit,
	onConfirm: (() -> Unit)? = null,
) {
	AlertDialog(
		onDismissRequest = onDismissed,
		modifier = Modifier.wrapContentSize(),
		confirmButton = {
			if (onConfirm != null) {
				Button(
					onClick = onConfirm,
					colors = ButtonDefaults.buttonColors(
						contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
						containerColor = MaterialTheme.colorScheme.primaryContainer
					)
				) {
					Text(text = "Confirm")
				}
			}
		},
		containerColor = containerColor,
		textContentColor = textContentColor,
		titleContentColor = titleContentColor,
		dismissButton = {
			Button(
				onClick = onDismissed,
				colors = ButtonDefaults.buttonColors(
					contentColor = titleContentColor,
					containerColor = containerColor
				)
			) {
				Text(text = "Close")
			}
		},
		text = {
			Column(
				modifier = Modifier
					.padding(Paddings.Dialogs.surround)
					.verticalScroll(rememberScrollState()),
				verticalArrangement = Arrangement.Center,
				horizontalAlignment = Alignment.CenterHorizontally,
			) {
				WSubtitleText(
					text = title,
					color = titleContentColor,
					textAlign = TextAlign.Center,
					maxLines = 2,
					style = MaterialTheme.typography.titleLarge
				)
				Spacer(modifier = Modifier.height(16.dp))
				WParagraph(
					text = body,
					color = textContentColor,
					textAlign = TextAlign.Justify,
					maxLines = Int.MAX_VALUE,
					style = MaterialTheme.typography.bodyLarge
				)

			}
		}
	)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WDatePickerDialog(
	onDismissed: () -> Unit,
	onPicked: (date: Instant) -> Unit,
	modifier: Modifier = Modifier,
	shape: Shape = DatePickerDefaults.shape,
	tonalElevation: Dp = DatePickerDefaults.TonalElevation,
	colors: DatePickerColors = DatePickerDefaults.colors(),
	properties: DialogProperties = DialogProperties(
		usePlatformDefaultWidth = false,
		dismissOnBackPress = true,
		dismissOnClickOutside = true
	),
	validator: (datetimeMillis: Long) -> Boolean = { true }
) {
	val datePickerState = rememberDatePickerState()

	var date by remember {
		mutableStateOf(
			datePickerState.selectedDateMillis ?: Clock.System.now().toEpochMilliseconds()
		)
	}

	DatePickerDialog(
		onDismissRequest = onDismissed,
		confirmButton = {
			FilledTonalButton(
				modifier = Modifier
					.padding(
						horizontal = Paddings.Dialogs.Buttons.horizontal,
						vertical = Paddings.Dialogs.Buttons.vertical
					),
				onClick = {
					val selected = datePickerState.selectedDateMillis
					if (selected != null) {
						date = datePickerState.selectedDateMillis!!
					}
					onPicked(Instant.fromEpochMilliseconds(date))
				}
			) {
				Text(
					text = "CHOOSE",
				)
			}
		},
		modifier = modifier,
		dismissButton = {
			TextButton(
				modifier = Modifier
					.padding(
						horizontal = 0.dp,
						vertical = Paddings.Dialogs.Buttons.vertical
					),
				onClick = { onDismissed() },
			) {
				Text(
					text = "CANCEL",
				)
			}
		},
		shape = shape,
		tonalElevation = tonalElevation,
		colors = colors,
		properties = properties
	) {
		DatePicker(
			state = datePickerState,
//			dateValidator = validator
		)
	}
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WTimePickerDialog(
	onDismissed: () -> Unit,
	onPicked: (Pair<Int, Int>) -> Unit
) {
	val currentTime = Clock.System.now()
		.toLocalDateTime(TimeZone.currentSystemDefault())
		.time

	val timePickerState = rememberTimePickerState(
		initialHour = currentTime.hour,
		initialMinute = currentTime.minute,
		is24Hour = true
	)

	var time by remember {
		mutableStateOf(
			Pair(timePickerState.hour, timePickerState.minute)
		)
	}

	TimePickerDialog(
		onCancel = onDismissed,
		onConfirm = {
			time = Pair(timePickerState.hour, timePickerState.minute)
			onPicked(time)
		},
	) {
		TimePicker(
			state = timePickerState,
		)
	}
}


@Composable
fun TimePickerDialog(
	title: String = "Select Time",
	onCancel: () -> Unit,
	onConfirm: () -> Unit,
	toggle: @Composable () -> Unit = {},
	content: @Composable () -> Unit,
) {
	Dialog(
		onDismissRequest = onCancel,
		properties = DialogProperties(
			usePlatformDefaultWidth = false
		),
	) {
		Surface(
			shape = MaterialTheme.shapes.extraLarge,
			tonalElevation = 6.dp,
			modifier = Modifier
				.width(IntrinsicSize.Min)
				.height(IntrinsicSize.Min)
				.background(
					shape = MaterialTheme.shapes.extraLarge,
					color = MaterialTheme.colorScheme.surface
				),
		) {
			Column(
				modifier = Modifier.padding(24.dp),
				horizontalAlignment = Alignment.CenterHorizontally
			) {
				Text(
					modifier = Modifier
						.fillMaxWidth()
						.padding(bottom = 20.dp),
					text = title,
					style = MaterialTheme.typography.labelMedium
				)
				content()
				Row(
					modifier = Modifier
						.height(40.dp)
						.fillMaxWidth()
				) {
					toggle()
					Spacer(modifier = Modifier.weight(1f))
					TextButton(
						onClick = onCancel
					) { Text("Cancel") }
					TextButton(
						onClick = onConfirm
					) { Text("OK") }
				}
			}
		}
	}
}


@Composable
fun WErrorDialog(
	title: String, description: String,
	showDialog: Boolean,
	onDismissRequest: () -> Unit,
	onConfirm: () -> Unit
) {
	if (showDialog) {
		AlertDialog(
			onDismissRequest = { onDismissRequest() },
			title = {
				Text(
					text = title,
					color = Color.Red,
					fontWeight = FontWeight.Bold,
					fontSize = 20.sp,
					modifier = Modifier.padding(bottom = 8.dp)
				)
			},
			text = {
				Text(
					text = description,
					color = Color.Red,
					fontSize = 16.sp,
					modifier = Modifier.padding(bottom = 8.dp)
				)
			},
			confirmButton = {
				Button(
					onClick = { onConfirm() },
					modifier = Modifier.padding(8.dp)
				) {
					Text(text = "OK")
				}
			}
		)
	}
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WDatePickerDialogOpener(
	modifier: Modifier = Modifier,
	validator: (datetimeMillis: Long) -> Boolean = { true },
	onItemSelected: (Instant) -> Unit
) {

	var showDpd by remember { mutableStateOf(false) }
	var date by remember { mutableStateOf(Clock.System.now()) }

	if (showDpd)
		WDatePickerDialog(
			validator = validator,
			onDismissed = {
				showDpd = false
			},
			onPicked = {
				showDpd = false
				date = it
				onItemSelected(it)
				logD(Tag.View.DatePicker, it.toReadableDate())
			},
		)

	Button(
		modifier = Modifier.padding(8.dp)
			.clickable { showDpd = true }
			.then(modifier),
		colors = ButtonDefaults.buttonColors(
			containerColor = MaterialTheme.colorScheme.secondary,
			contentColor = MaterialTheme.colorScheme.onSecondary
		),
		onClick = { showDpd = true }
	) {
		Text(date.toReadableDate())
	}

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WDateTimePickerDialogOpener(
	modifier: Modifier = Modifier,
	initialDateTime: Instant? = null,
	validator: (datetimeMillis: Long) -> Boolean = { true },
	onItemSelected: (Instant) -> Unit
) {

	var showDpd by remember { mutableStateOf(false) }
	var showTpd by remember { mutableStateOf(false) }
	var datetime by remember { mutableStateOf(initialDateTime) }

	if (showDpd) {
		WDatePickerDialog(
			validator = validator,
			onDismissed = {
				showDpd = false
				showTpd = false
			},
			onPicked = {
				datetime = it
				showDpd = false
				showTpd = true
			},
		)
	}

	if (showTpd) {
		WTimePickerDialog(
			onDismissed = {
				showTpd = false
			},
			onPicked = { hm ->
				val (hour, minute) = hm
				showTpd = false
				val dt = (datetime ?: Clock.System.now())
					.toLocalDateTime(TimeZone.currentSystemDefault())
				val time = LocalTime(hour = hour, minute = minute)
				val newDateTIme = dt.date.atTime(time)
				datetime = newDateTIme.toInstant(TimeZone.currentSystemDefault())
				datetime?.let(onItemSelected)
			}
		)
	}

	Button(
		modifier = Modifier.padding(8.dp)
			.clickable { showDpd = true }
			.then(modifier),
		colors = ButtonDefaults.buttonColors(
			containerColor = MaterialTheme.colorScheme.secondaryContainer,
			contentColor = MaterialTheme.colorScheme.onSecondaryContainer
		),
		onClick = { showDpd = true }
	) {
		Text(
			datetime?.let {
				"${it.toReadableDate()} ${it.toReadableTime()}"
			} ?: "Pick a datetime"
		)
	}

}