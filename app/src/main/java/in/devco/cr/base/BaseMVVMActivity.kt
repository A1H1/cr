package `in`.devco.cr.base

import `in`.devco.cr.util.ViewModelFactory
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.gson.JsonSyntaxException
import retrofit2.HttpException
import java.net.SocketTimeoutException
import javax.inject.Inject

abstract class BaseMVVMActivity<T, U : BaseViewModel<T>> : BaseActivity(), ErrorHandler {
    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    protected lateinit var viewModel: U
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
            response.exception?.let { error(it) }
            response.inputError?.let { inputError(it) }
        })
    }

    protected abstract fun setData(data: T)

    protected open fun loading(isLoading: Boolean) {
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

    override fun authError() {
    }

    override fun noInternet() {
    }

    override fun displayError() {
    }

    override fun serverError() {
    }

    override fun inputError(code: Int) {
    }
}