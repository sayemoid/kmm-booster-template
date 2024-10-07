package utils.expected

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import kotlinx.cinterop.ExperimentalForeignApi
import org.koin.core.module.Module
import org.koin.dsl.module
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSURL
import platform.Foundation.NSUserDomainMask
import utils.createDataStore
import utils.dataStoreFileName

actual val datastoreModule: Module = module {
	single { dataStore() }
}

@OptIn(ExperimentalForeignApi::class)
fun dataStore(): DataStore<Preferences> = createDataStore(
	producePath = {
		val documentDirectory: NSURL? = NSFileManager.defaultManager.URLForDirectory(
			directory = NSDocumentDirectory,
			inDomain = NSUserDomainMask,
			appropriateForURL = null,
			create = false,
			error = null,
		)
		requireNotNull(documentDirectory).path + "/$dataStoreFileName"
	}
)
