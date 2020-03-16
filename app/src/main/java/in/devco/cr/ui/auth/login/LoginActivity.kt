package `in`.devco.cr.ui.auth.login

import `in`.devco.cr.R
import `in`.devco.cr.base.BaseActivity
import `in`.devco.cr.ui.auth.register.SignUpActivity
import android.content.Context
import android.content.Intent
import butterknife.OnClick

class LoginActivity : BaseActivity() {
    companion object {
        fun launch(context: Context) {
            context.startActivity(Intent(context, LoginActivity::class.java))
        }
    }

    override fun layoutRes() = R.layout.activity_login

    override fun init() {
    }

    @OnClick(R.id.noAccountButton)
    fun createAccount() {
        SignUpActivity.launch(this)
    }
}
