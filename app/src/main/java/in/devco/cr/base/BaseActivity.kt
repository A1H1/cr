package `in`.devco.cr.base

import `in`.devco.cr.util.AppUtils.displaySnackBar
import `in`.devco.cr.util.SharedPref.getUser
import android.os.Bundle
import androidx.annotation.LayoutRes
import butterknife.ButterKnife
import dagger.android.support.DaggerAppCompatActivity

abstract class BaseActivity : DaggerAppCompatActivity() {
    @LayoutRes
    protected abstract fun layoutRes(): Int
    protected open fun init() {}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layoutRes())
        ButterKnife.bind(this)

        init()
    }

    fun displayMessage(message: String) {
        displaySnackBar(findViewById(android.R.id.content), message)
    }

    fun isLoggedIn(): Boolean {
        return getUser() != null
    }
}