package modules.exampleModule.features.todo

import arrow.core.toOption
import data.responses.toMessage
import data.types.RemoteListData
import modules.exampleModule.features.todo.dto.TodoResponse

interface TodoService {
	suspend fun getTodos(): RemoteListData<TodoResponse>
}

class TodoServiceImpl(
	private val todoRepository: TodoRepository
) : TodoService {

	override suspend fun getTodos(): RemoteListData<TodoResponse> =
		this.todoRepository.getTodos()
			.mapLeft { it.toMessage() }
			.toOption()

}