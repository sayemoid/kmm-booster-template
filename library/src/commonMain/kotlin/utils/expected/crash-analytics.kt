package utils.expected

expect class CrashAnalytics {
	fun init(dsn: String)

	companion object {
		fun capture(throwable: Throwable)
	}
}