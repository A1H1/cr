package in.devco.cr.di.module;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import in.devco.cr.service.FCM;

@Module
public abstract class ServiceBindingModule {

    @ContributesAndroidInjector
    abstract FCM fcm();
}
