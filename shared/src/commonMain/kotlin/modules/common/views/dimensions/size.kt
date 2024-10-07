package modules.common.views.dimensions

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun HorizontalSpacer() = Spacer(modifier = Modifier.width(Paddings.General.spacerWidth))

@Composable
fun VerticalSpacer() = Spacer(modifier = Modifier.height(Paddings.General.spacerWidth))

object Paddings {
	object Internal {
		val innerPadding = 10.dp

		object SmallObjects {
			val vertical = 5.dp
			val horizontal = 5.dp
			val tiny = 2.dp
		}
	}

	object Grid {
		val top = 24.dp
		val bottom = 24.dp
		val bottomWithButton = 50.dp
		val horizontalSpacing = 16.dp
		val verticalSpacing = 16.dp
	}

	object Card {
		val horizontal = 16.dp
		val vertical = 20.dp
		val horizontalLarge = 24.dp
		val verticalLarge = 24.dp
		val verticalLargeWithButtons = 50.dp
		val spacerHeight = 16.dp
		val internalHorizontal = 5.dp
		val internalVertical = 8.dp
	}

	object General {
		val surround = 16.dp
		val spacerHeightSmall = 5.dp
		val spacerHeight = 10.dp
		val spacerWidth = 10.dp
		val buttonSpacerWidth = 20.dp
	}

	object Image {
		val surround = 5.dp
	}

	object Screen {
		val horizontal = 10.dp
		val vertical = 0.dp
	}

	object Quiz {
		object Header {
			val horizontal = 16.dp
			val vertical = 10.dp
		}

		object Buttons {
			val horizontal = 0.dp
			val vertical = 20.dp
		}
	}

	object Dialogs {
		val surround = 30.dp

		object Buttons {
			val horizontal = 16.dp
			val vertical = 5.dp
		}
	}

}

