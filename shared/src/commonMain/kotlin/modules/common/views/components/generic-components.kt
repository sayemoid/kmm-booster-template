package modules.common.views.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Error
import androidx.compose.material.icons.outlined.SelfImprovement
import androidx.compose.material.icons.rounded.HourglassTop
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import data.responses.ErrMessage
import data.types.RemoteData
import data.types.RemoteDataPaginated
import data.types.RemoteListData
import data.types.State
import modules.common.animations.EnterAnimation
import modules.common.animations.LoadingAnimation
import modules.common.animations.Transition
import modules.common.di.getKoinInstance
import modules.common.features.auth.AuthVM
import modules.common.features.auth.authentication
import modules.exampleModule.screens.Screens

enum class SimpleStates {
	LOADING, CONTENT, EXITED
}

@Composable
fun SimpleThreeStateView(
	modifier: Modifier = Modifier,
	loadingView: (() -> Unit)? = null,
	exitView: (() -> Unit)? = null,
	content: @Composable () -> SimpleStates
) {
	var state by remember { mutableStateOf(SimpleStates.CONTENT) }

	when (state) {
		SimpleStates.LOADING -> {
			loadingView?.let { it() }
				?: LoadingView(modifier = modifier)
		}

		SimpleStates.CONTENT -> {
			state = content()
		}

		SimpleStates.EXITED -> {
			exitView?.let { it() }
				?: LoadingView(modifier = modifier)
		}
	}
}

@Composable
fun <T> StatefulSurface(
	state: State,
	initialContent: @Composable () -> Unit,
	loadingView: @Composable () -> Unit = {
		LoadingView(
			modifier = Modifier.background(MaterialTheme.colorScheme.background),
		)
	},
	errorView: @Composable (err: ErrMessage) -> Unit = {
		ErrorView(
			modifier = Modifier.background(
				MaterialTheme.colorScheme.background
			), err = it
		)
	},
	resultContent: @Composable (t: T) -> Unit = {}
) = when (state) {
	State.Init -> {
		EnterAnimation {
			initialContent()
		}
	}

	State.Loading -> loadingView()

	is State.Result<*> -> {
		state.result.fold(
			{
				EnterAnimation {
					errorView(it)
				}
			},
			{
				EnterAnimation {
					resultContent(it as T)
				}
			}
		)
	}
}

@Composable
fun <T> StatelessSurface(
	remoteData: RemoteData<T>,
	modifier: Modifier = Modifier,
	loadingView: @Composable () -> Unit? = { LoadingView() },
	errorView: @Composable ((err: ErrMessage) -> Unit)? = null,
	content: @Composable (T) -> Unit
) = Surface(modifier = Modifier.then(modifier)) {
	LoadingAnimation(remoteData.isNone()) {
		loadingView()
	}
	LoadingAnimation(remoteData.isSome()) {
		remoteData.onSome { data ->
			data.fold(
				{ errMsg ->
					errorView?.let {
						it(errMsg)
					} ?: ErrorView(err = errMsg)
				},
				{ c ->
					content(c)
				}
			)
		}
	}
}

@Composable
fun <T> IndefiniteSurface(
	modifier: Modifier = Modifier,
	items: Map<Int, T>,
	selectedIndex: Int = 0,
	itemView: @Composable (index: Int, value: T, onNext: (showResult: Boolean) -> Unit) -> Unit,
	resultView: @Composable (value: T, onNext: (showResult: Boolean) -> Unit) -> Unit,
	onFinished: () -> Unit,
) {
	Surface(modifier = modifier) {
		var index by remember { mutableStateOf(0) }
		var showResult by remember { mutableStateOf(false) }


		/*
		When selected index is changed, update the visible index.
		 */
		LaunchedEffect(selectedIndex) {
			index = selectedIndex
		}

		if (index >= items.size) {
			onFinished()
		} else {
			items.forEach { item ->
				AnimatedVisibility(
					visible = index == item.key,
//					enter = Transition.SimpleSlideInOut.enter,
//					exit = Transition.SimpleSlideInOut.exit
					enter = Transition.SlideInSlideOut(LocalDensity.current).enter,
					exit = Transition.SlideInSlideOut(LocalDensity.current).exit
				) {

					if (!showResult) {
						itemView(index, item.value) {
							showResult = it
							if (!showResult) {
								index++
							}
						}
					}

					if (showResult) {
						resultView(item.value) {
							showResult = it
							index++
						}
					}

				}
			}
		}
	}
}

@Composable
fun <T> LazyColumnWithLoading(
	remoteData: RemoteDataPaginated<T>,
	modifier: Modifier = Modifier,
	loadingView: @Composable () -> Unit = {
		LoadingView()
	},
	itemView: @Composable (T) -> Unit
) = Surface(modifier = Modifier.then(modifier)) {

	LoadingAnimation(remoteData.isNone()) {
		loadingView()
	}
	LoadingAnimation(remoteData.isSome()) {
		remoteData.onSome { data ->
			data.fold(
				{
					ErrorView(err = it)
				},
				{ page ->
					LazyColumn {
						items(
							count = page.content.size,
						) {
							itemView(page.content[it])
							Spacer(modifier = Modifier.height(15.dp))
						}
					}
				}
			)
		}
	}
}

