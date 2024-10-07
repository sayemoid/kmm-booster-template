package modules.exampleModule.features.todo.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TodoResponse(
	val id: Long,

	@SerialName("userId")
	val userId: Long,

	val title: String,

	val completed: Boolean
)