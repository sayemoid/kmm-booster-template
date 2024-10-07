package modules.exampleModule.features.todo

import arrow.core.none
import cafe.adriel.voyager.core.model.ScreenModel
import data.types.RemoteListData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import modules.exampleModule.features.todo.dto.TodoResponse

class TodoVM(
	private val todoService: TodoService
) : ScreenModel {
	private val _todos = MutableStateFlow<RemoteListData<TodoResponse>>(none())
	val todos = _todos.asStateFlow()


	suspend fun fetchTodos() {
		this._todos.value = this.todoService.getTodos()
	}

}