package modules.common.views.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Phone
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import arrow.core.right
import data.responses.toMessage
import data.types.CountryCodes
import data.validation.EmailValidation
import data.validation.PhoneValidation
import data.validation.ValidatedText
import data.validation.ValidationV2
import modules.common.views.dimensions.Paddings

import modules.exampleModule.theme.colorSuccess

@Composable
fun WPhoneField(
	country: CountryCodes = CountryCodes.BD,
	phone: String = "",
	label: String = "Phone",
	icon: ImageVector = Icons.Outlined.Phone,
	readOnly: Boolean = false,
	disableCountrySelection: Boolean = false,
	onPhoneChanged: (CountryCodes, ValidatedText) -> Unit
) {

	var expanded by remember { mutableStateOf(false) }
	var selectedCountry by remember { mutableStateOf(country) }

	Row(
		verticalAlignment = Alignment.CenterVertically,
		horizontalArrangement = Arrangement.Center
	) {

		// Dropdown for selecting the country code
		DropdownMenu(
			modifier = Modifier,
			expanded = expanded, // Change to true when clicked
			onDismissRequest = { expanded = false }
		) {
			CountryCodes.entries.forEach {
				DropdownMenuItem(
					colors = MenuDefaults.itemColors(
						textColor = MaterialTheme.colorScheme.onPrimary
					),
					text = {
						WMetaText(
							text = "${it.countryName} (${it.dialingCode})",
							color = MaterialTheme.colorScheme.primary,
							onClick = {
								selectedCountry = it
								expanded = false
							}
						)
					},
					onClick = {
						selectedCountry = it
						expanded = false
					})
			}
		}

		WOutlinedTextFieldV2(
			text = phone,
			readOnly = readOnly,
			onTextChanged = { validated ->
				val text = validated.copy(
					text = validated.text.filter { it.isDigit() }
				)
				onPhoneChanged(selectedCountry, text)
			},
			label = label,
			leadingIcon = {
				Row(
					modifier = Modifier
						.padding(Paddings.Internal.SmallObjects.horizontal)
						.clickable {
							if (disableCountrySelection) return@clickable
							if (!readOnly) {
								expanded = true
							}
						},
					verticalAlignment = Alignment.CenterVertically,
					horizontalArrangement = Arrangement.Center
				) {
					Icon(
						imageVector = icon,
						contentDescription = "User icon",
						tint = LocalContentColor.current.copy()
					)
					Spacer(modifier = Modifier.width(Paddings.Internal.SmallObjects.horizontal))
					Text(
						text = selectedCountry.dialingCode,
						color = MaterialTheme.colorScheme.onSurface,
						style = MaterialTheme.typography.bodyMedium
					)
				}
			},
			keyboardOptions = KeyboardOptions.Default.copy(
				imeAction = ImeAction.Next,
				keyboardType = KeyboardType.Phone
			),
			validation = PhoneValidation(selectedCountry)
		)
	}

}

@Composable
fun WEmailField(
	label: String = "Email",
	email: String = "",
	icon: ImageVector = Icons.Outlined.Email,
	readOnly: Boolean = false,
	onEmailChanged: (ValidatedText) -> Unit
) {

	WOutlinedTextFieldV2(
		text = email,
		readOnly = readOnly,
		onTextChanged = { validated ->
			onEmailChanged(validated)
		},
		label = label,
		leadingIcon = {
			Icon(
				imageVector = icon,
				contentDescription = "User icon",
				tint = LocalContentColor.current.copy()
			)
		},
		keyboardOptions = KeyboardOptions.Default.copy(
			imeAction = ImeAction.Next,
			keyboardType = KeyboardType.Email
		),
		validation = EmailValidation()
	)
}


@Composable
fun WOutlinedTextFieldV2(
	text: String,
	onTextChanged: (ValidatedText) -> Unit,
	label: String,
	placeholder: String = "",
	enabled: Boolean = true,
	readOnly: Boolean = false,
	modifier: Modifier = Modifier,
	leadingIcon: @Composable (() -> Unit)? = null,
	trailingIcon: @Composable (() -> Unit)? = null,
	visualTransformation: VisualTransformation = VisualTransformation.None,
	keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
	textStyle: TextStyle = LocalTextStyle.current,
	singleLine: Boolean = false,
	maxLines: Int = Int.MAX_VALUE,
	shape: Shape = MaterialTheme.shapes.small,
	colors: TextFieldColors = TextFieldDefaults.colors(),
	validationSuccessMessage: String? = null,
	validation: ValidationV2<String>? = null
) {
	val focusManager = LocalFocusManager.current

	var dirty by remember { mutableStateOf(false) }
	val validity = validation?.apply(text) ?: text.right()

	OutlinedTextField(
		value = text,
		onValueChange = {
			dirty = true
			val v = validation?.apply(it) ?: text.right()
			onTextChanged(ValidatedText(it, v.isRight()))
		},
		label = { Text(text = label) },
		placeholder = { Text(text = placeholder) },
		enabled = enabled,
		readOnly = readOnly,
		modifier = Modifier
			.then(modifier),
		leadingIcon = leadingIcon,
		trailingIcon = trailingIcon,
		isError = validity.isLeft() && dirty,
		visualTransformation = visualTransformation,
		keyboardOptions = keyboardOptions,
		keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
		textStyle = textStyle,
		singleLine = singleLine,
		maxLines = maxLines,
		shape = shape,
		colors = colors,
		supportingText = {
			validity.fold(
				{
					if (!dirty) {
						AutoResizeText(
							modifier = Modifier
								.fillMaxWidth(),
							text = it.instructionMsg,
							color = MaterialTheme.colorScheme.tertiary,
							textAlign = TextAlign.Left,
							maxLines = 1,
							fontSizeRange = FontSizeRange(
								min = MaterialTheme.typography.bodySmall.fontSize,
								max = MaterialTheme.typography.bodyMedium.fontSize
							)
						)
					} else {
						AutoResizeText(
							modifier = Modifier
								.fillMaxWidth(),
							text = it.toMessage().message,
							color = Color.Red,
							textAlign = TextAlign.Left,
							maxLines = 1,
							fontSizeRange = FontSizeRange(
								min = MaterialTheme.typography.bodySmall.fontSize,
								max = MaterialTheme.typography.bodyMedium.fontSize
							)
						)
					}
				},
				{
					validationSuccessMessage?.let { message ->
						AutoResizeText(
							modifier = Modifier
								.fillMaxWidth(),
							text = message,
							color = colorSuccess,
							textAlign = TextAlign.Left,
							maxLines = 2,
							fontSizeRange = FontSizeRange(
								min = MaterialTheme.typography.bodySmall.fontSize,
								max = MaterialTheme.typography.bodyMedium.fontSize
							)
						)
					}
				}
			)
		}
	)

}

