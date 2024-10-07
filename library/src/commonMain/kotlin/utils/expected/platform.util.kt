package utils.expected

expect fun uuid(): String

expect val isDebug: Boolean

enum class Platforms{
	ANDROID, IOS, JVM;
}

expect fun platform(): Platforms
