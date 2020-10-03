package `in`.devco.cr.ui.crimelist

import `in`.devco.cr.R
import `in`.devco.cr.base.BaseMVVMActivity
import `in`.devco.cr.data.model.Report
import android.app.Activity
import android.content.Intent
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_all_reports.*

class CrimeListActivity : BaseMVVMActivity<List<Report>, CrimeListViewModel>(),
    ReportAdapter.AdapterListener {
    companion object {
        const val REQUEST_CODE = 701
        const val EXTRA_USER_ID = "EXTRA_USER_ID"

        fun launch(activity: Activity) {
            val intent = Intent(activity, CrimeListActivity::class.java)
            activity.startActivityForResult(intent, REQUEST_CODE)
        }
    }

    private val adapter = ReportAdapter()
    private lateinit var reporterId: String

    override fun layoutRes() = R.layout.activity_all_reports

    override fun init() {
        super.init()
        isDisplayHomeAsUpEnabled = true

        adapter.setListener(this)
        recyclerview.layoutManager = LinearLayoutManager(this)
        recyclerview.adapter = adapter

        viewModel.getAllReport()
    }

    override fun observable() {
        super.observable()

        viewModel.tracking.observe(this, Observer { response ->
            loading(response.isLoading)
            response.response?.let {
                if (it) {
                    val intent = Intent()
                    intent.putExtra(EXTRA_USER_ID, reporterId)
                    setResult(Activity.RESULT_OK, intent)
                    finish()
                }
            }
            response.exception?.let { error(it) }
        })

        viewModel.alert.observe(this, Observer { response ->
            loading(response.isLoading)
            response.response?.let {
                if (it) {
                    Toast.makeText(this, R.string.all_users_alerted, Toast.LENGTH_LONG).show()
                }
            }
            response.exception?.let { error(it) }
        })
    }

    override fun setData(data: List<Report>) {
        adapter.setList(data)
    }

    override fun accept(report: Report) {
        reporterId = report.userId
        viewModel.startTracking(getUser()?.userId.orEmpty(), report.userId, report.id)
    }

    override fun alert(reportId: String) {
        viewModel.alert(reportId)
    }
}
