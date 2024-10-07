package utils.expected

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import org.koin.core.module.Module
import org.koin.dsl.module
import utils.createDataStore
import utils.dataStoreFileName


actual val datastoreModule: Module = module {
	single { dataStore(get())}
}

fun dataStore(context: Context): DataStore<Preferences> =
	createDataStore(
		producePath = { context.filesDir.resolve(dataStoreFileName).absolutePath }
	)
