package modules.common.features.preferences

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import arrow.core.Option
import arrow.core.toOption
import arrow.fx.coroutines.parZip
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class PrefVM(
	private val dataStore: DataStore<Preferences>
) : ScreenModel {

	fun <T> updatePref(prefItem: PrefItem<T>) = screenModelScope.launch {
		dataStore.updateData {
			val pref = it.toMutablePreferences()
			pref[prefItem.key] = prefItem.value
			pref
		}
	}

	fun <T> getPref(
		key: Preferences.Key<T>,
		defaultValue: T? = null
	): Flow<Option<T>> =
		dataStore.data.map { pref ->
			pref[key].toOption()
		}.map { r ->
			r.onNone {
				defaultValue.toOption().onSome { dv ->
					dataStore.updateData {
						val pref = it.toMutablePreferences()
						pref[key] = dv
						pref
					}
				}
			}
		}

	fun <T> getPrefs(keys: Set<Preferences.Key<T>>): Flow<Map<Preferences.Key<T>, Option<T>>> =
		dataStore.data.map { prefs ->
			keys.associateWith {
				prefs[it].toOption()
			}
		}

	/*
	CAUTION: Blocking code.
	Never use them below unless absolutely necessary
	 */
	fun <T> getPrefBlocking(key: Preferences.Key<T>): Option<T> = runBlocking {
		dataStore.data.map { pref ->
			pref[key].toOption()
		}.first()
	}

	/*
	 double blocking. could have better performance with something like
	 parZip(
			{ getPref(key1).first() },
			{ getPref(key2).first() }
		) { a, b -> Pair(a, b) }
		but this triggers recomposition for some reason.
	 */
	fun <A, B> getPrefsBlocking(
		key1: Preferences.Key<A>,
		key2: Preferences.Key<B>
	): Pair<Option<A>, Option<B>> = runBlocking {
		parZip(
			{ getPrefBlocking(key1) },
			{ getPrefBlocking(key2) }
		) { a, b -> Pair(a, b) }
	}
	/*
	End Blocking code
	 */

}