package `in`.devco.cr.ui.auth.login

import `in`.devco.cr.R
import `in`.devco.cr.base.BaseMVVMActivity
import `in`.devco.cr.data.model.User
import `in`.devco.cr.ui.auth.register.SignUpActivity
import `in`.devco.cr.ui.home.HomeActivity
import `in`.devco.cr.util.AppConst.INPUT_ERROR_EMAIL
import `in`.devco.cr.util.AppConst.INPUT_ERROR_PASSWORD
import `in`.devco.cr.util.SharedPref.setUser
import android.content.Context
import android.content.Intent
import butterknife.OnClick
import kotlinx.android.synthetic.main.input_email.*
import kotlinx.android.synthetic.main.input_password.*

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

    @OnClick(R.id.actionButton)
    fun login() {
        resetError()
        viewModel.login(emailEditText.text.toString(), passwordEditText.text.toString())
    }

    override fun setData(data: User) {
        setUser(data).apply {
            HomeActivity.launch(this@LoginActivity, true).apply {
                finish()
            }
        }
    }

    override fun inputError(code: Int) {
        when (code) {
            INPUT_ERROR_EMAIL -> emailInputLayout.error = getString(R.string.invalid_email)
            INPUT_ERROR_PASSWORD -> passwordInputLayout.error = getString(R.string.invalid_password)
        }
    }

    private fun resetError() {
        emailInputLayout.isErrorEnabled = false
        passwordInputLayout.isErrorEnabled = false
    }
}
