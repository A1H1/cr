package `in`.devco.cr.ui.auth.register

import `in`.devco.cr.base.BaseViewModel
import `in`.devco.cr.data.model.DataWrapper
import `in`.devco.cr.data.model.ErrorResponse
import `in`.devco.cr.data.model.User
import `in`.devco.cr.data.repository.UserRepository
import `in`.devco.cr.util.AppConst.INPUT_ERROR_EMAIL
import `in`.devco.cr.util.AppConst.INPUT_ERROR_NAME
import `in`.devco.cr.util.AppConst.INPUT_ERROR_PASSWORD
import `in`.devco.cr.util.AppConst.INPUT_ERROR_PHONE
import `in`.devco.cr.util.AppUtils.isValidEmail
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class SignUpViewModel @Inject constructor(private val repository: UserRepository) :
    BaseViewModel<User>() {

    fun register(name: String, email: String, phone: String, password: String) {
        data.postValue(DataWrapper(isLoading = true))

        if (name.length < 3) {
            data.postValue(DataWrapper(inputError = INPUT_ERROR_NAME))
            return
        } else if (!isValidEmail(email)) {
            data.postValue(DataWrapper(inputError = INPUT_ERROR_EMAIL))
            return
        } else if (phone.length < 10) {
            data.postValue(DataWrapper(inputError = INPUT_ERROR_PHONE))
            return
        } else if (password.length < 6) {
            data.postValue(DataWrapper(inputError = INPUT_ERROR_PASSWORD))
            return
        }

        val body = HashMap<String, String>()
        body["name"] = name
        body["email"] = email
        body["phone"] = phone
        body["password"] = password

        disposable.add(
            repository
                .signUp(body)
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