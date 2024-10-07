package modules.exampleModule.features.todo

import arrow.core.Either
import data.types.Err
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.url
import io.ktor.http.ContentType
import io.ktor.http.contentType
import modules.common.models.ErrResponse
import modules.exampleModule.features.todo.dto.TodoResponse
import modules.exampleModule.routes.ExampleRoutes
import utils.result

interface TodoRepository {
	suspend fun getTodos(): Either<Err.HttpErr<ErrResponse>, List<TodoResponse>>
}

class TodoRepositoryImpl(
	private val httpClient: HttpClient
) : TodoRepository {

	override suspend fun getTodos(): Either<Err.HttpErr<ErrResponse>, List<TodoResponse>> = result {
		this.httpClient.get {
			url(ExampleRoutes.Todo.GET_TODO_ITEMS)
			contentType(ContentType.Application.Json)
		}.body()
	}

}