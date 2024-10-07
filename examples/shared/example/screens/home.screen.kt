package modules.exampleModule.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import modules.common.views.dimensions.Paddings
import modules.common.views.screens.AppScreen
import modules.exampleModule.screens.layouts.ExampleAppLayout
import utils.show

object MainScreen : AppScreen {

	@Composable
	override fun Content() {

		ExampleAppLayout { paddingValues, snackbar ->

			Column(
				modifier = Modifier.padding(paddingValues)
					.fillMaxSize()
					.padding(Paddings.Screen.horizontal),
				verticalArrangement = Arrangement.Center,
				horizontalAlignment = Alignment.CenterHorizontally
			) {
				Text(
					modifier = Modifier.clickable {
						snackbar.show("Unbelievable! You've successfully clicked me.")
					},
					textAlign = TextAlign.Center,
					text = "Click Me"
				)
			}
		}
	}

}

