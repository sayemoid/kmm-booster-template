package configs

import arrow.core.Option
import arrow.core.none
import arrow.core.toOption
import kotlinx.coroutines.flow.Flow
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.KSerializer
import modules.common.AuthCredentials
import org.hildan.krossbow.stomp.StompClient
import org.hildan.krossbow.stomp.StompSession
import org.hildan.krossbow.stomp.conversions.kxserialization.StompSessionWithKxSerialization
import org.hildan.krossbow.stomp.conversions.kxserialization.json.withJsonConversions
import org.hildan.krossbow.stomp.headers.StompSendHeaders
import org.hildan.krossbow.stomp.headers.StompSubscribeHeaders
import org.hildan.krossbow.stomp.subscribeText
import org.hildan.krossbow.stomp.use
import org.hildan.krossbow.websocket.ktor.KtorWebSocketClient
import utils.Tag
import utils.logD

fun wsClient(cred: AuthCredentials) = KtorWebSocketClient(ktorClient(cred))
fun stompClient(cred: AuthCredentials) = StompClient(wsClient(cred))

suspend fun socket(cred: AuthCredentials, url: String): StompSession =
	stompClient(cred).connect(url = url)

suspend fun connectWithSerialization(
	cred: AuthCredentials,
	url: String
): StompSessionWithKxSerialization =
	stompClient(cred).connect(url).withJsonConversions()

suspend fun StompSession.subscribeTopic(topic: String): Flow<String> =
	this.subscribeText(destination = "/topic${topic}")

suspend fun <T : Any> StompSessionWithKxSerialization.subscribeTopic(
	topic: String, deserializer: KSerializer<T>
): Flow<T> = this.subscribe(
	headers = StompSubscribeHeaders(
		destination = "/topic${topic}"
	), deserializer
)

suspend fun <T : Any> StompSessionWithKxSerialization.sub(
	topic: String,
	serializer: DeserializationStrategy<T>
): Flow<T> =
	this.subscribe(
		StompSubscribeHeaders(
			destination = topic
		),
		serializer
	)

suspend fun <T> StompSessionWithKxSerialization.push(
	destination: String,
	jsonObject: T,
	serializer: KSerializer<T>
) {
	this.use { s ->
		s.convertAndSend(
			headers = StompSendHeaders(destination = destination),
			body = jsonObject,
			serializer = serializer
		)
	}
}

class WSConnection(private val cred: AuthCredentials, private val socketURI: String) {
	private var session: Option<StompSessionWithKxSerialization> = none()
	private var isConnected = false

	suspend fun init(
		url: String = socketURI,
		block: suspend (session: StompSessionWithKxSerialization) -> Unit
	) {
		try {
			this.session.fold(
				{
					logD(Tag.Network.WebSocket, "Session doesn't exist, connecting..")
					val newSession = socket(cred = cred, url = url).withJsonConversions()
					this.session = newSession.toOption()
					this.isConnected = true
					logD(Tag.Network.WebSocket, "Connection established. Executing block().")
					this.init(url) { block(newSession) }
				}
			) {
				if (this.isConnected) {
					logD(
						Tag.Network.WebSocket,
						"Session already exists and connected. Continuing execution."
					)
					block(it)
				} else {
					logD(
						Tag.Network.WebSocket,
						"Session exist, but disconnected. " +
								"Proceeding to disconnect and creating new connection.."
					)
					it.disconnect()
					val newSession = socket(cred = cred, url = url).withJsonConversions()
					this.session = newSession.toOption()
					this.isConnected = true
					logD(Tag.Network.WebSocket, "Success. Executing block().")
					this.init(url) { block(newSession) }
				}
			}
			this.isConnected = true
		} catch (e: Exception) {
			logD(Tag.Network.WebSocket, "Error Occurred. Disconnecting session..")
			logD(Tag.Network.WebSocket, e.toString())
			this.isConnected = false
			if (this.session.isSome()) {
				session = none()
				logD(Tag.Network.WebSocket, "clearing session because of error.")
			}
		}
	}

	suspend fun close() = session.onSome {
		if (this.isConnected) it.disconnect()
		session = none()
	}
}