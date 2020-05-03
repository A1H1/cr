package `in`.devco.cr.service

import `in`.devco.cr.data.remote.API
import `in`.devco.cr.util.SharedPref.setFCMToken
import android.util.Log
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

    override fun onCreate() {
        AndroidInjection.inject(this)
        super.onCreate()
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        message.data.forEach {
            Log.e("test", "${it.key} = ${it.value}")
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
}