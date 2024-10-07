package modules.common.features.notifications.models

import kotlinx.serialization.SerialName

data class NChannel(
	val id: Long,

	@SerialName("channel_id")
	val channelId: String,

	@SerialName("name")
	val name: String,

	@SerialName("description")
	val description: String,

	@SerialName("priority")
	val priority: Int,

	@SerialName("topics")
	val topics: Set<String>
)

data class Notification(
	val title: String,
	val body: String,
	val image: String?
)

data class NotificationData(
	val notificationId: Int,
	val referenceId: Long?,
	val referenceId2: Long?,
	val navigable: Boolean,
	val intentAction: String?,
	val channelId: String,
)

enum class NDataKeys(
	val key: String
) {
	NOTIFICATION_ID("notification_id"),
	REFERENCE_ID("reference_id"),
	REFERENCE_ID_2("reference_id_2"),
	NAVIGABLE("navigable"),
	CHANNEL_ID("channel_id"),
	INTENT_ACTION("intent_action"),
	VAL_OPEN_POLL_DETAILS_PAGE("open_poll_details_page"),
	VAL_OPEN_KOLLYAN_MAIN("open_kollyan_main"),
}