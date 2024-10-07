package modules.common.models

import data.responses.ErrActions
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable

@Serializable
data class ErrResponse(
	val code: Int,
	val status: String,
	val message: String
) {
	override fun toString(): String = message
}

@Serializable
data class ErrResponseV2(
	val type: ResponseType,
	val status: HttpStatus,
	val code: Int = status.value,
	val time: Instant = Clock.System.now(),
	val error: ErrData
) {
	override fun toString(): String = error.message
}

@Serializable
data class ErrData(
	val type: String,
	val message: String,
	val status: HttpStatus,
	val description: String = "",
	val actions: Set<ErrActions> = setOf(),
)