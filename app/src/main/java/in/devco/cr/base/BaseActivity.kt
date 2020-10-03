package `in`.devco.cr.base

import `in`.devco.cr.R
import `in`.devco.cr.data.model.UserTye
import `in`.devco.cr.util.AppUtils.displaySnackBar
import `in`.devco.cr.util.SharedPref
import android.os.Bundle
import android.view.MenuItem
import androidx.annotation.LayoutRes
import androidx.appcompat.widget.Toolbar
import butterknife.ButterKnife
import dagger.android.support.DaggerAppCompatActivity

abstract class BaseActivity : DaggerAppCompatActivity() {
    @LayoutRes
    protected abstract fun layoutRes(): Int
    protected open fun init() {}
    protected var toolbar: Toolbar? = null

    var isPolice = getUser()?.userTye == UserTye.POLICE

    var isDisplayHomeAsUpEnabled: Boolean
        get() = false
        set(value) {
            supportActionBar?.setDisplayHomeAsUpEnabled(value)
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layoutRes())
        ButterKnife.bind(this)

        setToolbar()
        init()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> onBackPressed()
        }
        return true
    }

    private fun setToolbar() {
        toolbar = findViewById(R.id.toolbar)

        setSupportActionBar(toolbar)

        if (isDisplayHomeAsUpEnabled) {
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }
    }

    fun setActivityTitle(title: Int) {
        supportActionBar?.setTitle(title)
    }

    fun setActivityTitle(title: String) {
        supportActionBar?.title = title
    }

    fun displayMessage(message: String) {
        displaySnackBar(findViewById(android.R.id.content), message)
    }

    fun isLoggedIn(): Boolean {
        return getUser() != null
    }

    fun getUser() = SharedPref.getUser()
}