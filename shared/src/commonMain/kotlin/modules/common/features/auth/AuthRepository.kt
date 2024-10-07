package modules.common.features.auth

import arrow.core.Either
import configs.Credentials
import data.types.Err
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.forms.submitForm
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.request.url
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.parameters
import modules.common.features.auth.models.Auth
import modules.common.features.auth.models.AuthErr
import modules.common.features.auth.models.FirebaseToken
import modules.common.features.auth.models.FirebaseTokenReq
import modules.common.features.auth.models.SignUpReq
import modules.common.features.auth.models.UsernameAvailableResponse
import modules.common.features.auth.models.VerificationResponse
import modules.common.models.ErrResponse
import modules.common.routes.Routes
import utils.RemoteResult
import utils.result
import utils.resultSingleWithHeaders

interface AuthRepository {
	suspend fun token(username: String, password: String): Either<Err.HttpErr<AuthErr>, Auth>
	suspend fun refreshToken(refreshToken: String): HttpResponse
	suspend fun checkUsername(username: String): Either<Err.HttpErr<ErrResponse>, UsernameAvailableResponse>
	suspend fun requestOTP(phoneOrEmail: String): Either<Err.HttpErr<ErrResponse>, RemoteResult<VerificationResponse>>
	suspend fun signup(token: String, signUpReq: SignUpReq): Either<Err.HttpErr<ErrResponse>, Auth>
	suspend fun registerFirebaseToken(
		tokenReq: FirebaseTokenReq
	): Either<Err.HttpErr<ErrResponse>, FirebaseToken>
}

class AuthRepositoryImpl(
	private val httpClient: HttpClient
) : AuthRepository {

	override suspend fun token(
		username: String,
		password: String
	): Either<Err.HttpErr<AuthErr>, Auth> =
		result {
			httpClient.submitForm(
				url = Routes.GET_TOKEN,
				formParameters = parameters {
					append("username", username.trim())
					append("password", password)
					append("client_id", Credentials.Auth.clientId.get())
					append("client_secret", Credentials.Auth.clientSecret.get())
					append("grant_type", "password")
				}
			).body()
		}

	override suspend fun refreshToken(refreshToken: String): HttpResponse =
		refreshToken(
			client = httpClient,
			clientId = Credentials.Auth.clientId.get(),
			clientSecret = Credentials.Auth.clientSecret.get(),
			refreshToken = refreshToken
		)

	override suspend fun checkUsername(username: String): Either<Err.HttpErr<ErrResponse>, UsernameAvailableResponse> =
		result {
			this.httpClient.get {
				url(Routes.checkUsername(username))
			}.body()
		}

	override suspend fun requestOTP(phoneOrEmail: String): Either<Err.HttpErr<ErrResponse>, RemoteResult<VerificationResponse>> =
		resultSingleWithHeaders {
			this.httpClient.post {
				url(Routes.getOTP(phoneOrEmail))
			}
		}

	override suspend fun signup(
		token: String,
		signUpReq: SignUpReq
	): Either<Err.HttpErr<ErrResponse>, Auth> =
		result {
			this.httpClient.post {
				url(Routes.signup(token))
				contentType(ContentType.Application.Json)
				setBody(signUpReq)
			}.body()
		}

	override suspend fun registerFirebaseToken(
		tokenReq: FirebaseTokenReq
	): Either<Err.HttpErr<ErrResponse>, FirebaseToken> =
		result {
			this.httpClient.post {
				url(Routes.registerFirebaseToken())
				contentType(ContentType.Application.Json)
				setBody(tokenReq)
			}.body()
		}

}

suspend fun refreshToken(
	client: HttpClient,
	clientId: String,
	clientSecret: String,
	refreshToken: String,
	block: HttpRequestBuilder.() -> Unit = {}
) = client.submitForm(
	url = Routes.GET_TOKEN,
	formParameters = parameters {
		append("grant_type", "refresh_token")
		append("client_id", clientId)
		append("client_secret", clientSecret)
		append("refresh_token", refreshToken)
	},
	block = block
)
