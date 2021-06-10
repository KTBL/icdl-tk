package de.ktbl.eikotiger.view.activity;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import dagger.android.AndroidInjection;
import de.ktbl.eikotiger.R;

public class SettingsActivity extends SupportFragmentActivity {

    public NavController getNavigationController() {
        return Navigation.findNavController(this, R.id.nav_host_fragment_settings);
    }

    private void onNavigated(NavController navController, NavDestination navDestination,
                             Bundle arguments) {
        //
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        boolean value = false;
        if (item.getItemId() == android.R.id.home) {
            value = this.getNavigationController().navigateUp();
        }
        // In case there's no possibility to move further upwards (value == false) in the
        // settings-nav-graph
        // we're going to hand over the moving decision to the default behaviour.
        // In this case the PARENT_ACTIVITY defined in the AndroidManifest should be the one
        // to be navigated to next.
        if (!value) {
            value = super.onOptionsItemSelected(item);
        }
        return value;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Toolbar toolbar = findViewById(R.id.settings_toolbar);
        this.setSupportActionBar(toolbar);
        assert this.getSupportActionBar() != null;
        this.getSupportActionBar().setTitle(R.string.settings);
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.getNavigationController().addOnDestinationChangedListener(this::onNavigated);
    }

    @Override
    public void hideBar() {

    }

    @Override
    public void showBar() {

    }

    @Override
    public View getRoot() {
        return null;
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
