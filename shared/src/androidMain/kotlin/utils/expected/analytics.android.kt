package utils.expected

import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.analytics.logEvent
import com.google.firebase.ktx.Firebase
import utils.Tag

actual object Analytics {
	private val fa = Firebase.analytics

	actual fun log(event: Tag.Event, data: Data) {
		fa.logEvent(event.name) {
			data.id?.let { param(FirebaseAnalytics.Param.ITEM_ID, it) }
			param(FirebaseAnalytics.Param.ITEM_NAME, data.name)
			param(FirebaseAnalytics.Param.CONTENT_TYPE, data.contentType.name)
		}
	}
}

