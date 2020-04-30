package `in`.devco.cr.ui.splash

import `in`.devco.cr.R
import `in`.devco.cr.base.BaseActivity
import `in`.devco.cr.ui.auth.login.LoginActivity
import `in`.devco.cr.ui.home.HomeActivity
import android.os.Handler

class  SplashActivity : BaseActivity() {
    override fun layoutRes() = R.layout.activity_splash_screen

    override fun init() {
        Handler().postDelayed({
            if (isLoggedIn()) {
                HomeActivity.launch(this).apply { finish() }
            } else {
                LoginActivity.launch(this).apply { finish() }
            }
        }, 3000)
    }
}
