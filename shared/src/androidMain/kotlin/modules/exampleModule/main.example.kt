package modules.exampleModule

import android.Manifest
import android.app.Application
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import cafe.adriel.voyager.navigator.Navigator
import com.google.android.gms.ads.MobileAds
import configs.Credentials
import modules.common.features.notifications.models.NDataKeys
import modules.exampleModule.di.koinExampleModules
import modules.exampleModule.screens.MainScreen
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.GlobalContext
import utils.Tag
import utils.expected.CrashAnalytics
import utils.logD
import java.util.Locale


class ExampleApp : Application() {
	override fun onCreate() {
		CrashAnalytics(this).init(Credentials.Sentry.dsn.get())
		super.onCreate()
		GlobalContext.startKoin {
			androidContext(this@ExampleApp)
			androidLogger()
			modules(koinExampleModules)
		}
	}
}

class MainActivity : AppCompatActivity() {
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		this.window.setSoftInputMode(
			WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE
					or WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE
		)

		val taskId = intent.getLongExtra(NDataKeys.REFERENCE_ID.key, -1)
		logD(Tag.Network.PushMessage, taskId.toString())

		setContent {
			Navigator(
				screen = MainScreen
			)
		}

		logD(Tag.Locale, Locale.getDefault().language)

		askNotificationPermission()

		// Initialize mobile ads
		MobileAds.initialize(this)
	}

	override fun onNewIntent(intent: Intent) {
		super.onNewIntent(intent)
		/*
		Do something based on the intent.action
		May be open a different page or something
		 */
		setContent {
			Navigator(screen = MainScreen)
		}
	}

	// firbase notification permission
	// Declare the launcher at the top of your Activity/Fragment:
	private val requestPermissionLauncher = registerForActivityResult(
		ActivityResultContracts.RequestPermission(),
	) { isGranted: Boolean ->
		if (isGranted) {
			// FCM SDK (and your app) can post notifications.
		} else {
			// TODO: Inform user that that your app will not show notifications.
		}
	}

	private fun askNotificationPermission() {
		// This is only necessary for API level >= 33 (TIRAMISU)
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
			if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) ==
				PackageManager.PERMISSION_GRANTED
			) {
				// FCM SDK (and your app) can post notifications.
			} else if (shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS)) {
				// TODO: display an educational UI explaining to the user the features that will be enabled
				//       by them granting the POST_NOTIFICATION permission. This UI should provide the user
				//       "OK" and "No thanks" buttons. If the user selects "OK," directly request the permission.
				//       If the user selects "No thanks," allow the user to continue without notifications.
			} else {
				// Directly ask for the permission
				requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
			}
		}
	}
}