@Composable
fun <T> LazyColumnWithLoadingForList(
	remoteData: RemoteListData<T>,
	modifier: Modifier = Modifier,
	loadingView: @Composable () -> Unit = { LoadingView() },
	emptyView: @Composable () -> Unit = {},
	itemView: @Composable (T) -> Unit
) = Surface(modifier = Modifier.then(modifier)) {

	LoadingAnimation(remoteData.isNone()) {
		loadingView()
	}
	LoadingAnimation(remoteData.isSome()) {
		remoteData.onSome { data ->
			data.fold(
				{
					ErrorView(err = it)
				},
				{ items ->
					if (items.isEmpty()) {
						Box(
							modifier = Modifier.fillMaxSize(),
							contentAlignment = Alignment.Center,
							content = { emptyView() }
						)
					} else {
						LazyColumn {
							items(
								count = items.size,
							) {
								itemView(items[it])
								Spacer(modifier = Modifier.height(15.dp))
							}
						}
					}
				}
			)
		}
	}
}

@Composable
fun ErrorView(
	modifier: Modifier = Modifier,
	err: ErrMessage,
	onlyMessage: Boolean = false,
	contentBackground: Color = MaterialTheme.colorScheme.tertiaryContainer,
	titleColor: Color = MaterialTheme.colorScheme.secondary,
	messageColor: Color = MaterialTheme.colorScheme.tertiary,
	descriptionColor: Color = MaterialTheme.colorScheme.tertiary
) {
	if (err.message.contains("Invalid refresh token")) {
		val authVM = getKoinInstance<AuthVM>()
		authVM.logout()
		LocalNavigator.currentOrThrow.replaceAll(Screens.Main.screen)
		authentication(authVM).require()
		return
	}

	Box(
		modifier = Modifier.fillMaxSize()
			.background(Color.Transparent),
		contentAlignment = Alignment.Center
	) {
		Card(modifier = modifier) {
			Column(
				modifier = Modifier.background(contentBackground)
					.padding(10.dp),
				horizontalAlignment = Alignment.CenterHorizontally
			) {
				if (!onlyMessage) {
					Icon(Icons.Outlined.Error, err.type)
					Spacer(modifier = Modifier.height(16.dp))
					WSubtitleText(
						color = titleColor,
						text = "${err.statusCode} ${err.type}"
					)
					Spacer(modifier = Modifier.height(16.dp))
				}

				WParagraph(color = messageColor, text = err.mappedMessage)

				if (err.description.isNotBlank() && !onlyMessage) {
					Spacer(modifier = Modifier.height(16.dp))
					WMetaText(color = descriptionColor, text = err.mappedDescription)
				}
			}
		}
	}
}

@Composable
fun LoadingView(modifier: Modifier = Modifier, logo: Painter? = null) {
	Box(
		modifier = Modifier.fillMaxSize()
			.then(modifier),
		contentAlignment = Alignment.Center
	) {
		Column(
			horizontalAlignment = Alignment.CenterHorizontally
		) {
			logo?.let {
				AppLogo(logo = logo, contentDescription = "App Logo")
			} ?: Icon(
				modifier = Modifier.size(48.dp),
				imageVector = Icons.Rounded.HourglassTop,
				contentDescription = "Loading Icon",
				tint = MaterialTheme.colorScheme.primary
			)
			Spacer(modifier = Modifier.height(10.dp))
			CircularProgressIndicator()
			Spacer(modifier = Modifier.height(16.dp))
			WSubtitleText(color = MaterialTheme.colorScheme.primary, text = "Loading..")
		}
	}
}

@Composable
fun AppLogo(
	modifier: Modifier = Modifier,
	logo: Painter,
	contentDescription: String?
) {
	WImage(
		painter = logo,
		contentDescription = contentDescription,
		alignment = Alignment.CenterStart,
		modifier = Modifier
			.height(72.dp)
			.width(72.dp)
			.then(modifier)
	)
}

@Composable
fun NoContentView(
	paddingValues: PaddingValues = PaddingValues(),
	icon: ImageVector = Icons.Outlined.SelfImprovement,
	message: String,
	buttonText: String = "RELOAD",
	onButtonClick: () -> Unit
) {
	Box(
		modifier = Modifier.fillMaxSize()
			.padding(paddingValues)
			.background(MaterialTheme.colorScheme.surface),
	) {

		Column(
			modifier = Modifier.align(Alignment.Center),
			horizontalAlignment = Alignment.CenterHorizontally
		) {
			Icon(
				modifier = Modifier.size(96.dp),
				imageVector = icon,
				contentDescription = "No content icon",
				tint = MaterialTheme.colorScheme.primary
			)

			WSubtitleText(
				modifier = Modifier.padding(24.dp),
				text = message,
				color = MaterialTheme.colorScheme.onSurface,
				maxLines = 3
			)

			Button(
				modifier = Modifier.padding(72.dp, 24.dp),
				colors = ButtonDefaults.buttonColors(
					containerColor = MaterialTheme.colorScheme.primary,
					contentColor = MaterialTheme.colorScheme.onPrimary
				),
				onClick = onButtonClick
			) { Text(buttonText) }
		}
	}
}
