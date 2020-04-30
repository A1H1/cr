package `in`.devco.cr.util

import `in`.devco.cr.CRApplication
import `in`.devco.cr.data.model.User
import android.content.Context.MODE_PRIVATE
import com.google.gson.Gson

object SharedPref {
    private const val PREF_FILE = "pref_file"
    private const val PREF_USER = "pref_user"

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
}