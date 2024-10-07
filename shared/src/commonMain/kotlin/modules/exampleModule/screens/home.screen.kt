package modules.exampleModule.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.koin.koinScreenModel
import modules.common.features.auth.AuthVM
import modules.common.features.auth.authentication
import modules.common.views.components.LazyColumnWithLoadingForList
import modules.common.views.dimensions.Paddings
import modules.common.views.screens.AppScreen
import modules.exampleModule.features.todo.TodoCategoryView
import modules.exampleModule.features.todo.TodoItemView
import modules.exampleModule.features.todo.TodoVM
import modules.exampleModule.screens.layouts.ExampleAppLayout
import utils.show

object MainScreen : AppScreen {

	@Composable
	override fun Content() {
		val todoVM = koinScreenModel<TodoVM>()
		val authVM = koinScreenModel<AuthVM>()

		ExampleAppLayout { paddingValues, snackbar ->

			LaunchedEffect(Unit) {
				todoVM.fetchTodos()
			}

			val auth = authentication(authVM)

			Column(
				modifier = Modifier.padding(paddingValues)
					.padding(Paddings.Screen.horizontal),
			) {
				TodoCategoryView()

				LazyColumnWithLoadingForList(
					remoteData = todoVM.todos.collectAsState().value,
					itemView = {
						TodoItemView(
							todo = it,
							itemClick = {
								auth.require {
									snackbar.show("Hi, you clicked #${it.id}")
								}
							}
						)
					}
				)
			}
		}
	}

}

