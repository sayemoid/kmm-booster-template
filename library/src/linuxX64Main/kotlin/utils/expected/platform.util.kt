package utils.expected

import kotlin.random.Random


actual fun uuid(): String = generateUuid()

actual val isDebug = true

actual fun platform(): Platforms = Platforms.JVM

fun generateUuid(): String {
	// Generate random 128-bit value by combining two random 64-bit long values
	val mostSigBits = Random.nextLong()
	val leastSigBits = Random.nextLong()

	return "${digits(mostSigBits shr 32, 8)}-" +
			"${digits(mostSigBits shr 16, 4)}-" +
			"${digits(mostSigBits, 4)}-" +
			"${digits(leastSigBits shr 48, 4)}-" +
			digits(leastSigBits, 12)
}

// Helper function to format the UUID parts
private fun digits(value: Long, digits: Int): String {
	val hexValue = value.toString(16)
	return hexValue.padStart(digits, '0')
}