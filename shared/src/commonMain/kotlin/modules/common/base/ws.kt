package modules.common.base

interface WSMessage {
	val username: String?
	val message: String
	val data: Map<String, Any>
}