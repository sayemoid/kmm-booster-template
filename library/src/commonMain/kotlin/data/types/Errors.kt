package data.types

import arrow.core.Option
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.RedirectResponseException
import io.ktor.client.plugins.ResponseException
import io.ktor.client.plugins.ServerResponseException
import io.ktor.util.toMap

sealed class Err(val throwable: Throwable) {

	sealed class ValidationErr(ex: Throwable, val instructionMsg: String) : Err(ex) {
		data class Generic(val ex: Throwable, val instruction: String) :
			ValidationErr(ex, instruction)

		data class TextValidationErr(private val ex: Throwable, private val instruction: String) :
			ValidationErr(ex, instruction)

		data class PhoneValidationErr(private val ex: Throwable, private val instruction: String) :
			ValidationErr(ex, instruction)

		data class EmailValidationErr(private val ex: Throwable, private val instruction: String) :
			ValidationErr(ex, instruction)
	}

	data object GenericError :
		Err(RuntimeException("Generic Error, usually used as a placeholder for error."))

	data object NotExistsError :
		Err(RuntimeException("Data doesn't exist"))

	class UserErr(ex: Throwable) : Err(ex)
	sealed class ParseErr(ex: Throwable) : Err(ex) {
		class JsonParseErr(ex: Throwable) : ParseErr(ex)
		class DateParseErr(ex: Throwable) : ParseErr(ex)
	}

	sealed class HttpErr<T>(ex: Throwable, val statusCode: Int, val body: Option<T>) : Err(ex) {
		class ClientErr<T>(
			ex: Throwable,
			status: Int,
			val headers: Map<String, List<String>>,
			body: Option<T>
		) :
			HttpErr<T>(ex, status, body)

		class RedirectErr<T>(ex: Throwable, status: Int, body: Option<T>) :
			HttpErr<T>(ex, status, body)

		class ServerErr<T>(ex: Throwable, status: Int, body: Option<T>) :
			HttpErr<T>(ex, status, body)

		class GenericHttpErr<T>(ex: Throwable, status: Int, body: Option<T>) :
			HttpErr<T>(ex, status, body)

		class ConnectionErr<T>(ex: Throwable, status: Int, body: Option<T>) :
			HttpErr<T>(ex, status, body)

		class HttpJsonParseErr<T>(ex: Throwable, status: Int, body: Option<T>) :
			HttpErr<T>(ex, status, body)
	}

}

fun <T> ResponseException.toHttpError(body: Option<T>) =
	when (this) {
		is ServerResponseException -> Err.HttpErr.ServerErr(
			this, this.response.status.value, body
		)

		is ClientRequestException -> Err.HttpErr.ClientErr(
			this, this.response.status.value, this.response.headers.toMap(), body
		)

		is RedirectResponseException -> Err.HttpErr.RedirectErr(
			this,
			this.response.status.value, body
		)

		else -> Err.HttpErr.GenericHttpErr(
			this, this.response.status.value, body
		)
	}
