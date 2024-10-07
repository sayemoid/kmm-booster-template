package modules.common.features.auth.models

import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserBriefResponse(
	val id: Long = 0,

	@SerialName("created_at")
	val createdAt: Instant,

	@SerialName("updated_at")
	val updatedAt: Instant? = null,

	val name: String,

	val username: String,

	var phone: String? = null,

	var email: String? = null,

	val gender: Genders,

	val roles: List<Long>,

	val avatar: String? = null,

	val label: String,
)


enum class Genders(@SerialName("label") val label: String) {
	MALE("Male"),
	FEMALE("Female"),
	OTHER("Other"),
	NOT_SPECIFIED("Not Specified")
}

