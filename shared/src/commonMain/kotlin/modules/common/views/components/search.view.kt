package modules.common.views.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.text.style.TextAlign
import arrow.core.toOption
import data.Page
import data.types.State

@Composable
fun <T> SearchView(
	modifier: Modifier = Modifier,
	title: String = "Search",
	hint: String = "Search",
	state: State,
	requestFocus: Boolean = false,
	hideSearchInput: Boolean = false,
	searchInputColors: TextFieldColors = TextFieldDefaults.colors(),
	onQueryChanged: (query: String, currentPage: Long, pageSize: Int) -> Unit,
	noContentView: (@Composable () -> Unit)? = null,
	content: @Composable (Page<T>) -> Unit,
) {
	var loading by remember { mutableStateOf(false) }
	var query by remember { mutableStateOf("") }
	val focusRequester = remember { FocusRequester() }

	LaunchedEffect(Unit) {
		if (requestFocus) {
			focusRequester.requestFocus()
		}
	}

	var currentPage = 0L
	var pageSize = 10
	Column(
		modifier = modifier
	) {
		if (loading) {
			LinearProgressIndicator(
				modifier = Modifier.fillMaxWidth()
			)
		}
		if (!hideSearchInput) {
			WOutlinedTextFieldV2(
				modifier = Modifier
					.focusRequester(focusRequester)
					.fillMaxWidth(),
				text = query,
				label = hint,
				leadingIcon = {
					Icon(
						imageVector = Icons.Outlined.Search,
						contentDescription = "Search Icon"
					)
				},
				onTextChanged = { validated ->
					val q = validated.copy(
						text = validated.text.filter {
							true
//							it.isLetterOrDigit() || it.isWhitespace() || it in ('\u0980'..'\u09FF')
						}
					)
					query = q.text
					loading = true
					onQueryChanged(q.text, currentPage, pageSize)
				},
				colors = searchInputColors
			)
		}

		StatefulSurface<Page<T>>(
			state = state,
			initialContent = {
				WSubtitleText(
					modifier = Modifier.fillMaxSize(),
					textAlign = TextAlign.Center,
					text = title,
					color = MaterialTheme.colorScheme.onSurface
				)
			},
			resultContent = { page ->
				loading = false
				currentPage = page.number
				pageSize = page.size

				if (page.numberOfElements > 0) {
					content(page)
				} else {
					noContentView.toOption().fold(
						{
							NoContentView(
								message = "No content",
								buttonText = "Clear",
								onButtonClick = {
									query = ""
									onQueryChanged("", 0, pageSize)
								}
							)
						},
						{
							it()
						}
					)
				}
			}
		)
	}
}


fun Char.isBengaliCharacter(): Boolean {
	return this in ('\u0980'..'\u09FF') || this == ' ' // Adjust the Unicode range accordingly
}