package configs

import utils.expected.isDebug

sealed interface Credentials {
	data object Sentry : Credentials {
		// TODO: Update sentry credentials
		val dsn = Credential(
			debug = "https://invalidf46ba474d9invalid57269f51@o4505135996272640.ingest.sentry.io/450513599invalid",
			release = "https://invalidf46ba474d9539eac057269f51@invalid5996272640.ingest.sentry.io/450513599invalid"
		)
	}

	data object Auth : Credentials {
		val clientId = Credential(
			debug = "client_id",
			release = "client_id"
		)
		val clientSecret = Credential(
			debug = "client_secret",
			release = "client_secret"
		)
	}

	data object Ads {
		// ad credentials
		val adUnitInterstitialPB = Credential(
			debug = "",
			release = ""
		)

	}

	data object UserPass {
		val username = Credential(
			debug = "",
			release = ""
		)
		val password = Credential(
			debug = "",
			release = ""
		)
	}

	data class Credential(val debug: String, val release: String) {
		override fun toString() = get()
		fun get() = if (isDebug) debug else release
	}
}



