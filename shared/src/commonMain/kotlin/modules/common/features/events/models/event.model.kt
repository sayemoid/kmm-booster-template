package modules.common.features.events.models

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TaskEventReq(

	@SerialName("start_at")
	val startAt: Instant,

	@SerialName("end_at")
	val endAt: Instant?

)

@Serializable
data class EventBrief(
	val id: Long,

	@SerialName("created_at")
	val createdAt: Instant,

	@SerialName("updated_at")
	val updatedAt: Instant? = null,

	val title: String,

	@SerialName("ref_id")
	val refId: Long?,

	val image: String?,

	val type: EventTypes,

	val active: Boolean,

	@SerialName("start_at")
	val startAt: Instant,

	@SerialName("end_at")
	val endAt: Instant?,

	val repetitive: Boolean,

	@SerialName("repeat_interval")
	val repeatInterval: Long,

	@SerialName("repeat_count")
	val repeatCount: Int,

	@SerialName("user_id")
	val userId: Long
){
	val isExpired = !this.active
			|| this.endAt?.let { it<Clock.System.now() }?: false
}

enum class EventTypes {
	TASK
}