package `in`.devco.cr.ui

import `in`.devco.cr.R
import `in`.devco.cr.base.BaseActivity
import `in`.devco.cr.ui.auth.login.LoginActivity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler

class SplashScreen : BaseActivity() {
    override fun layoutRes()=R.layout.activity_splash_screen
    private val SPLASH_TIME_OUT:Long = 3000

    override fun init() {

        Handler().postDelayed(
            {
                LoginActivity.launch(this).apply {finish()}
            }, SPLASH_TIME_OUT)

    }


}
