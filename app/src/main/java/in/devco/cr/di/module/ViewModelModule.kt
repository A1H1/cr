package `in`.devco.cr.di.module

import `in`.devco.cr.ui.auth.login.LoginViewModel
import `in`.devco.cr.ui.auth.register.SignUpViewModel
import `in`.devco.cr.ui.crimelist.CrimeListViewModel
import `in`.devco.cr.ui.home.HomeViewModel
import `in`.devco.cr.ui.reportcrime.ReportCrimeViewModel
import `in`.devco.cr.util.ViewModelFactory
import `in`.devco.cr.util.ViewModelKey
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelModule {
    @Binds
    internal abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(LoginViewModel::class)
    internal abstract fun loginViewModel(loginViewModel: LoginViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SignUpViewModel::class)
    internal abstract fun signUpViewModel(signUpViewModel: SignUpViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(HomeViewModel::class)
    internal abstract fun homeViewModel(homeViewModel: HomeViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ReportCrimeViewModel::class)
    internal abstract fun reportCrimeViewModel(reportCrimeViewModel: ReportCrimeViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(CrimeListViewModel::class)
    internal abstract fun crimeListViewModel(crimeListViewModel: CrimeListViewModel): ViewModel
}