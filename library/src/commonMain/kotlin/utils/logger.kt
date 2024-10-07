package utils

import co.touchlab.kermit.Logger
import utils.expected.isDebug

sealed interface Tag {

	sealed interface View : Tag {
		data object SocialButton : View
		data object Pager : View
		data object Quiz : View
		data object Pref : View
		data object DatePicker : View
	}

	sealed interface Auth : Tag {
		data object LoadAuthFromStorage : Auth
		data object RefreshToken : Auth
		data object SignUp : Auth
	}

	sealed interface Network : Tag {
		data object PushMessage : Network
		data object Call : Network
		data object JsonParsing : Network

		data object WebSocket : Network {
			override fun toString() = "WebSocket"
		}
	}

	sealed interface Notification : Tag {
		data object FirebaseServiceToken : Notification
	}

	data object General : Tag
	data object Locale : Tag
	data object Crash : Tag
	data class Service(val name: String) : Tag {
		override fun toString() = name
	}

	data object BroadcastReceiver : Tag

	sealed class Event(val name: String) : Tag {
		data object Select : Event("select_item")
		data object Completed : Event("completed")
		data object Open : Event("open")
	}

}

fun logD(tag: Tag, msg: String, throwable: Throwable? = null) {
	if (isDebug) Logger.d(msg, throwable, tag::class.simpleName ?: "")
}

fun logE(tag: Tag, msg: String, throwable: Throwable? = null) {
	if (isDebug) Logger.e(msg, throwable, tag::class.simpleName ?: "")
}

fun logI(tag: Tag, msg: String) = Logger.e(msg, null, tag::class.simpleName ?: "")
fun logV(tag: Tag, msg: String) = Logger.v(msg, null, tag::class.simpleName ?: "")
fun logW(tag: Tag, msg: String, throwable: Throwable? = null) =
	Logger.w(msg, throwable, tag::class.simpleName ?: "")

fun logWTF(tag: Tag, msg: String, throwable: Throwable? = null) =
	Logger.a(msg, throwable, tag::class.simpleName ?: "")