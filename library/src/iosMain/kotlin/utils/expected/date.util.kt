package utils.expected

import platform.QuartzCore.CACurrentMediaTime

actual fun getDefaultDateInMillis(): Long = CACurrentMediaTime().toLong()
