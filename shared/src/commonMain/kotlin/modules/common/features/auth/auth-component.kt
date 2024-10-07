package modules.common.features.auth

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
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
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import arrow.core.none
import arrow.core.toOption
import cognito_kmm_template.shared.generated.resources.Res
import cognito_kmm_template.shared.generated.resources.box
import cognito_kmm_template.shared.generated.resources.compose_multiplatform
import configs.Credentials
import data.responses.ErrMessage
import data.responses.ErrTypes
import data.types.State
import data.validation.ValidatedText
import data.validation.ValidationV2
import data.validation.genericValidation
import modules.common.animations.Transition
import modules.common.features.auth.components.SignupComponent
import modules.common.features.auth.models.Auth
import modules.common.views.components.AppLogo
import modules.common.views.components.ErrorView
import modules.common.views.components.HyperlinkedText
import modules.common.views.components.StatefulSurface
import modules.common.views.components.WDialog
import modules.common.views.components.WImage
import modules.common.views.components.WOutlinedTextFieldV2
import modules.common.views.dimensions.Paddings
import org.jetbrains.compose.resources.painterResource
import utils.expected.isDebug

@Composable
fun AuthComponent(authVM: AuthVM) {
	val (triggered, block: () -> Unit) = authVM.authTrigger.collectAsState().value
	if (triggered) {
		val authenticated = authVM.getAuth().collectAsState(none()).value.fold(
			{ false },
			{ it.isRight() }
		)

		if (!authenticated) {
			var showDialog by remember { mutableStateOf(true) }
			AnimatedVisibility(
				visible = showDialog,
				enter = Transition.SimpleSlideInOut.enter,
				exit = Transition.SimpleSlideInOut.exit
			) {
				WDialog(
					modifier = Modifier
						.height(600.dp),
					onDismissRequest = {
						showDialog = false
						authVM.trigger(false)
					}
				) {
					val logoRes = Res.drawable.compose_multiplatform
					val backgroundRes = Res.drawable.box
					val onLogin = { username: String, password: String ->
						authVM.login(
							username,
							password,
							block
						)
					}

					var showSignup by remember { mutableStateOf(false) }
					if (!showSignup) {
						LoginComponent(
							state = authVM.state.collectAsState().value,
							logo = painterResource(logoRes),
							backgroundGraphics = painterResource(backgroundRes),
							onSignup = { showSignup = true },
							onLogin = { u, p ->
								onLogin(u, p)
							}
						)
					} else {
						LaunchedEffect(Unit) { authVM.resetSignupState() }
						SignupComponent(
							state = authVM.signUpState.collectAsState().value,
							usernameAvailableResponse = authVM.usernameResponse.collectAsState().value
								.flatMap { r -> r.fold({ none() }, { it.toOption() }) },
							logo = painterResource(logoRes),
							backgroundGraphics = painterResource(backgroundRes),
							onLoginClick = { showSignup = false },
							onUsernameChange = {
								authVM.checkUsername(it)
							},
							onVerifyClick = { verificationData ->
								authVM.requestVerification(verificationData)
							},
							onSignUpClick = { token, signUpReq ->
								authVM.signup(
									token, signUpReq, block
								)
							}
						)
					}
				}
			}
		}
	}

}

@Composable
fun LoginComponent(
	state: State,
	logo: Painter,
	backgroundGraphics: Painter,
	message: ErrMessage? = null,
	onLogin: (username: String, password: String) -> Unit,
	onSignup: () -> Unit = {}
) {
	StatefulSurface<Auth>(
		state = state,
		initialContent = {
			LoginView(
				logo = logo,
				backgroundGraphics = backgroundGraphics,
				onSignup = onSignup,
				onLogin = { u, p -> onLogin(u, p) },
				message = message ?: ErrMessage(
					type = ErrTypes.VALIDATION.type,
					message = "You need to login to complete this action."
				)
			)
		},
		errorView = {
			LoginView(
				logo = logo,
				backgroundGraphics = backgroundGraphics,
				onSignup = onSignup,
				onLogin = { u, p -> onLogin(u, p) },
				message = it
			)
		},
		resultContent = {
			LoginView(
				logo = logo,
				backgroundGraphics = backgroundGraphics,
				onSignup = onSignup,
				onLogin = { u, p -> onLogin(u, p) }
			)
		}
	)
}

