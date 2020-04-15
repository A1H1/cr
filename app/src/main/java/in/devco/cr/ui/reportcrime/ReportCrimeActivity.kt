package `in`.devco.cr.ui.reportcrime

import `in`.devco.cr.R
import `in`.devco.cr.base.BaseMVVMActivity
import `in`.devco.cr.data.model.ReportCrime
import `in`.devco.cr.data.model.User
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.button_action.*

class ReportCrimeActivity : BaseMVVMActivity<ReportCrime, ReportCrimeViewModel>()  {
    companion object{
        fun launch(context: Context){
            context.startActivity(Intent(context, ReportCrimeActivity::class.java))
        }
    }

    override fun layoutRes()= R.layout.activity_report_crime

    override fun init() {
        super.init()
        actionTextView.setText(R.string.report)
    }

    override fun setData(data: ReportCrime) {

    }
}
