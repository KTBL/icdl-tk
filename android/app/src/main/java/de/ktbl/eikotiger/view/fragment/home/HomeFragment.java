package de.ktbl.eikotiger.view.fragment.home;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import de.ktbl.android.sharedlibrary.view.fragment.AbstractBaseFragment;
import de.ktbl.eikotiger.R;
import de.ktbl.eikotiger.view.viewmodel.home.HomeViewModel;
import timber.log.Timber;

public class HomeFragment extends AbstractBaseFragment {
    private final String TAG = HomeFragment.class.getSimpleName();

    @SuppressWarnings("FieldCanBeLocal")
    private HomeViewModel mViewModel;

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    // TODO: Check whats the specific difference betweend onActivityCreated and onViewCreated! ~ KS
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // If the App is started first time, or the Apps data has been reset,
        // then we have to download all needed data first and initialize the
        // database. Therefore we use the SplashScreenFragment.
        boolean isUninitiated = this.checkIfUninitiated();
        boolean hasSplashBeenShown = this.checkIfSplashHasBeenShown();
        if(isUninitiated && !hasSplashBeenShown){
            this.navigateToSplashScreen();
        }

        mViewModel = new ViewModelProvider(this).get(HomeViewModel.class);


    }

    private void navigateToSplashScreen() {
        // Navigate to the Splashscreen
        // When showing the Splashscreen the Appbar needs to be hidden
        // and the Drawer needs to be locked
        // Both actions, hiding the bar and locking the Drawer, as well as
        // showing the Bar again and unlocking the Drawer will be managed in
        // onNavigated of the Activity hosting this Fragment.
        Activity activity = this.getActivity();
        if(activity == null){
            NullPointerException npException = new NullPointerException("this.getActivity returned null");
            Timber.tag(TAG).e(npException,"Activity is null, but needed to find NavController!");
            throw npException;

        }
        NavController navController = Navigation.findNavController(activity,
                                                                   R.id.main_nav_host_fragment);
        navController.navigate(R.id.action_global_splashScreenFragment);
        this.setHasSplashBeenShown(true);
    }

    /**
     *
     * @param hasBeenShown
     */
    private void setHasSplashBeenShown(boolean hasBeenShown) {
        SharedPreferences sharedPreferences = this.getPrivateSharedPrefs();
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(getString(R.string.splash_shown), hasBeenShown);
        editor.apply();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.home_fragment, container, false);
    }

    @Override public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button btn = view.findViewById(R.id.nav_to_splash);
        btn.setOnClickListener(b -> this.navigateToSplashScreen());
    }

    /**
     *
     * @return
     */
    private boolean checkIfUninitiated() {
        return !this.getPrivateSharedPrefs().contains(getString(R.string.db_initiated));
    }

    /**
     *
     * @return
     */
    private boolean checkIfSplashHasBeenShown() {
        return this.getPrivateSharedPrefs()
                   .contains(getString(R.string.splash_shown));
    }

    /**
     * @return
     */
    private SharedPreferences getPrivateSharedPrefs() {
        return this.getActivity()
                   .getPreferences(Context.MODE_PRIVATE);
    }

    @Override
    protected int getNavHostId() {
        return R.id.main_nav_host_fragment;
    }
}
