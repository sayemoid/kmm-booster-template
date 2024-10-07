package modules.common.routes

import utils.expected.isDebug

val BASE_URL = if (isDebug) {
	"https://dev.example.com"
} else {
	"https://api.example.com"
}

val SOCKET_CONNECT = if (isDebug) {
	"wss://dev.example.com/connect"
} else {
	"wss://api.example.com/connect"
}


object SocketRoutes {
	private const val MESSAGES = "/messages"
	fun queue(sessionId: String, name: String? = null) =
		name?.let { "/user/$sessionId/queue/$name" } ?: "/user/$sessionId/queue/default"

	object V1 {
		/*
		Send/Destinations
		 */
		const val SEND_PING = "$MESSAGES/ping"

		/*
		Topics
		 */
		const val TOPIC_PING = "/ping"

		fun queuePing(sessionId: String) = queue(sessionId, "ping")
	}
}

object Routes {

	private const val API_VERSION = "/api/v1"
	private const val API_VERSION_V2 = "/api/v2"

	// Auth
	val GET_TOKEN = "$BASE_URL/oauth/token"
	fun getOTP(phoneOrEmail: String) =
		"$BASE_URL$API_VERSION/register/verify?identity=$phoneOrEmail"

	fun checkUsername(username: String) =
		"$BASE_URL$API_VERSION/public/register/check-username?username=$username"

	fun signup(token: String) = "$BASE_URL$API_VERSION/register?token=$token"
	fun registerFirebaseToken() = "$BASE_URL$API_VERSION/firebase/token"


}
