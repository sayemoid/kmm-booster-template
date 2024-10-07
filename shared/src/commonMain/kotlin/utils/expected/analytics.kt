package utils.expected

import utils.Tag

expect object Analytics {
	fun log(event: Tag.Event, data: Data)
}

data class Data(
	val name: String,
	val id: String? = null,
	val contentType: CType = CType.Button,
	val extra: Map<String, String> = mapOf()
)

sealed class CType(val name: String) {
	data object Button : CType("Button")
	data object Label : CType("Label")
	data object Questionnaire : CType("Questionnaire")
	data object Screen : CType("Screen")
	data object Page : CType("Page")
	data object Pref : CType("Pref")
	data object Registration : CType("Registration")
}

fun anal(data: Data, event: Tag.Event = Tag.Event.Select) = Analytics.log(event, data)
