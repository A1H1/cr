package `in`.devco.cr.di

import `in`.devco.cr.CRApplication
import `in`.devco.cr.di.module.ActivityBindingModule
import `in`.devco.cr.di.module.ApplicationModule
import `in`.devco.cr.di.module.ContextModule
import `in`.devco.cr.di.module.ViewModelModule
import android.app.Application
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import dagger.android.support.DaggerApplication
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        ContextModule::class,
        ApplicationModule::class,
        AndroidSupportInjectionModule::class,
        ActivityBindingModule::class,
        ViewModelModule::class
    ]
)
interface ApplicationComponent : AndroidInjector<DaggerApplication> {
    fun inject(application: CRApplication)

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application?): Builder?

        fun build(): ApplicationComponent?
    }
}