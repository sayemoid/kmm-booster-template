package modules.common.features.auth.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ConfirmationNumber
import androidx.compose.material.icons.outlined.PersonOutline
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import arrow.core.Option
import data.responses.ErrActions
import data.responses.ErrMessage
import data.types.CountryCodes
import data.types.SignUpStates
import data.validation.OTPValidation
import data.validation.ValidatedText
import data.validation.genericValidation
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import modules.common.features.auth.WPasswordField
import modules.common.features.auth.models.RegMethod
import modules.common.features.auth.models.SignUpReq
import modules.common.features.auth.models.UsernameAvailableResponse
import modules.common.features.auth.models.VerificationData
import modules.common.features.auth.models.VerificationResponse
import modules.common.views.components.AppLogo
import modules.common.views.components.ErrorView
import modules.common.views.components.HyperlinkedText
import modules.common.views.components.LoadingView
import modules.common.views.components.WEmailField
import modules.common.views.components.WImage
import modules.common.views.components.WMetaText
import modules.common.views.components.WOutlinedTextFieldV2
import modules.common.views.components.WPhoneField
import modules.common.views.dimensions.Paddings
import utils.RemoteResult
import utils.Tag
import utils.logD
import kotlin.time.Duration.Companion.seconds

@Composable
fun SignupComponent(
	state: SignUpStates,
	usernameAvailableResponse: Option<UsernameAvailableResponse>,
	logo: Painter,
	backgroundGraphics: Painter,
	message: ErrMessage? = null,
	onLoginClick: () -> Unit = {},
	onVerifyClick: (data: VerificationData) -> Unit = { _ -> },
	onUsernameChange: (String) -> Unit,
	onSignUpClick: (String, SignUpReq) -> Unit = { _, _ -> },
) {

	Column(
		horizontalAlignment = Alignment.CenterHorizontally,
		verticalArrangement = Arrangement.Center
	) {

		var errMessage by remember { mutableStateOf(message) }

		Box(
			modifier = Modifier.fillMaxSize()
				.background(MaterialTheme.colorScheme.background)
		) {
			WImage(
				modifier = Modifier.fillMaxSize(),
				painter = backgroundGraphics,
				alignment = Alignment.BottomCenter,
				contentDescription = "Background graphics box"
			)
			Column(
				modifier = Modifier.fillMaxSize()
					.verticalScroll(rememberScrollState())
					.padding(Paddings.General.surround),
				horizontalAlignment = Alignment.CenterHorizontally,
				verticalArrangement = Arrangement.Center
			) {
				AppLogo(logo = logo, contentDescription = "App Logo")

				errMessage?.let {
					ErrorView(
						modifier = Modifier,
						err = it,
						onlyMessage = true,
						contentBackground = MaterialTheme.colorScheme.errorContainer,
						messageColor = MaterialTheme.colorScheme.error
					)
				}

				Column {
					when (state) {
						SignUpStates.Init -> {
							VerificationForm(onVerifyClick = onVerifyClick)
						}

						SignUpStates.Loading -> {
							LoadingView()
						}

						is SignUpStates.Acknowledgement<*> -> {
							state.result.fold(
								{
									errMessage = it
									VerificationForm(onVerifyClick = onVerifyClick)
								},
								{
									val result = (it as RemoteResult<*>)
									val body = result.body as VerificationResponse
									val headers = ErrActions.from(result.headers)
									if (headers.contains(ErrActions.TRIGGER_OTP_INPUT)) {
										errMessage = ErrMessage(
											"",
											"OTP already sent. Please check your messages."
										)
									}
									RegistrationForm(
										verificationResponse = body,
										usernameAvailableResponse,
										onNewOTPRequest = {},
										onSignUpClick = onSignUpClick,
										onUsernameChange = onUsernameChange
									)
								}
							)
						}

						is SignUpStates.Result<*> -> {
							state.result.fold(
								{
									errMessage = it
									VerificationForm(onVerifyClick = onVerifyClick)
								},
								{
									logD(Tag.Auth.SignUp, "Signup successful!" + it.toString())
								}
							)
						}
					}
					Spacer(modifier = Modifier.height(8.dp))
				}
				Spacer(modifier = Modifier.height(Paddings.General.spacerHeight))
				HyperlinkedText(
					modifier = Modifier.fillMaxWidth().padding(8.dp),
					text = "Already have an account?",
					url = "",
					color = MaterialTheme.colorScheme.primary,
					textAlign = TextAlign.Center,
					style = TextStyle(
						fontSize = 14.sp, fontFamily = FontFamily.Default
					)
				) {}
				Button(
					modifier = Modifier.width(150.dp),
					onClick = onLoginClick
				) {
					Text("LOGIN")
				}
				HyperlinkedText(
					modifier = Modifier.fillMaxWidth().padding(8.dp),
					text = "Privacy Policy",
					url = "https://blog.sayem.dev/privacy-policy-for-pollbox/",
					color = MaterialTheme.colorScheme.primary,
					textAlign = TextAlign.Center,
					style = TextStyle(
						fontSize = 14.sp, fontFamily = FontFamily.Default
					)
				) {}
				HyperlinkedText(
					modifier = Modifier.fillMaxWidth().padding(8.dp),
					text = "Version 1.0.0",
					url = "https://cognitox.org",
					color = MaterialTheme.colorScheme.primary,
					textAlign = TextAlign.Center,
					style = TextStyle(
						fontSize = 16.sp, fontFamily = FontFamily.Default
					),
					fontWeight = FontWeight.Bold
				) {}
			}
		}
	}
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun VerificationForm(
	onVerifyClick: (data: VerificationData) -> Unit
) {
	val keyboardController = LocalSoftwareKeyboardController.current
	var country by remember { mutableStateOf(CountryCodes.BD) }

	var registerWithEmail by remember { mutableStateOf(true) }

	var phone by remember { mutableStateOf(ValidatedText("", false)) }
	var email by remember { mutableStateOf(ValidatedText("", false)) }

	Column(
		modifier = Modifier.fillMaxWidth()
			.padding(0.dp, Paddings.General.spacerHeight),
		verticalArrangement = Arrangement.Center,
		horizontalAlignment = Alignment.CenterHorizontally
	) {
		Text(
			text = "EMAIL OR PHONE",
			fontWeight = FontWeight.Bold,
			fontSize = MaterialTheme.typography.bodySmall.fontSize
		)
		Spacer(modifier = Modifier.width(Paddings.General.spacerWidth))
		Switch(
			checked = !registerWithEmail,
			onCheckedChange = {
				registerWithEmail = !it
			},
			colors = SwitchDefaults.colors(
				checkedThumbColor = MaterialTheme.colorScheme.primary,
				checkedTrackColor = MaterialTheme.colorScheme.primaryContainer,
				uncheckedThumbColor = MaterialTheme.colorScheme.secondary,
				uncheckedTrackColor = MaterialTheme.colorScheme.secondaryContainer,
			)
		)
	}

	if (registerWithEmail) {
		WEmailField(
			email = email.text
		) { text ->
			email = text
		}
	} else {
		WPhoneField(
			country = country,
			phone = phone.text,
			disableCountrySelection = true
		) { c, text ->
			country = c
			phone = text
		}
	}

	Spacer(modifier = Modifier.height(Paddings.General.spacerHeight))
	Button(modifier = Modifier.fillMaxWidth()
		.padding(20.dp, 0.dp),
		enabled = if (registerWithEmail) email.valid else phone.valid,
		colors = ButtonDefaults.buttonColors(
			containerColor = MaterialTheme.colorScheme.primary,
			contentColor = MaterialTheme.colorScheme.onPrimary
		),
		onClick = {
			onVerifyClick(
				if (registerWithEmail) {
					VerificationData.Email(email.text)
				} else {
					VerificationData.Phone(country, phone.text)
				}
			)
			keyboardController?.hide()
		}
	) {
		Text("NEXT")
	}
}

@Composable
fun RegistrationForm(
	verificationResponse: VerificationResponse,
	usernameAvailableResponse: Option<UsernameAvailableResponse>,
	onNewOTPRequest: (String) -> Unit,
	onUsernameChange: (String) -> Unit,
	onSignUpClick: (String, SignUpReq) -> Unit,
) {
	val keyboardController = LocalSoftwareKeyboardController.current
	var otp by remember { mutableStateOf(ValidatedText("", false)) }
	var timer by remember { mutableStateOf(verificationResponse.validity.inWholeSeconds) }


	LaunchedEffect(Unit) {
		coroutineScope {
			repeat(timer.toInt()) {
				delay(1.seconds)
				timer--
			}
		}

	}

	var showRegistrationForm by remember { mutableStateOf(false) }

	var name by remember { mutableStateOf(ValidatedText("", false)) }
	var gender by remember { mutableStateOf(ValidatedText("", false)) }
	var username by remember { mutableStateOf(ValidatedText("", false)) }
	var password by remember { mutableStateOf(ValidatedText("", false)) }

	Column {
		OTPField(
			text = otp.text,
			readOnly = showRegistrationForm,
		) { otp = it }

		if (timer > 0) {
			Column(
				modifier = Modifier.fillMaxWidth(),
				horizontalAlignment = Alignment.CenterHorizontally
			) {
				WMetaText(
					modifier = Modifier.fillMaxWidth(),
					textAlign = TextAlign.Center,
					text = "OTP will expire in",
					color = MaterialTheme.colorScheme.secondary
				)
				WMetaText(
					modifier = Modifier.fillMaxWidth(),
					textAlign = TextAlign.Center,
					text = "$timer's",
					color = MaterialTheme.colorScheme.error
				)
			}
		} else {
			WMetaText(
				modifier = Modifier
					.fillMaxWidth()
					.padding(Paddings.General.surround)
					.clickable {
						onNewOTPRequest(verificationResponse.identity)
					},
				textAlign = TextAlign.Center,
				text = "Request another otp",
				color = MaterialTheme.colorScheme.tertiary
			)
		}

		AnimatedVisibility(showRegistrationForm) {
			Column {

				WOutlinedTextFieldV2(
					modifier = Modifier.fillMaxWidth(),
					text = name.text,
					label = "Name",
					onTextChanged = { name = it },
					validation = genericValidation(
						message = "Name should be at least 3 characters"
					) {
						it.length > 3
					}
				)

				var expanded by remember { mutableStateOf(false) }

				Box(
					modifier = Modifier.fillMaxWidth()
						.clickable { expanded = true },
				) {
					val genders = mapOf(
						"Male" to "MALE",
						"Female" to "FEMALE"
					)
					WOutlinedTextFieldV2(
						modifier = Modifier.fillMaxWidth(),
						leadingIcon = {
							Icon(
								imageVector = Icons.Outlined.PersonOutline,
								contentDescription = "Gender"
							)
						},
						text = gender.text,
						label = "Gender",
						enabled = false,
						onTextChanged = { gender = it },
						colors = TextFieldDefaults.colors(
							disabledLabelColor = MaterialTheme.colorScheme.onSurface,
							disabledTextColor = MaterialTheme.colorScheme.onSurface
						),
						validation = genericValidation(
							message = "You must choose a gender."
						) { text ->
							genders.any { it.value == text }
						}
					)

					DropdownMenu(
						expanded = expanded,
						onDismissRequest = { expanded = false },
						content = {
							val onClick: (String) -> Unit = {
								gender = ValidatedText(it, true)
								expanded = false
							}
							genders.forEach {
								DropdownMenuItem(
									text = {
										WMetaText(
											text = it.key,
											onClick = { onClick(it.value) })
									},
									onClick = { onClick(it.value) },
									colors = MenuDefaults.itemColors(
										textColor = MaterialTheme.colorScheme.primary
									)
								)
							}
						}
					)
				}

				WOutlinedTextFieldV2(
					modifier = Modifier.fillMaxWidth(),
					text = username.text,
					label = "Username",
					onTextChanged = { validated ->
						val u = validated.copy(
							text = validated.text.filter { it.isLetterOrDigit() }
						)
						username = u
						if (u.text.length >= 4) {
							onUsernameChange(u.text)
							username = ValidatedText(
								text = u.text,
								valid = u.valid && usernameAvailableResponse.fold({ false },
									{ uname -> uname.available })
							)
						}
					},
					validationSuccessMessage = "Username is available",
					validation = genericValidation(
						message = if (username.valid) {
							usernameAvailableResponse.fold(
								{ "" },
								{ it.reason }
							)
						} else "Username must be at least 5 characters"
					) { text ->
						text.length >= 5 && usernameAvailableResponse.fold({ false },
							{ it.available })
					}
				)

				WPasswordField(
					text = password,
					validation = genericValidation(
						message = "Password is invalid."
					) {
						it.length >= 6
					}
				) { password = it }

			}
		}

		Spacer(modifier = Modifier.height(Paddings.General.spacerHeight))

		val actionButtonEnabled = if (!showRegistrationForm) {
			otp.valid
		} else {
			val usernameAvailable = usernameAvailableResponse.fold({ false }, { it.available })
			otp.valid && name.valid && usernameAvailable && gender.valid && password.valid
		}

		Button(modifier = Modifier.fillMaxWidth()
			.padding(20.dp, 0.dp),
			colors = ButtonDefaults.buttonColors(
				containerColor = MaterialTheme.colorScheme.primary,
				contentColor = MaterialTheme.colorScheme.onPrimary
			),
			enabled = actionButtonEnabled,
			onClick = {
				if (!showRegistrationForm) {
					showRegistrationForm = true
				} else {
					onSignUpClick(
						otp.text,
						SignUpReq(
							name = name.text,
							gender = gender.text,
							username = username.text,
							password = password.text,
							phone = if (verificationResponse.regMethod == RegMethod.PHONE) {
								verificationResponse.identity
							} else null,
							email = if (verificationResponse.regMethod == RegMethod.EMAIL) {
								verificationResponse.identity
							} else null,
							role = "User"
						)
					)
				}
				keyboardController?.hide()
			}
		) {
			Text(if (showRegistrationForm) "SIGN UP" else "SUBMIT")
		}
	}

}

@Composable
private fun OTPField(
	text: String = "",
	label: String = "OTP",
	readOnly: Boolean = false,
	icon: ImageVector = Icons.Outlined.ConfirmationNumber,
	onOtpChanged: (ValidatedText) -> Unit
) {
	WOutlinedTextFieldV2(
		readOnly = readOnly,
		text = text,
		onTextChanged = { validated ->
			val otp = validated.copy(
				text = validated.text.filter { it.isDigit() }
			)
			onOtpChanged(otp)
		},
		label = label,
		modifier = Modifier.fillMaxWidth(),
		leadingIcon = {
			Icon(
				imageVector = icon,
				contentDescription = "User icon",
				tint = LocalContentColor.current.copy()
			)
		},
		keyboardOptions = KeyboardOptions.Default.copy(
			imeAction = ImeAction.Next,
			keyboardType = KeyboardType.Number
		),
		validation = OTPValidation
	)
}