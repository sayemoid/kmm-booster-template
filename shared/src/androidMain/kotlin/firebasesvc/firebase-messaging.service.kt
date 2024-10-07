package firebasesvc

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import modules.common.di.getKoinInstance
import modules.common.features.notifications.models.NChannel
import modules.common.features.notifications.models.NDataKeys
import modules.common.features.notifications.models.NotificationData
import modules.common.features.preferences.PrefKeys.firebaseTokenKey
import modules.exampleModule.MainActivity
import utils.Tag
import utils.logD


class MyFirebaseMessagingService : FirebaseMessagingService() {
	private val job = SupervisorJob()
	private val scope = CoroutineScope(Dispatchers.IO + job)

	override fun onNewToken(token: String) {
		super.onNewToken(token)
		val dataStore = getKoinInstance<DataStore<Preferences>>()
		scope.launch {
			dataStore.updateData {
				val pref = it.toMutablePreferences()
				pref[firebaseTokenKey] = token
				pref
			}
		}

		logD(Tag.Notification.FirebaseServiceToken, token)
	}

	override fun onDestroy() {
		super.onDestroy()
		job.cancel()
	}

	inline fun <reified T> checkGenericType(value: String): Boolean {
		return value is T
	}

	override fun onMessageReceived(remoteMessage: RemoteMessage) {
		logD(Tag.Notification.FirebaseServiceToken, "From: ${remoteMessage.from}")

		val data = NotificationData(
			notificationId = (remoteMessage.data[NDataKeys.NOTIFICATION_ID.key]
				?: "0").toInt(),
			referenceId = (remoteMessage.data[NDataKeys.REFERENCE_ID.key])?.toLong(),
			referenceId2 = (remoteMessage.data[NDataKeys.REFERENCE_ID_2.key])?.toLong(),
			navigable = (remoteMessage.data[NDataKeys.NAVIGABLE.key]
				?: "false").toBoolean(),
			intentAction = remoteMessage.data[NDataKeys.INTENT_ACTION.key],
			channelId = remoteMessage.data[NDataKeys.CHANNEL_ID.key] ?: "default",
		)


		remoteMessage.notification?.let {
			logD(
				Tag.Notification.FirebaseServiceToken,
				"notification body: ${it.body}"
			)
			val channel = this.resolveChannel(data.channelId)
			createNotificationChannel(channel)
			// Register the channel with the system.
			val notificationManager: NotificationManager =
				getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
			val builder = NotificationCompat.Builder(this, channel.channelId)
				.setSmallIcon(android.R.drawable.sym_def_app_icon)
				.setContentTitle(it.title)
				.setContentText(it.body)
				.setStyle(
					NotificationCompat.BigTextStyle()
						.bigText(it.body)
				)
				.setPriority(NotificationCompat.PRIORITY_DEFAULT)
				.setVibrate(longArrayOf(500, 500, 500))
				.setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
				.setAutoCancel(true)

			if (data.navigable && (data.referenceId != null)) {
				builder.setContentIntent(
					createPollPendingIntent(
						context = this,
						action = data.intentAction,
						referenceId = data.referenceId,
						referenceId2 = data.referenceId2,
					)
				)
			}

			notificationManager.notify(data.notificationId, builder.build())
		}
		// Also if you intend on generating your own notifications as a result of a received FCM
		// message, here is where that should be initiated. See sendNotification method below.
	}

	private fun createPollPendingIntent(
		context: Context,
		action: String?,
		referenceId: Long,
		referenceId2: Long?,
	): PendingIntent {
		val intent = Intent(this, resolveClass(action))

		referenceId2?.let {
			intent.putExtra(NDataKeys.REFERENCE_ID_2.key, referenceId2)
		}
		intent.putExtra(NDataKeys.REFERENCE_ID.key, referenceId)

		intent.action = action ?: Intent.ACTION_MAIN
		intent.addCategory(Intent.CATEGORY_LAUNCHER)
		intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

		return PendingIntent.getActivity(
			context,
			referenceId.toInt(),
			intent,
			PendingIntent.FLAG_IMMUTABLE
		)
	}

	/*
	Do something with the action
	May be open a different page
	 */
	private fun resolveClass(action: String?) = MainActivity::class.java

	private fun resolveChannel(channelId: String): NChannel = channelId.let { cId ->
		val channels = HashSet<NChannel>()
		channels.firstOrNull { it.channelId == cId }
	} ?: NChannel(0, channelId, "Default", "", NotificationManager.IMPORTANCE_DEFAULT, setOf())

	private fun createNotificationChannel(channelDef: NChannel) {
		// Create the NotificationChannel, but only on API 26+ because
		// the NotificationChannel class is not in the Support Library.
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
			val channel = NotificationChannel(
				channelDef.channelId,
				channelDef.name,
				channelDef.priority
			).apply {
				description = channelDef.description
			}
			// Register the channel with the system.
			val notificationManager: NotificationManager =
				getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
			notificationManager.createNotificationChannel(channel)
		}
	}
}