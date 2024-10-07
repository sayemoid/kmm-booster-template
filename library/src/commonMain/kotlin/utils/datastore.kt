package utils

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import okio.Path.Companion.toPath

fun createDataStore(
	producePath: () -> String,
): DataStore<Preferences> = PreferenceDataStoreFactory.createWithPath(
	corruptionHandler = null,
	migrations = emptyList(),
	produceFile = { producePath().toPath() },
)

internal const val dataStoreFileName = "meetings.preferences_pb"