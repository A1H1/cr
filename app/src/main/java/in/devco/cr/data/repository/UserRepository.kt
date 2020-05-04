package `in`.devco.cr.data.repository

import `in`.devco.cr.data.remote.API
import `in`.devco.cr.di.module.NetworkModule.provideClient
import `in`.devco.cr.di.module.NetworkModule.provideService
import javax.inject.Inject

class UserRepository @Inject constructor() {
    private var service: API = provideService(provideClient())

    fun login(email: String, password: String) = service.login(email, password)

    fun signUp(data: Map<String, String>) = service.signUp(data)

    fun updateFCMToken(token: String) = service.updateUserFCM(token)
}