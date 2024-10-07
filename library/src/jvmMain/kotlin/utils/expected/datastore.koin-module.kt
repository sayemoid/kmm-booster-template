package utils.expected

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import org.koin.core.module.Module
import org.koin.dsl.module
import utils.createDataStore
import utils.dataStoreFileName
import java.io.File

actual val datastoreModule: Module = module {
	single { dataStore() }
}

fun dataStore(): DataStore<Preferences> = createDataStore(
	producePath = {
		// Define the path to the local file
		val file = File("/tmp/", dataStoreFileName)
		file.absolutePath
	}
)
