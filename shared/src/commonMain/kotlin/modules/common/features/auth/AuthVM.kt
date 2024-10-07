package modules.common.features.auth

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import arrow.core.none
import arrow.core.right
import arrow.core.toOption
import cafe.adriel.voyager.core.model.StateScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import configs.authKey
import configs.cleanupAuth
import data.responses.toMessage
import data.types.RemoteData
import data.types.SignUpStates
import data.types.State
import io.ktor.client.HttpClient
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import modules.common.features.auth.models.Auth
import modules.common.features.auth.models.FirebaseTokenReq
import modules.common.features.auth.models.SignUpReq
import modules.common.features.auth.models.UsernameAvailableResponse
import modules.common.features.auth.models.VerificationData
import modules.common.features.preferences.PrefKeys.appIdentifierKey
import modules.common.features.preferences.PrefKeys.firebaseTokenKey
import utils.Tag
import utils.expected.uuid
import utils.logD

class AuthVM(
	private val authService: AuthService,
	private val dataStore: DataStore<Preferences>,
	private val httpClient: HttpClient
) : StateScreenModel<State>(State.Init) {

	private val _authTrigger = MutableStateFlow(Pair(false) {})
	val authTrigger = _authTrigger.asStateFlow()

	private val _signupState = MutableStateFlow<SignUpStates>(SignUpStates.Init)
	val signUpState = _signupState.asStateFlow()

	private val _usernameResponse =
		MutableStateFlow<RemoteData<UsernameAvailableResponse>>(none())
	val usernameResponse = _usernameResponse.asStateFlow()

	fun getAuth(): Flow<RemoteData<Auth>> =
		dataStore.data.map { pref ->
			pref[authKey]?.let {
				logD(Tag.Auth.LoadAuthFromStorage, it)
				Json.decodeFromString<Auth>(it).right()
			}.toOption()
		}

	fun logout() = screenModelScope.launch {
		dataStore.edit { it.remove(authKey) }
		httpClient.cleanupAuth()
		trigger(false)
		mutableState.value = State.Init
	}

	fun login(username: String, password: String, block: () -> Unit = {}) =
		screenModelScope.launch {
			mutableState.value = State.Loading
			val result = authService.login(username, password)
				.onRight { auth ->
					onLoginSuccess(auth, block)
				}
			mutableState.value = State.Result(result.mapLeft { it.toMessage() })
		}

	fun signup(token: String, signUpReq: SignUpReq, block: () -> Unit = {}) =
		screenModelScope.launch {
			_signupState.value = SignUpStates.Loading
			delay(1000)
			val result = authService.signup(token, signUpReq)
				.onRight { auth ->
					onLoginSuccess(auth, block)
				}
			_signupState.value = SignUpStates.Result(result.mapLeft { it.toMessage() })
		}

	private suspend fun onLoginSuccess(auth: Auth, block: () -> Unit = {}) {
		trigger(false)
		dataStore.updateData {
			val pref = it.toMutablePreferences()
			pref[authKey] = Json.encodeToString(auth)
			pref
		}
		httpClient.cleanupAuth(authService.refreshToken(auth.refreshToken))
		/*
		 execute a code-block after login succeeded
		 typically when user tries to access something without login, we prompt them to authenticate
		 then execute automatically what user originally intended to do
		 */
		block()
		// register firebase token
		dataStore.data.collect { pref ->
			pref[firebaseTokenKey].toOption().onSome { token ->
				logD(Tag.Notification.FirebaseServiceToken, "Loaded From Datastore: $token")
				getAppIdentifier { identifier ->
					authService.registerFirebaseToken(
						FirebaseTokenReq(
							auth.id, token, identifier
						)
					)
				}
			}
		}
	}

	private suspend fun getAppIdentifier(fn: suspend (identifier: String) -> Unit) {
		dataStore.data.collect { pref ->
			val identifier = pref[appIdentifierKey].toOption().fold(
				{
					val uuid = uuid()
					dataStore.updateData { p ->
						val mp = p.toMutablePreferences()
						mp[appIdentifierKey] = uuid
						mp
					}
					uuid
				},
				{ it }
			)
			fn(identifier)
		}
	}


	fun checkUsername(username: String) = screenModelScope.launch {
		_usernameResponse.value = authService.checkUsername(username)
			.mapLeft { it.toMessage() }
			.toOption()
	}

	fun trigger(trigger: Boolean = true, block: () -> Unit = {}) {
		_authTrigger.value = Pair(trigger, block)
	}

	fun requestVerification(verificationData: VerificationData) = screenModelScope.launch {
		_signupState.value = SignUpStates.Loading
		val result = authService.requestOTP(verificationData)
			.mapLeft { it.toMessage() }
		_signupState.value = SignUpStates.Acknowledgement(result)
	}

	fun resetSignupState() {
		_signupState.value = SignUpStates.Init
	}
}