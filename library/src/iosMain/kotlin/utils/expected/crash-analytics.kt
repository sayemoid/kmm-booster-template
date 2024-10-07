package utils.expected

import utils.Tag
import utils.logD

actual class CrashAnalytics(private val debug: Boolean) {
	actual fun init(dsn: String) {
		logD(Tag.Crash, "Sentry initialization for IOS (Not yet implemented)!")
	}

	actual companion object {
		actual fun capture(throwable: Throwable) {}
	}
}