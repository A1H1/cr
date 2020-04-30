package `in`.devco.cr.util

import android.view.View
import com.google.android.material.snackbar.Snackbar
import java.util.regex.Pattern

object AppUtils {
    fun displaySnackBar(view: View, message: Int) {
        Snackbar.make(view, message, Snackbar.LENGTH_LONG).show()
    }

    fun displaySnackBar(view: View, message: String) {
        Snackbar.make(view, message, Snackbar.LENGTH_LONG).show()
    }

    fun isValidEmail(email: String?): Boolean {
        val emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\." +
                "[a-zA-Z0-9_+&*-]+)*@" +
                "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                "A-Z]{2,7}$"
        val pat: Pattern = Pattern.compile(emailRegex)
        return if (email == null) false else pat.matcher(email).matches()
    }
}