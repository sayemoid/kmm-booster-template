package utils

fun ByteArray.resizeTo(newSize: Int): ByteArray {
	if (newSize > this.size) return this
	val newByteArray = ByteArray(newSize)
	for (i in 0 until newSize) {
		newByteArray[i] = this[i]
	}
	return newByteArray
}