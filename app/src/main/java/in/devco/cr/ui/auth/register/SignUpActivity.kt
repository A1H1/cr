package `in`.devco.cr.ui.auth.register

import `in`.devco.cr.R
import `in`.devco.cr.base.BaseMVVMActivity
import `in`.devco.cr.data.model.User
import `in`.devco.cr.ui.home.HomeActivity
import `in`.devco.cr.util.AppConst.INPUT_ERROR_EMAIL
import `in`.devco.cr.util.AppConst.INPUT_ERROR_NAME
import `in`.devco.cr.util.AppConst.INPUT_ERROR_PASSWORD
import `in`.devco.cr.util.AppConst.INPUT_ERROR_PHONE
import `in`.devco.cr.util.SharedPref.setUser
import android.content.Context
import android.content.Intent
import butterknife.OnClick
import kotlinx.android.synthetic.main.button_action.*
import kotlinx.android.synthetic.main.input_email.*
import kotlinx.android.synthetic.main.input_name.*
import kotlinx.android.synthetic.main.input_password.*
import kotlinx.android.synthetic.main.input_phone.*
import kotlinx.android.synthetic.main.toolbar_common.*

class SignUpActivity : BaseMVVMActivity<User, SignUpViewModel>() {
    companion object {
        fun launch(context: Context) {
            context.startActivity(Intent(context, SignUpActivity::class.java))
        }
    }

    override fun layoutRes() = R.layout.activity_sign_up

    override fun init() {
        super.init()
        setActivityTitle("")
        toolbarTitleText.setText(R.string.register)
        actionTextView.setText(R.string.sign_up)
    }

    @OnClick(R.id.toolbarActionButton, R.id.alreadyHaveAccountButton)
    fun goBack() {
        onBackPressed()
    }

    @OnClick(R.id.actionButton)
    fun login() {
        resetError()

        viewModel.register(
            nameEditText.text.toString(),
            emailEditText.text.toString(),
            phoneEditText.text.toString(),
            passwordEditText.text.toString()
        )
    }

    override fun setData(data: User) {
        setUser(data).apply {
            HomeActivity.launch(this@SignUpActivity, true).apply {
                finish()
            }
        }
    }

    override fun inputError(code: Int) {
        when (code) {
            INPUT_ERROR_NAME -> nameInputLayout.error = getString(R.string.invalid_name)
            INPUT_ERROR_EMAIL -> emailInputLayout.error = getString(R.string.invalid_email)
            INPUT_ERROR_PHONE -> phoneInputLayout.error = getString(R.string.invalid_phone)
            INPUT_ERROR_PASSWORD -> passwordInputLayout.error = getString(R.string.invalid_password)
        }
    }

    private fun resetError() {
        nameInputLayout.isErrorEnabled = false
        emailInputLayout.isErrorEnabled = false
        phoneInputLayout.isErrorEnabled = false
        passwordInputLayout.isErrorEnabled = false
    }
}
