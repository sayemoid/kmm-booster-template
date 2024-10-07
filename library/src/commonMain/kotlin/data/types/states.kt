package data.types

import arrow.core.Either
import data.responses.ErrMessage

sealed interface State {
	data object Init : State
	data object Loading : State
	data class Result<T>(val result: Either<ErrMessage, T>) : State
}

sealed interface SignUpStates {
	data object Init : SignUpStates
	data object Loading : SignUpStates
	data class Acknowledgement<T>(val result: Either<ErrMessage, T>) : SignUpStates
	data class Result<T>(val result: Either<ErrMessage, T>) : SignUpStates
}