@Composable
fun LoginView(
	logo: Painter,
	backgroundGraphics: Painter,
	onSignup: () -> Unit,
	onLogin: (username: String, password: String) -> Unit,
	message: ErrMessage? = null
) {
	var username by remember {
		mutableStateOf(
			if (isDebug) ValidatedText(
				Credentials.UserPass.username.get(),
				false
			) else ValidatedText("", false)
		)
	}
	var password by remember {
		mutableStateOf(
			if (isDebug) ValidatedText(
				Credentials.UserPass.password.get(),
				false
			) else ValidatedText("", false)
		)
	}
	val errMessage by remember { mutableStateOf(message) }
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
				.padding(24.dp),
			horizontalAlignment = Alignment.CenterHorizontally,
			verticalArrangement = Arrangement.SpaceAround
		) {
			val keyboardController = LocalSoftwareKeyboardController.current
			AppLogo(
				modifier = Modifier
					.height(96.dp),
				logo = logo,
				contentDescription = "App Logo"
			)
			Spacer(Modifier.height(Paddings.General.spacerHeight))
			errMessage?.let {
				ErrorView(
					modifier = Modifier,
					err = it,
					onlyMessage = true,
					contentBackground = MaterialTheme.colorScheme.tertiaryContainer
				)
			}
			Column {
				WUsernameField(text = username) { username = it }
				Spacer(modifier = Modifier.height(8.dp))
				WPasswordField(
					text = password,
					validation = genericValidation(
						message = "Must be at least 6 characters."
					) { it.length >= 6 }
				) { password = it }
//				HyperlinkedText(
//					modifier = Modifier.fillMaxWidth().padding(8.dp),
//					text = "Forgot Password?",
//					url = "https://cognitox.org",
//					color = MaterialTheme.colorScheme.primary,
//					textAlign = TextAlign.Right,
//					style = TextStyle(
//						fontSize = 14.sp, fontFamily = FontFamily.Default
//					)
//				) {}
			}
			Spacer(modifier = Modifier.height(16.dp))
			Button(
				modifier = Modifier.fillMaxWidth().padding(20.dp, 0.dp),
				colors = ButtonDefaults.buttonColors(
					containerColor = MaterialTheme.colorScheme.primary,
					contentColor = MaterialTheme.colorScheme.onPrimary
				),
				onClick = {
					if (username.valid && password.valid) {
						onLogin(username.text, password.text)
						keyboardController?.hide()
					}
				}) {
				Text("LOGIN")
			}
			HyperlinkedText(
				modifier = Modifier.fillMaxWidth().padding(8.dp),
				text = "Don\'t have any account?",
				url = "https://cognitox.org",
				color = MaterialTheme.colorScheme.primary,
				textAlign = TextAlign.Center,
				style = TextStyle(
					fontSize = 14.sp, fontFamily = FontFamily.Default
				)
			) {}
			Button(
				modifier = Modifier.width(150.dp),
				onClick = onSignup
			) {
				Text("SIGN UP")
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

@Composable
private fun WUsernameField(
	text: ValidatedText = ValidatedText("", false),
	label: String = "Username",
	icon: ImageVector = Icons.Outlined.Person,
	onUserNameChanged: (ValidatedText) -> Unit
) {
	WOutlinedTextFieldV2(
		text = text.text,
		onTextChanged = onUserNameChanged,
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
			imeAction = ImeAction.Next
		),
		validation = genericValidation(
			message = "Must be at least 5 characters."
		) {
			it.length >= 5
		}
	)
}


@Composable
fun WPasswordField(
	text: ValidatedText = ValidatedText("", false),
	label: String = "Password",
	icon: ImageVector = Icons.Outlined.Lock,
	validation: ValidationV2<String>? = null,
	onPasswordChanged: (ValidatedText) -> Unit
) {
	WOutlinedTextFieldV2(
		text = text.text,
		onTextChanged = {
			onPasswordChanged(it)
		},
		label = label,
		modifier = Modifier.fillMaxWidth(),
		leadingIcon = {
			Icon(
				imageVector = icon,
				contentDescription = "Password Icon",
				tint = LocalContentColor.current.copy()
			)
		},
		keyboardOptions = KeyboardOptions.Default.copy(
			imeAction = ImeAction.Done, keyboardType = KeyboardType.Password
		),
		visualTransformation = PasswordVisualTransformation(),
		validation = validation
	)
}



