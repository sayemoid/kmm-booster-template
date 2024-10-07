package utils.expected

import utils.Tag

actual object Analytics {
	actual fun log(event: Tag.Event, data: Data) {}
}

