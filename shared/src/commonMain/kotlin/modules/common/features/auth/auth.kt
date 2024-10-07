package modules.common.features.auth

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import arrow.core.Either
import arrow.core.none
import data.responses.ErrMessage
import data.types.flatten
import modules.common.features.auth.models.Auth

@Composable
fun authentication(authVM: AuthVM): Authentication {
	AuthComponent(authVM)
	val auth = authVM.getAuth().collectAsState(none()).value
	return Authentication(
		authVM,
		auth.flatten(),
		auth.fold(
			{ false },
			{ it.isRight() }
		))
}

data class Authentication(
	val authVM: AuthVM,
	val auth: Either<ErrMessage, Auth>,
	val authenticated: Boolean
) {
	fun require(block: () -> Unit = {}) = if (authenticated) {
		block()
	} else {
		authVM.trigger(block = block)
	}

	fun matches(userId: Long) = auth.fold(
		{ false },
		{ it.id == userId }
	)
}
