package data.responses

import data.types.Err
import kotlinx.serialization.Serializable
import utils.expected.CrashAnalytics

@Serializable
data class ErrMessage(
	val type: String,
	val message: String,
	val statusCode: Int? = null,
	val description: String = "",
	val actions: Set<ErrActions> = setOf(),

	val mappedMessage: String = if (message.contains("Invalid refresh token (expired):"))
		"Something wrong with the connection. Please try logout and login to resolve the issue."
	else messageMapping[message] ?: message,

	val mappedDescription: String = if (description.contains("Invalid refresh token (expired):"))
		"Something wrong with the connection. Please try logout and login to resolve the issue."
	else messageMapping[description] ?: description
)

val messageMapping = mapOf(
	"invalid_grant : Bad credentials" to "Wrong username or password"
)

enum class ErrActions(val value: String) {
	/*
	 Header will be provided by api as such
	 s-err-action : show-upcoming-page
	 */
	SHOW_CONNECTION_ERR_DIALOG("show-conn-err-dialog"),
	SHOW_UPCOMING_PAGE("show-upcoming-page"),
	TRIGGER_OTP_INPUT("trigger-otp-input");


	companion object {
		private const val headerName = "s-err-action"
		fun from(headers: Map<String, List<String>>): Set<ErrActions> {
			val header = headers[headerName] ?: emptyList()
			val actions = header.mapNotNull {
				fromValue(it)
			}.toSet()
			return actions
		}

		private fun fromValue(value: String) =
			entries.firstOrNull { it.value == value }
	}
}

enum class ErrTypes(val type: String, val msg: String) {
	VALIDATION("Validation", "Validation Failed"),
	NOT_AUTHENTICATED("Not Authenticated", "User not authenticated")
}

fun Err.toMessage(): ErrMessage = when (this) {
	Err.GenericError -> {
		ErrMessage(
			actions = HashSet(),
			type = this::class.simpleName ?: "",
			message = this.throwable.message ?: ""
		)
	}

	Err.NotExistsError -> {
		ErrMessage(
			actions = HashSet(),
			type = this::class.simpleName ?: "",
			message = this.throwable.message ?: ""
		)
	}

	is Err.UserErr -> {
		ErrMessage(
			actions = HashSet(),
			type = this::class.simpleName ?: "",
			message = this.throwable.message ?: ""
		)
	}

	is Err.ValidationErr -> {
		when (this) {
			is Err.ValidationErr.Generic -> ErrMessage(
				this::class.simpleName ?: "",
				this.throwable.message ?: ""
			)

			is Err.ValidationErr.TextValidationErr -> ErrMessage(
				this::class.simpleName ?: "",
				this.throwable.message ?: ""
			)

			is Err.ValidationErr.PhoneValidationErr -> ErrMessage(
				this::class.simpleName ?: "",
				this.throwable.message ?: ""
			)

			is Err.ValidationErr.EmailValidationErr -> ErrMessage(
				this::class.simpleName ?: "",
				this.throwable.message ?: ""
			)
		}
	}

	is Err.ParseErr -> {
		CrashAnalytics.capture(this.throwable)
		when (this) {
			is Err.ParseErr.JsonParseErr -> ErrMessage(
				actions = HashSet(),
				type = this::class.simpleName ?: "",
				message = this.throwable.message ?: ""
			)

			is Err.ParseErr.DateParseErr -> ErrMessage(
				actions = HashSet(),
				type = this::class.simpleName ?: "",
				message = this.throwable.message ?: ""
			)
		}
	}

	is Err.HttpErr<*> -> {
		CrashAnalytics.capture(this.throwable)
		when (this) {
			is Err.HttpErr.ClientErr<*> -> ErrMessage(
				actions = ErrActions.from(this.headers),
				type = this::class.simpleName ?: "",
				message = body.fold({ this.throwable.message ?: "" }, { it.toString() }),
				statusCode = this.statusCode,
				description = body.fold({ "" }, { it.toString() })
			)

			is Err.HttpErr.RedirectErr<*> -> ErrMessage(
				actions = HashSet(),
				type = this::class.simpleName ?: "",
				message = this.throwable.message ?: "",
				statusCode = this.statusCode,
				description = body.fold({ "" }, { it.toString() })
			)

			is Err.HttpErr.ServerErr<*> -> ErrMessage(
				actions = HashSet(),
				type = this::class.simpleName ?: "",
				message = this.throwable.message ?: "",
				statusCode = this.statusCode,
				description = body.fold({ "" }, { it.toString() })
			)

			is Err.HttpErr.GenericHttpErr<*> -> ErrMessage(
				actions = HashSet(),
				type = this::class.simpleName ?: "",
				message = this.throwable.message ?: "",
				statusCode = this.statusCode,
				description = body.fold({ "" }, { it.toString() })
			)

			is Err.HttpErr.HttpJsonParseErr<*> -> ErrMessage(
				actions = HashSet(),
				type = this::class.simpleName ?: "",
				message = this.throwable.message ?: "",
				statusCode = this.statusCode,
				description = body.fold({ "" }, { it.toString() })
			)


			is Err.HttpErr.ConnectionErr<*> -> ErrMessage(
				actions = setOf(ErrActions.SHOW_CONNECTION_ERR_DIALOG),
				type = this::class.simpleName ?: "",
				message = "Internet Connection Unavailable",
				statusCode = this.statusCode,
				description = body.fold({ "" }, { it.toString() })
			)
		}
	}

}