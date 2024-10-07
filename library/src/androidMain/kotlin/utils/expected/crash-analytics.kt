package utils.expected

import android.content.Context
import io.sentry.kotlin.multiplatform.OptionsConfiguration
import io.sentry.kotlin.multiplatform.Sentry

actual class CrashAnalytics(private val context: Context) {

	private fun optionsConfiguration(dsn: String): OptionsConfiguration = {
		it.dsn = dsn
	}

	actual fun init(dsn: String) {
		Sentry.init(context, optionsConfiguration(dsn))
	}

	actual companion object {
		actual fun capture(throwable: Throwable) {
			Sentry.captureException(throwable)
		}
	}
}