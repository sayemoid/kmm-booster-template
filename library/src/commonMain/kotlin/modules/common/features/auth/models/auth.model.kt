package modules.common.features.auth.models

import data.types.CountryCodes
import io.ktor.http.HttpStatusCode
import kotlinx.datetime.Instant
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonNames
import modules.common.models.ErrResponse
import kotlin.time.Duration.Companion.milliseconds

@Serializable
@OptIn(ExperimentalSerializationApi::class)
data class Auth constructor(
	@JsonNames(names = ["access_token"])
	val accessToken: String,

	@JsonNames(names = ["token_type"])
	val tokenType: String,

	@JsonNames(names = ["refresh_token"])
	val refreshToken: String,

	@JsonNames(names = ["expires_in"])
	val expiresIn: Long,

	val scope: String,
	val phone: String? = null,
	val name: String,
	val id: Long,
	val email: String? = null,
	val username: String,
	val jti: String
)

@Serializable
data class AuthErr(
	@SerialName("error")
	val errType: String,
	@SerialName("error_description")
	val description: String
) {
	enum class AuthErrTypes(val value: String) {
		INVALID_TOKEN("invalid_token")
	}

	override fun toString(): String {
		return "$errType : $description"
	}

	fun toErrorResponse() = ErrResponse(HttpStatusCode.Unauthorized.value, errType, description)
}

@Serializable
data class VerificationResponse(
	@SerialName("identity")
	val identity: String,
	@SerialName("token_valid_until")
	val tokenValidUntil: Instant,
	@SerialName("token_validity_millis")
	val tokenValidity: Long,
	@SerialName("reg_method")
	val regMethod: RegMethod
) {
	val validity = tokenValidity.milliseconds
}

enum class RegMethod {
	PHONE, EMAIL
}

@Serializable
data class UsernameAvailableResponse(
	val available: Boolean,
	val reason: String
)


@Serializable
data class SignUpReq(
	val name: String,
	val gender: String,
	val email: String? = null,
	val username: String,
	val password: String,
	val phone: String? = null,
	val role: String,
)

@Serializable
data class FirebaseToken(
	val id: Long,

	@SerialName("created_at")
	var createdAt: Instant,

	@SerialName("updated_at")
	var updatedAt: Instant? = null,

	@SerialName("user_id")
	val userId: Long,

	@SerialName("user_token")
	val userToken: String
)

@Serializable
data class FirebaseTokenReq(
	@SerialName("user_id")
	val userId: Long,
	@SerialName("user_token")
	val userToken: String,
	@SerialName("app_identifier")
	val appIdentifier: String
)

sealed interface VerificationData {
	data class Email(val email: String) : VerificationData
	data class Phone(val countryCodes: CountryCodes, val phone: String) : VerificationData
}