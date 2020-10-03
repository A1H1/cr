package `in`.devco.cr.util

import `in`.devco.cr.CRApplication
import `in`.devco.cr.data.model.User
import android.content.Context.MODE_PRIVATE
import com.google.gson.Gson

object SharedPref {
    private const val PREF_FILE = "pref_file"
    private const val PREF_USER = "pref_user"
    private const val PREF_FCM_TOKEN = "pref_fcm_token"
    private const val PREF_TRACKING_USER = "pref_tracking_user"

    fun setUser(user: User) {
        val pref = CRApplication
            .instance
            .applicationContext
            .getSharedPreferences(
                PREF_FILE,
                MODE_PRIVATE
            )
            .edit()

        pref.putString(PREF_USER, Gson().toJson(user, User::class.java))
        pref.apply()
    }

    fun getUser(): User? {
        val pref = CRApplication
            .instance
            .applicationContext
            .getSharedPreferences(
                PREF_FILE,
                MODE_PRIVATE
            )

        return Gson().fromJson(pref.getString(PREF_USER, ""), User::class.java)
    }

    fun logout() {
        val pref = CRApplication
            .instance
            .applicationContext
            .getSharedPreferences(
                PREF_FILE,
                MODE_PRIVATE
            )
            .edit()

        pref.remove(PREF_USER)
        pref.apply()
    }

    fun setFCMToken(token: String) {
        val pref = CRApplication
            .instance
            .applicationContext
            .getSharedPreferences(
                PREF_FILE,
                MODE_PRIVATE
            )
            .edit()

        pref.putString(PREF_FCM_TOKEN, token)
        pref.apply()
    }

    fun getFCMToken(): String? {
        val pref = CRApplication
            .instance
            .applicationContext
            .getSharedPreferences(
                PREF_FILE,
                MODE_PRIVATE
            )

        return pref.getString(PREF_FCM_TOKEN, "")
    }

    fun setTrackingUser(userId: String) {
        val pref = CRApplication
            .instance
            .applicationContext
            .getSharedPreferences(
                PREF_FILE,
                MODE_PRIVATE
            )
            .edit()

        pref.putString(PREF_TRACKING_USER, userId)
        pref.apply()
    }

    fun getTrackingUser(): String? {
        val pref = CRApplication
            .instance
            .applicationContext
            .getSharedPreferences(
                PREF_FILE,
                MODE_PRIVATE
            )

        return pref.getString(PREF_TRACKING_USER, "")
    }

    fun clearTrackingUser() {
        val pref = CRApplication
            .instance
            .applicationContext
            .getSharedPreferences(
                PREF_FILE,
                MODE_PRIVATE
            )
            .edit()

        pref.remove(PREF_TRACKING_USER)
        pref.apply()
    }
}