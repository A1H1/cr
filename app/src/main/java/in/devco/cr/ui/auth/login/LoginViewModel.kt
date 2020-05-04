package `in`.devco.cr.ui.auth.login

import `in`.devco.cr.base.BaseViewModel
import `in`.devco.cr.data.model.DataWrapper
import `in`.devco.cr.data.model.ErrorResponse
import `in`.devco.cr.data.model.User
import `in`.devco.cr.data.repository.UserRepository
import `in`.devco.cr.util.AppConst.INPUT_ERROR_EMAIL
import `in`.devco.cr.util.AppConst.INPUT_ERROR_PASSWORD
import `in`.devco.cr.util.AppUtils.isValidEmail
import `in`.devco.cr.util.SharedPref.getFCMToken
import io.reactivex.schedulers.Schedulers
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

class LoginViewModel @Inject constructor(private val repository: UserRepository) :
    BaseViewModel<User>() {
    fun login(email: String, password: String) {
        data.postValue(DataWrapper(isLoading = true))

        if (!isValidEmail(email)) {
            data.postValue(DataWrapper(inputError = INPUT_ERROR_EMAIL))
            return
        } else if (password.length < 6) {
            data.postValue(DataWrapper(inputError = INPUT_ERROR_PASSWORD))
            return
        }

        disposable.add(
            repository
                .login(email, password)
                .subscribeOn(Schedulers.io())
                .subscribe({ response ->
                    if (response.code == 200) {
                        data.postValue(DataWrapper(response = response.data))
                    } else {
                        response.message?.let {
                            data.postValue(DataWrapper(error = ErrorResponse(response.code, it)))
                        } ?: kotlin.run {
                            data.postValue(DataWrapper(errorCode = response.code))
                        }
                    }
                }, {
                    data.postValue(DataWrapper(exception = it))
                })
        )
    }
}