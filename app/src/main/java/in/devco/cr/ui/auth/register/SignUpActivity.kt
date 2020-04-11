package `in`.devco.cr.ui.auth.register

import `in`.devco.cr.R
import `in`.devco.cr.base.BaseMVVMActivity
import `in`.devco.cr.data.model.User
import android.content.Context
import android.content.Intent
import butterknife.OnClick
import kotlinx.android.synthetic.main.button_action.*
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
        toolbarTitleText.setText(R.string.register)
        actionTextView.setText(R.string.sign_up)
    }

    @OnClick(R.id.toolbarActionButton, R.id.alreadyHaveAccountButton)
    fun goBack() {
        onBackPressed()
    }

    override fun setData(data: User) {

    }
}
