package utils

import kotlinx.datetime.Instant

fun <T> List<T>.qPop(): List<T> =
	if (this.isEmpty()) this
	else this.subList(1, lastIndex + 1)

fun <T> List<T>.qPush(element: T): List<T> = this + listOf(element)

fun <T> List<T>.qPeek(): T? = this.firstOrNull()

fun Map<String, Any?>.toParamString() =
	if (this.isEmpty()) ""
	else {
		this.map {
			val value = when (val p = it.value) {
				is Instant -> p.toString()
				null -> ""
				else -> p.toString()
			}
			"${it.key}=${value}"
		}.joinToString("&")
	}