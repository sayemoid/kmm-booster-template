package data.types

import arrow.core.Either
import arrow.core.Option
import arrow.core.left
import data.Page
import data.responses.ErrMessage
import data.responses.toMessage

typealias RemoteListData<T> = Option<Either<ErrMessage, List<T>>>
typealias RemoteDataPaginated<T> = Option<Either<ErrMessage, Page<T>>>
typealias RemoteData<T> = Option<Either<ErrMessage, T>>

fun <T> RemoteData<T>.flatten() = this.fold(
	{ Err.NotExistsError.toMessage().left() },
	{
		it
	}
)

fun Option<Boolean>.flatten() = this.fold({ false }, { it })