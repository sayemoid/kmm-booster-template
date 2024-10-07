package utils.expected

sealed interface Duration {
	data object Short : Duration
	data object Medium : Duration
	data object Long : Duration

	fun millisFromDuration(duration: Duration): Int =
		when (duration) {
			Short -> 500
			Medium -> 1000
			Long -> 2000
		}
}

expect class Toasty {
	companion object {
		fun with(context: Any, resId: Int, duration: Duration): Companion
		fun show()

	}
}