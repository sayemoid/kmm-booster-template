package utils.expected

import platform.Foundation.NSUUID
import kotlin.experimental.ExperimentalNativeApi

actual fun uuid(): String = NSUUID().UUIDString()

@OptIn(ExperimentalNativeApi::class)
actual val isDebug = Platform.isDebugBinary

actual fun platform(): Platforms = Platforms.IOS
