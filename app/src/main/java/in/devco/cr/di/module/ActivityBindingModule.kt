package `in`.devco.cr.di.module

import `in`.devco.cr.ui.auth.login.LoginActivity
import `in`.devco.cr.ui.auth.register.SignUpActivity
import `in`.devco.cr.ui.home.HomeActivity
import `in`.devco.cr.ui.reportcrime.ReportCrimeActivity
import `in`.devco.cr.ui.splash.SplashActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBindingModule {
    @ContributesAndroidInjector
    abstract fun loginActivity(): LoginActivity

    @ContributesAndroidInjector
    abstract fun signUpActivity(): SignUpActivity

    @ContributesAndroidInjector
    abstract fun splashScreen(): SplashActivity

    @ContributesAndroidInjector
    abstract fun reportCrimeActivity(): ReportCrimeActivity

    @ContributesAndroidInjector
    abstract fun homeActivity(): HomeActivity
}