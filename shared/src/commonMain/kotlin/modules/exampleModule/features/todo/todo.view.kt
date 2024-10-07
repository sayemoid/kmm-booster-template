package modules.exampleModule.features.todo

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Alarm
import androidx.compose.material.icons.outlined.Cancel
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import modules.common.views.components.LazyColumnWithLoadingForList
import modules.common.views.dimensions.Paddings
import modules.exampleModule.features.todo.dto.TodoResponse

@Composable
fun TodoCategoryView() {
	Row(
		modifier = Modifier.wrapContentHeight()
	) {
		Card(
			modifier = Modifier.wrapContentSize().weight(1F)
				.padding(5.dp),
			colors = CardDefaults.cardColors(
				containerColor = MaterialTheme.colorScheme.primaryContainer,
				contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
			)
		) {
			Column(
				modifier = Modifier.padding(Paddings.Card.horizontal, Paddings.Card.vertical)
			) {
				Icon(imageVector = Icons.Outlined.Alarm, contentDescription = "")
				HorizontalDivider(thickness = 1.dp, modifier = Modifier.padding(vertical = 10.dp))
				Text("Reminders", fontSize = 12.sp)
				HorizontalDivider(thickness = 1.dp, modifier = Modifier.padding(vertical = 10.dp))
				Text("0")
			}
		}
		Card(
			modifier = Modifier.wrapContentSize().weight(1F)
				.padding(5.dp),
			colors = CardDefaults.cardColors(
				containerColor = MaterialTheme.colorScheme.secondaryContainer,
				contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
			)
		) {
			Column(
				modifier = Modifier.padding(Paddings.Card.horizontal, Paddings.Card.vertical)
			) {
				Icon(imageVector = Icons.Outlined.Alarm, contentDescription = "")
				HorizontalDivider(thickness = 1.dp, modifier = Modifier.padding(vertical = 10.dp))
				Text("Tasks", fontSize = 12.sp)
				HorizontalDivider(thickness = 1.dp, modifier = Modifier.padding(vertical = 10.dp))
				Text("5")
			}
		}
		Card(
			modifier = Modifier.wrapContentSize().weight(1F)
				.padding(5.dp),
			colors = CardDefaults.cardColors(
				containerColor = MaterialTheme.colorScheme.secondaryContainer,
				contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
			)
		) {
			Column(
				modifier = Modifier.padding(Paddings.Card.horizontal, Paddings.Card.vertical)
			) {
				Icon(imageVector = Icons.Outlined.Alarm, contentDescription = "")
				HorizontalDivider(thickness = 1.dp, modifier = Modifier.padding(vertical = 10.dp))
				Text("Groceries", fontSize = 12.sp)
				HorizontalDivider(thickness = 1.dp, modifier = Modifier.padding(vertical = 10.dp))
				Text("30")
			}
		}
	}
}

@Composable
fun TodoItemView(
	todo: TodoResponse,
	itemClick: (item: TodoResponse) -> Unit = {}
) {

	var done by remember { mutableStateOf(todo.completed) }

	Row(
		modifier = Modifier.fillMaxWidth()
			.clickable { itemClick(todo) },
		verticalAlignment = Alignment.CenterVertically,
	) {
		Text(
			modifier = Modifier.weight(1F),
			text = "#${todo.id}",
			textAlign = TextAlign.Center
		)
		VerticalDivider(thickness = 2.dp, modifier = Modifier.width(10.dp))
		Text(
			modifier = Modifier.weight(6F),
			text = todo.title.take(30),
			textAlign = TextAlign.Left
		)
		VerticalDivider(thickness = 2.dp)

		Icon(
			modifier = Modifier.weight(1F).wrapContentSize()
				.padding(Paddings.Internal.innerPadding)
				.clickable {
					done = !done
				},
			tint = if (!done) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error,
			imageVector = if (!done) Icons.Outlined.CheckCircle else Icons.Outlined.Cancel,
			contentDescription = "Complete"
		)
	}
}

@Composable
fun TodoListView(
	modifier: Modifier = Modifier,
	todoVM: TodoVM,
	itemClick: (item: TodoResponse) -> Unit = {}
) {
	LaunchedEffect(Unit) {
		todoVM.fetchTodos()
	}

	LazyColumnWithLoadingForList(
		modifier = Modifier.then(modifier)
			.animateContentSize(),
		remoteData = todoVM.todos.collectAsState().value,
		itemView = { todo ->

			var done by remember { mutableStateOf(todo.completed) }

			Row(
				modifier = Modifier.fillMaxWidth()
					.clickable { itemClick(todo) },
				verticalAlignment = Alignment.CenterVertically,
			) {
				Text(
					modifier = Modifier.weight(1F),
					text = "#${todo.id}",
					textAlign = TextAlign.Center
				)
				VerticalDivider(thickness = 2.dp, modifier = Modifier.width(10.dp))
				Text(
					modifier = Modifier.weight(6F),
					text = todo.title.take(30),
					textAlign = TextAlign.Left
				)
				VerticalDivider(thickness = 2.dp)

				Icon(
					modifier = Modifier.weight(1F).wrapContentSize()
						.padding(Paddings.Internal.innerPadding)
						.clickable {
							done = !done
						},
					tint = if (!done) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error,
					imageVector = if (!done) Icons.Outlined.CheckCircle else Icons.Outlined.Cancel,
					contentDescription = "Complete"
				)
			}
		}
	)

}