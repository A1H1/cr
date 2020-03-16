package `in`.devco.cr.ui.auth.register

import `in`.devco.cr.R
import `in`.devco.cr.base.BaseActivity
import android.content.Context
import android.content.Intent

class SignUpActivity : BaseActivity() {
    companion object {
        fun launch(context: Context) {
            context.startActivity(Intent(context, SignUpActivity::class.java))
        }
    }

    override fun layoutRes() = R.layout.activity_sign_up

    override fun init() {
    }
}
