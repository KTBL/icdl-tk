package de.ktbl.eikotiger.view.fragment.settings;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import de.ktbl.android.sharedlibrary.navigation.NavigationCommand;
import de.ktbl.android.sharedlibrary.view.fragment.AbstractBaseFragment;
import de.ktbl.eikotiger.R;
import de.ktbl.eikotiger.databinding.FragmentSettingsOverviewBinding;
import de.ktbl.eikotiger.view.viewmodel.settings.SettingsVM;

public class SettingsOverviewFragment extends AbstractBaseFragment {
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_settings_overview, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View rootView, @Nullable Bundle savedInstanceState) {
        FragmentSettingsOverviewBinding binding = FragmentSettingsOverviewBinding.bind(rootView);
        SettingsVM vm = new ViewModelProvider(this).get(SettingsVM.class);
        binding.setVm(vm);
        vm.observeNavigationRequests(this, this::onNavigationRequest);
    }

    @Override protected int getNavHostId() {
        return R.id.nav_host_fragment_settings;
    }

}
