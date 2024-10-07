package utils.expected

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import io.ktor.http.encodeURLPath
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.refTo
import kotlinx.cinterop.toKString
import org.koin.core.module.Module
import org.koin.dsl.module
import platform.posix.fclose
import platform.posix.fgets
import platform.posix.fopen
import utils.createDataStore
import utils.dataStoreFileName

actual val datastoreModule: Module = module {
	single { dataStore() }
}

fun dataStore(): DataStore<Preferences> = createDataStore(
	producePath = {
		// Define the path to the local file
		val file = readFile("/tmp/" + dataStoreFileName)
		file.encodeURLPath()
	}
)

@OptIn(ExperimentalForeignApi::class)
fun readFile(filePath: String): String {
	val file = fopen(filePath, "r") ?: throw IllegalArgumentException("Cannot open file: $filePath")
	try {
		val buffer = StringBuilder()
		val line = ByteArray(1024)
		while (fgets(line.refTo(0), line.size, file) != null) {
			buffer.append(line.toKString())
		}
		return buffer.toString()
	} finally {
		fclose(file)
	}
}