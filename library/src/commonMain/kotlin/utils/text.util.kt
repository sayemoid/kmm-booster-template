package utils

fun htmlToPlainText(html: String): String {
	var text = ""
	var inTag = false

	for (char in html) {
		if (char == '<') {
			inTag = true
		} else if (char == '>') {
			inTag = false
		} else if (!inTag) {
			text += char
		}
	}
	return text.trim()
}
