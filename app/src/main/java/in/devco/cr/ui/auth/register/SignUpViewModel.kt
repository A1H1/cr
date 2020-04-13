package `in`.devco.cr.ui.auth.register

import `in`.devco.cr.base.BaseViewModel
import `in`.devco.cr.data.model.User
import `in`.devco.cr.data.repository.UserRepository
import javax.inject.Inject

class SignUpViewModel @Inject constructor(private val repository: UserRepository) :
    BaseViewModel<User>() {
}