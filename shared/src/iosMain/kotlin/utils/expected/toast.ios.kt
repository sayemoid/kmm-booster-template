package utils.expected

actual class Toasty {
	actual companion object {
		actual fun show() = Unit
		actual fun with(
			context: Any,
			resId: Int,
			duration: Duration
		): Companion {
			TODO("Not yet implemented")
		}
	}
}