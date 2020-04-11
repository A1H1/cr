package `in`.devco.cr.di.module

import `in`.devco.cr.ui.SplashScreen
import `in`.devco.cr.ui.auth.login.LoginActivity
import `in`.devco.cr.ui.auth.register.SignUpActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBindingModule {
    @ContributesAndroidInjector
    abstract fun loginActivity(): LoginActivity


    @ContributesAndroidInjector
    abstract fun signUpActivity(): SignUpActivity

    @ContributesAndroidInjector
    abstract fun splashScreen(): SplashScreen
}