package `in`.devco.cr

import `in`.devco.cr.di.ApplicationComponent
import `in`.devco.cr.di.DaggerApplicationComponent
import dagger.android.support.DaggerApplication

class CRApplication : DaggerApplication() {
    override fun applicationInjector(): ApplicationComponent? {
        val component: ApplicationComponent? =
            DaggerApplicationComponent.builder().application(this)?.build()
        component?.inject(this)

        return component
    }
}