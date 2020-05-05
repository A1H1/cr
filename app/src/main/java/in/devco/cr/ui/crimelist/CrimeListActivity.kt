package `in`.devco.cr.ui.crimelist

import `in`.devco.cr.R
import `in`.devco.cr.base.BaseMVVMActivity
import android.content.Context
import android.content.Intent

class CrimeListActivity : BaseMVVMActivity<Boolean, CrimeListViewModel>() {
    companion object {
        fun launch(context: Context) {
            val intent = Intent(context, CrimeListActivity::class.java)
            context.startActivity(intent)
        }
    }

    override fun layoutRes() = R.layout.activity_all_reports

    override fun init() {
        super.init()
        isDisplayHomeAsUpEnabled = true
    }

    override fun setData(data: Boolean) {

    }
}
