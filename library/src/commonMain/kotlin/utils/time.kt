package utils

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.math.min

fun Instant.toReadableDate(timeZone: TimeZone = TimeZone.currentSystemDefault()): String {
	val localDate = this.toLocalDateTime(timeZone).date
	val month = localDate.month.name.lowercase()
		.replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }
	val monthShort = month.substring(0, min(3, month.length))
	return "$monthShort ${localDate.dayOfMonth}, ${localDate.year}"
}

fun Instant.toReadableTime(
	timeZone: TimeZone = TimeZone.currentSystemDefault(),
	withSeconds: Boolean = false
): String {
	val localTime = this.toLocalDateTime(timeZone).time
	return "${localTime.hour}:${localTime.minute}" + if (withSeconds) ":${localTime.second}" else ""
}

fun Instant.toReadableDuration(
	instant: Instant = Clock.System.now(),
	short: Boolean = false
): String {
	val duration = (instant - this).absoluteValue
	val days = duration.inWholeDays
	val hours = duration.inWholeHours % 24
	val minutes = duration.inWholeMinutes % 60
	val seconds = duration.inWholeSeconds % 60
	val years = duration.inWholeDays / 365
	val yName = if (short) "y" else "years"
	val dName = if (short) "d" else "days"
	val hName = if (short) "h" else "hours"
	val mnName = if (short) "m" else "minutes"
	val sName = if (short) "s" else "seconds"
	return when {
		years > 0 -> "$years $yName ${days % 365} $dName"
		days > 0 -> "$days $dName $hours $hName"
		hours > 0 -> "$hours $hName $minutes $mnName"
		else -> "$minutes $mnName $seconds $sName"
	}
}

fun Instant.toHumanReadable(
	timeZone: TimeZone = TimeZone.currentSystemDefault()
): String {
	val duration = Clock.System.now() - this
	return if (duration.absoluteValue.inWholeDays > 0) {
		this.toReadableDate(timeZone) + " " + this.toReadableTime(timeZone)
	} else {
		this.toReadableDuration(short = true)
	}
}

fun secondsToTime(seconds: Long): String {
	val hours = seconds / 3600
	val minutes = (seconds % 3600) / 60
	val remainingSeconds = seconds % 60

	return "$hours h $minutes m $remainingSeconds s"
}
