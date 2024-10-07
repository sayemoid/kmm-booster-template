package utils.expected

import android.content.Context
import android.widget.Toast
import utils.Tag
import utils.expected.Duration.Long.millisFromDuration
import utils.logW

actual class Toasty private constructor() {

	actual companion object {
		private lateinit var toast: Toast
		actual fun with(context: Any, resId: Int, duration: Duration): Companion {
			toast = Toast.makeText(
				context as Context, resId, when (millisFromDuration(duration)) {
					in 500..1000 -> Toast.LENGTH_SHORT
					else -> Toast.LENGTH_LONG
				}
			)
			return this
		}

		actual fun show() = if (this::toast.isInitialized)
			this.toast.show()
		else {
			logW(
				Tag.Crash,
				"You should call Toast() with constructor parameters, otherwise it won't work!"
			)
		}
	}


}
