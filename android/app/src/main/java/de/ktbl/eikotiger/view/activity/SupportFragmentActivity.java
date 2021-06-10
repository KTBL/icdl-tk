package de.ktbl.eikotiger.view.activity;


import androidx.fragment.app.Fragment;

import javax.inject.Inject;

import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.HasSupportFragmentInjector;
import de.ktbl.android.sharedlibrary.view.activity.AbstractBaseActivity;

public abstract class SupportFragmentActivity extends AbstractBaseActivity
        implements HasSupportFragmentInjector {

    @Inject
    DispatchingAndroidInjector<Fragment> dispatchingAndroidInjector;


    @Override
    public AndroidInjector<Fragment> supportFragmentInjector() {
        return dispatchingAndroidInjector;
    }

}
