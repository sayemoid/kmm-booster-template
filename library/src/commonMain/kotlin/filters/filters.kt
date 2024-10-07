package filters

import io.ktor.client.statement.HttpResponse

interface HttpFilter {
	suspend fun apply(response: HttpResponse): HttpResponse
}
