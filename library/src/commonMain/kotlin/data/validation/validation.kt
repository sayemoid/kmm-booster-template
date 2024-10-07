package data.validation

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import data.responses.ErrMessage
import data.responses.toMessage
import data.types.CountryCodes
import data.types.Err

data class ValidatedText(val text: String, val valid: Boolean)

@Deprecated("Should be replaced with ValidationV2<T>")
interface Validation<T> {
	fun apply(data: T): Either<ErrMessage, T>
}

interface ValidationV2<T> {
	fun apply(data: T): Either<Err.ValidationErr, T>

}

fun <T> genericValidation(
	message: String? = null,
	instruction: String = "",
	valid: (data: T) -> Boolean
): ValidationV2<T> =
	object : ValidationV2<T> {

		override fun apply(data: T): Either<Err.ValidationErr, T> =
			if (valid(data)) {
				data.right()
			} else {
				Err.ValidationErr
					.Generic(RuntimeException(message ?: "$data is invalid"), instruction)
					.left()
			}

	}

object OTPValidation : ValidationV2<String> {
	override fun apply(data: String): Either<Err.ValidationErr, String> =
		if (data.length == 6) data.right()
		else Err.ValidationErr
			.TextValidationErr(
				RuntimeException("OTP should be 6 characters."),
				"We've sent an OTP in your phone/email. Please enter it here."
			)
			.left()
}

class PhoneValidation(private val countryCode: CountryCodes) : ValidationV2<String> {
	override fun apply(data: String): Either<Err.ValidationErr, String> {
		val phoneNumber = countryCode.dialingCode + data
		return if (
			phoneNumber.isValidPhoneNumber()
			&& data.length in countryCode.phoneLength
		) {
			phoneNumber.right()
		} else {
			// Check if it's because of leading 0, then correct it.
			val message = if (data.startsWith("0"))
				"Invalid phone number. Please remove leading 0 before number."
			else "Invalid phone number."

			val (isRange, length) = if (countryCode.phoneLength.first == countryCode.phoneLength.last) {
				Pair(true, countryCode.phoneLength.first.toString())
			} else Pair(false, countryCode.phoneLength.last.toString())
			Err.ValidationErr
				.PhoneValidationErr(
					RuntimeException(message),
					"Length should be ${if (isRange) "in range" else ""} $length"
				)
				.left()
		}
	}

	private fun String.isValidPhoneNumber() = Regex(
		"\\+(9[976]\\d|8[987530]\\d|6[987]\\d|5[90]\\d|42\\d|3[875]\\d|\n" +
				"2[98654321]\\d|9[8543210]|8[6421]|6[6543210]|5[87654321]|\n" +
				"4[987654310]|3[9643210]|2[70]|7|1)\\d{1,14}\$"
	).matches(this)
}

class EmailValidation : ValidationV2<String> {
	override fun apply(data: String): Either<Err.ValidationErr, String> =
		if ("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,}$".toRegex().matches(data))
			data.right()
		else Err.ValidationErr.EmailValidationErr(
			RuntimeException("Invalid email address"),
			"Please input a valid email address"
		).left()
}

@Deprecated("Uses deprecated interface Validation<String>, which should be replaced with ValidationV2<String>")
class GenericTextValidation(
	private val length: Int,
	private val message: String = "Text must be at least 6 digit"
) : Validation<String> {

	override fun apply(data: String): Either<ErrMessage, String> {
		return if (data.length < length)
			Err.ValidationErr
				.TextValidationErr(RuntimeException(message), "")
				.toMessage().left()
		else data.right()
	}
}

class FieldIsRequired(
	private val message: String = "Field required"
) : Validation<String> {

	override fun apply(data: String): Either<ErrMessage, String> {
		return if (data.isBlank())
			Err.ValidationErr
				.TextValidationErr(RuntimeException(message), "")
				.toMessage().left()
		else data.right()
	}

}
