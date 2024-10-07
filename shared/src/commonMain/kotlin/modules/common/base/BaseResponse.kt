package modules.common.base

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
open class BaseReq {
	val id: Long? = null
}

@Serializable
open class BaseResponse {
	val id: Long = 0

	@SerialName("created_at")
	lateinit var createdAt: String

	@SerialName("updated_at")
	var updatedAt: String? = null
}
