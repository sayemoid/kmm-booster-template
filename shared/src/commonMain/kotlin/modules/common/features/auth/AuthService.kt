package modules.common.features.auth

import arrow.core.Either
import arrow.core.flatMap
import arrow.core.left
import data.types.Err
import data.validation.EmailValidation
import data.validation.PhoneValidation
import io.ktor.client.statement.HttpResponse
import modules.common.features.auth.models.Auth
import modules.common.features.auth.models.FirebaseToken
import modules.common.features.auth.models.FirebaseTokenReq
import modules.common.features.auth.models.SignUpReq
import modules.common.features.auth.models.UsernameAvailableResponse
import modules.common.features.auth.models.VerificationData
import modules.common.features.auth.models.VerificationResponse
import modules.common.models.ErrResponse
import utils.RemoteResult

interface AuthService {
	fun isAuthenticated(): Boolean
	suspend fun login(username: String, password: String): Either<Err, Auth>
	suspend fun refreshToken(refreshToken: String): HttpResponse
	suspend fun checkUsername(username: String): Either<Err, UsernameAvailableResponse>
	suspend fun signup(token: String, signUpReq: SignUpReq): Either<Err.HttpErr<ErrResponse>, Auth>
	suspend fun requestOTP(
		data: VerificationData
	): Either<Err, RemoteResult<VerificationResponse>>

	suspend fun registerFirebaseToken(
		tokenReq: FirebaseTokenReq
	): Either<Err.HttpErr<ErrResponse>, FirebaseToken>
}

class AuthServiceImpl(
	private val authRepository: AuthRepository
) : AuthService {
	override fun isAuthenticated() = false

	override suspend fun login(username: String, password: String): Either<Err, Auth> =
		this.authRepository.token(username, password)

	override suspend fun refreshToken(refreshToken: String): HttpResponse =
		this.authRepository.refreshToken(refreshToken)

	override suspend fun checkUsername(username: String): Either<Err, UsernameAvailableResponse> =
		if (username.length < 5) {
			Err.ValidationErr
				.Generic(RuntimeException("Username not available"), "Enter Username")
				.left()
		} else {
			authRepository.checkUsername(username)
		}

	override suspend fun signup(
		token: String,
		signUpReq: SignUpReq
	): Either<Err.HttpErr<ErrResponse>, Auth> =
		this.authRepository.signup(token, signUpReq)

	override suspend fun requestOTP(
		data: VerificationData
	): Either<Err, RemoteResult<VerificationResponse>> = when (data) {
		is VerificationData.Phone -> PhoneValidation(data.countryCodes).apply(data.phone)
		is VerificationData.Email -> EmailValidation().apply(data.email)
	}.flatMap { authRepository.requestOTP(it) }

	override suspend fun registerFirebaseToken(
		tokenReq: FirebaseTokenReq
	): Either<Err.HttpErr<ErrResponse>, FirebaseToken> =
		this.authRepository.registerFirebaseToken(tokenReq)


}