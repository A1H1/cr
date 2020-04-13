package `in`.devco.cr.data.repository

import `in`.devco.cr.data.remote.API
import javax.inject.Inject

class UserRepository @Inject constructor(private val service: API) {
    fun login(email: String, password: String) = service.login(email, password)

    fun signUp(data: Map<String, String>) = service.signUp(data)
}