package de.ktbl.eikotiger.view.fragment.home;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import javax.inject.Inject;

import de.ktbl.eikotiger.R;
import de.ktbl.eikotiger.databinding.FragmentSplashScreenBinding;
import de.ktbl.eikotiger.di.Injectable;
import de.ktbl.eikotiger.view.viewmodel.home.SplashscreenVM;


/**
 * A fragment with a Google +1 button.
 */
public class SplashScreenFragment extends Fragment implements Injectable {

    @Inject
    ViewModelProvider.Factory viewModelFactory;

    SplashscreenVM splashscreenVM;

    public SplashScreenFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_splash_screen, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View rootView, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(rootView, savedInstanceState);
        FragmentSplashScreenBinding binding = FragmentSplashScreenBinding.bind(rootView);
        SplashscreenVM vm = new ViewModelProvider(this, viewModelFactory).get(SplashscreenVM.class);
        //SplashscreenVM vm = new ViewModelProvider(this).get(SplashscreenVM.class);
        binding.setLifecycleOwner(getViewLifecycleOwner());
        binding.setVm(vm);
    }


    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onPause() {
        super.onPause();
    }


}
