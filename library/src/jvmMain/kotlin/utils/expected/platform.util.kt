package utils.expected

import java.util.UUID

actual fun uuid(): String = UUID.randomUUID().toString()

actual val isDebug = true

actual fun platform(): Platforms = Platforms.JVM
