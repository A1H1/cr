package `in`.devco.cr.ui.auth.login

import `in`.devco.cr.R
import `in`.devco.cr.base.BaseMVVMActivity
import `in`.devco.cr.data.model.User
import `in`.devco.cr.ui.auth.register.SignUpActivity
import android.content.Context
import android.content.Intent
import butterknife.OnClick

class LoginActivity : BaseMVVMActivity<User, LoginViewModel>() {
    companion object {
        fun launch(context: Context) {
            context.startActivity(Intent(context, LoginActivity::class.java))
        }
    }

    override fun layoutRes() = R.layout.activity_login

    @OnClick(R.id.noAccountButton)
    fun createAccount() {
        SignUpActivity.launch(this)
    }

    override fun setData(data: User) {

    }
}
