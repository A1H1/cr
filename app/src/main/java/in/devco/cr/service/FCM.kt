package `in`.devco.cr.service

import `in`.devco.cr.R
import `in`.devco.cr.data.remote.API
import `in`.devco.cr.ui.home.HomeActivity
import `in`.devco.cr.util.SharedPref.setFCMToken
import `in`.devco.cr.util.SharedPref.setTrackingUser
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import dagger.android.AndroidInjection
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

class FCM : FirebaseMessagingService() {
    @Inject
    lateinit var mApiService: API
    private lateinit var mNotificationManager: NotificationManager

    override fun onCreate() {
        AndroidInjection.inject(this)
        super.onCreate()

        mNotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)

        if (message.data["type"] == "start tracking") {
            setTrackingUser(message.data["currUser"].orEmpty())
        } else {
            sendNotification(message)
        }
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        setFCMToken(token)

        mApiService.updateUserFCM(token).enqueue(object : Callback<Void> {
            override fun onFailure(call: Call<Void>, t: Throwable) {}
            override fun onResponse(call: Call<Void>, response: Response<Void>) {}
        })
    }

    private fun sendNotification(notification: RemoteMessage) {
        val notificationBuilder = NotificationCompat.Builder(this, "1")
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle(getString(R.string.crime_alert))
            .setContentText(notification.data["title"])
            .setAutoCancel(true)

        notificationBuilder.setContentIntent(
            PendingIntent.getActivity(
                this,
                0,
                Intent(this, HomeActivity::class.java),
                PendingIntent.FLAG_UPDATE_CURRENT
            )
        )

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "1",
                "channel1",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            channel.lightColor = Color.GREEN
            channel.lockscreenVisibility = Notification.VISIBILITY_PRIVATE
            mNotificationManager.createNotificationChannel(channel)
        }

        mNotificationManager.notify(1, notificationBuilder.build())
    }
}