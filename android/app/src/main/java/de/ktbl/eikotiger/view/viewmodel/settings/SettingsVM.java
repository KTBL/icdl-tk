package de.ktbl.eikotiger.view.viewmodel.settings;

import androidx.navigation.NavDirections;

import de.ktbl.android.sharedlibrary.navigation.NavigationCommand;
import de.ktbl.android.sharedlibrary.view.viewmodel.AbstractBaseViewModel;
import de.ktbl.eikotiger.R;
import de.ktbl.eikotiger.view.activity.SettingsActivityDirections;
import de.ktbl.eikotiger.view.fragment.settings.SettingsOverviewFragment;
import de.ktbl.eikotiger.view.fragment.settings.SettingsOverviewFragmentDirections;

public class SettingsVM extends AbstractBaseViewModel {

    public void onBusinessInformationClick() {
        //controller.navigate(R.id.actionSettingsToBusinessInfo);
        NavDirections action =
                SettingsOverviewFragmentDirections.actionSettingsToBusinessInfo();
        this.navigationEventHandler.notifyLiveEvent(new NavigationCommand(action));
    }
}
