package data

import kotlinx.serialization.Serializable
import utils.toParamString

@Serializable
data class Page<T>(
	val content: List<T>,
	val totalElements: Long,
	val last: Boolean,
	val totalPages: Int,
	val sort: Sort,
	val numberOfElements: Long,
	val first: Boolean,
	val size: Int,
	val number: Long,
	val empty: Boolean,
) {
	companion object {
		fun <T> of(content: List<T>) =
			Page(
				content = content,
				totalElements = 0,
				last = true,
				totalPages = 0,
				sort = Sort(false, true, false),
				numberOfElements = 0L,
				first = true,
				size = 10,
				number = 0,
				empty = false
			)
	}
}

@Serializable
data class Sort(
	val sorted: Boolean,
	val unsorted: Boolean,
	val empty: Boolean,
)

data class PageableParams(
	val query: String? = null,
	val page: Long = 0,
	val size: Int = 10,
	val sortBy: SortByFields = SortByFields.ID,
	val direction: SortDirections = SortDirections.DESC
){
	fun toParamString() = mapOf(
		"q" to if (this.query == null) "" else this.query.toString(),
		"page" to this.page,
		"size" to this.size,
		"sort_by" to this.sortBy.name,
		"sort_direction" to this.direction.name
	).toParamString()
}

enum class SortByFields {
	ID, CREATED_AT, SERIAL;
}

enum class SortDirections {
	ASC, DESC
}

