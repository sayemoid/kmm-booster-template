package modules.common.base

import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel

actual abstract class ViewModel {
	actual val viewModelScope = MainScope()

	protected actual open fun onCleared() {}

	fun clear() {
		onCleared()
		viewModelScope.cancel()
	}
}