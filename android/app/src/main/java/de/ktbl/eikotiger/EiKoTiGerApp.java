package de.ktbl.eikotiger;

import android.app.Activity;
import android.app.Application;

import javax.inject.Inject;

import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasActivityInjector;
import de.ktbl.eikotiger.di.AppInjector;
import timber.log.Timber;

/**
 * Custom Application class for our application to bind application instance to Dagger 2 graph
 * Requires update of AndroidManifest.xml to set name attribute for this class
 */
public class EiKoTiGerApp extends Application implements HasActivityInjector {

    private static final String TAG = EiKoTiGerApp.class.getSimpleName();

    @Inject
    DispatchingAndroidInjector<Activity> dispatchingAndroidInjector;

    @Override
    public DispatchingAndroidInjector<Activity> activityInjector() {
        return dispatchingAndroidInjector;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
            Timber.tag(TAG).w("App runs in debug mode!");
            Timber.tag(TAG).i("Created Timber DebugTree.");
        }

        AppInjector.init(this);
    }

}
