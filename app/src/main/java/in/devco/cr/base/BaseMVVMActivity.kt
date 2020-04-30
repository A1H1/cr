package `in`.devco.cr.base

import `in`.devco.cr.R
import `in`.devco.cr.data.model.ErrorResponse
import `in`.devco.cr.util.AppUtils.displaySnackBar
import `in`.devco.cr.util.ViewModelFactory
import `in`.devco.cr.util.show
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.afollestad.materialdialogs.MaterialDialog
import com.google.gson.JsonSyntaxException
import retrofit2.HttpException
import java.net.SocketTimeoutException
import javax.inject.Inject

abstract class BaseMVVMActivity<T, U : BaseViewModel<T>> : BaseActivity(), ErrorHandler {
    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    @Inject
    protected lateinit var viewModel: U
    private lateinit var progressDialog: MaterialDialog

    override fun init() {
        super.init()
        viewModel = ViewModelProvider(this, viewModelFactory).get(viewModel.javaClass)
        observable()
    }

    protected open fun observable() {
        viewModel.response.observe(this, Observer { response ->
            loading(response.isLoading)
            response.response?.let { setData(it) }
            response.error?.let { error(it) }
            response.errorCode?.let { error(it) }
            response.exception?.let { error(it) }
            response.inputError?.let { inputError(it) }
        })
    }

    protected abstract fun setData(data: T)

    protected open fun loading(isLoading: Boolean) {
        if (::progressDialog.isInitialized) {
            progressDialog.show(isLoading)
        } else {
            initMaterialDialog()
        }
    }

    override fun error(throwable: Throwable) {
        if (throwable is SocketTimeoutException ||
            throwable is JsonSyntaxException ||
            throwable is HttpException
        ) {
            serverError()
        } else {
            noInternet()
        }
    }

    override fun error(code: Int) {
        when (code) {
            403, 422 -> authError()
            500 -> serverError()
            else -> displayError()
        }
    }

    override fun error(error: ErrorResponse) {
        displayMessage(error.error)
    }

    override fun authError() {
        displaySnackBar(findViewById(android.R.id.content), R.string.auth_error)
    }

    override fun noInternet() {
        displaySnackBar(findViewById(android.R.id.content), R.string.no_internet)
    }

    override fun displayError() {
        displaySnackBar(findViewById(android.R.id.content), R.string.something_went_wrong)
    }

    override fun serverError() {
        displaySnackBar(findViewById(android.R.id.content), R.string.server_error)
    }

    override fun inputError(code: Int) {
    }

    private fun initMaterialDialog() {
        progressDialog = MaterialDialog.Builder(this)
            .content(R.string.please_wait)
            .cancelable(false)
            .progress(true, 0)
            .show()
    }
}