package com.ias.gsscore.fcm

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_IMMUTABLE
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.ias.gsscore.R
import com.ias.gsscore.ui.activity.LoginActivity
import com.ias.gsscore.ui.activity.NotificationActivity
import com.ias.gsscore.utils.Preferences
import me.leolin.shortcutbadger.ShortcutBadger
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL


class MyFirebaseMessagingService : FirebaseMessagingService() {
    var intent: Intent? = null

    override fun onMessageReceived(remoteMessage: RemoteMessage) {

        if (remoteMessage.data.isNotEmpty()) {

            Log.d(TAG, "Message data payload: ${remoteMessage.notification}")
            sendNotification(remoteMessage.notification!!.icon!!,remoteMessage.notification!!.title!!,remoteMessage.notification!!.body!!)
        }


    }

    override fun onNewToken(token: String) {
        Log.d(TAG, "Refreshed token: $token")
        Preferences.getInstance(this).fcmToken = token
        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // FCM registration token to your app server.
        sendRegistrationToServer(token)
    }


    private fun sendRegistrationToServer(token: String?) {
        // TODO: Implement this method to send token to your app server.
        Log.d(TAG, "sendRegistrationTokenToServer($token)")
    }

    private fun sendNotification(image:String,title:String,body: String) {


        if (Preferences.getInstance(this).isLogin) {
            intent = Intent(applicationContext, NotificationActivity::class.java)
            intent?.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        } else {
            intent = Intent(this, LoginActivity::class.java)
            intent?.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

        }

        val pendingIntent = PendingIntent.getActivity(
            this, 0  , intent,
            PendingIntent.FLAG_ONE_SHOT or  FLAG_IMMUTABLE
        )
        val channelId = getString(R.string.default_notification_channel_id)
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.mipmap.app_icon)
            .setContentTitle(title)
            .setContentText(body)
            .setAutoCancel(true)
            .setBadgeIconType(NotificationCompat.BADGE_ICON_SMALL)
            .setSound(defaultSoundUri)
            .setStyle( NotificationCompat.BigPictureStyle()
                .bigPicture(getBitmapfromUrl(image)!!))
            .setContentIntent(pendingIntent)
        val badgeCount = 1
        ShortcutBadger.applyCount(this, badgeCount)
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Channel human readable title",
                NotificationManager.IMPORTANCE_HIGH
            )
            channel.setShowBadge(true)
            notificationBuilder.setChannelId(channelId)
            notificationManager.createNotificationChannel(channel)

        }

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build())
    }

    companion object {

        private const val TAG = "MyFirebaseMsgService"
    }

    fun getBitmapfromUrl(imageUrl: String?): Bitmap? {
        return try {
            val url = URL(imageUrl)
            val connection: HttpURLConnection = url.openConnection() as HttpURLConnection
            connection.setDoInput(true)
            connection.connect()
            val input: InputStream = connection.getInputStream()
            BitmapFactory.decodeStream(input)
        } catch (e: Exception) {
            // TODO Auto-generated catch block
            e.printStackTrace()
            null
        }
    }


}