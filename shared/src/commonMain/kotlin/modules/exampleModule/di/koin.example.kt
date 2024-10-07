package modules.exampleModule.di

import modules.common.di.commonModule
import modules.exampleModule.features.todo.TodoRepository
import modules.exampleModule.features.todo.TodoRepositoryImpl
import modules.exampleModule.features.todo.TodoService
import modules.exampleModule.features.todo.TodoServiceImpl
import modules.exampleModule.features.todo.TodoVM
import org.koin.core.context.startKoin
import org.koin.dsl.module

val exampleModule = module {

	/*
	To Do
	 */
	single<TodoRepository> { TodoRepositoryImpl(get()) }
	single<TodoService> { TodoServiceImpl(get()) }

	factory<TodoVM> { TodoVM(get()) }

}

val koinExampleModules = commonModule + exampleModule

fun initKoin() = startKoin { modules(koinExampleModules) }