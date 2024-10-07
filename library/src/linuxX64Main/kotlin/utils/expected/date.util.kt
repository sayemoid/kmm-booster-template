package utils.expected

import arrow.fx.coroutines.timeInMillis

actual fun getDefaultDateInMillis(): Long = timeInMillis()
