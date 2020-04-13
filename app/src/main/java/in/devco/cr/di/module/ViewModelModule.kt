package `in`.devco.cr.di.module

import `in`.devco.cr.ui.auth.login.LoginViewModel
import `in`.devco.cr.ui.auth.register.SignUpViewModel
import `in`.devco.cr.util.ViewModelFactory
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import `in`.devco.cr.util.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelModule {
    @Binds
    abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(LoginViewModel::class)
    abstract fun loginViewModel(loginViewModel: LoginViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SignUpViewModel::class)
    abstract fun signUpViewModel(signUpViewModel: SignUpViewModel): ViewModel
}