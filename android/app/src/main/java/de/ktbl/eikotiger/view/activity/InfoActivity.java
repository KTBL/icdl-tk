package de.ktbl.eikotiger.view.activity;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;

import de.ktbl.eikotiger.R;

public class InfoActivity extends AppCompatActivity {

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        boolean value;
        item.getItemId();
        // In case there's no possibility to move further upwards (value == false) in the
        // settings-nav-graph
        // we're going to hand over the moving decision to the default behaviour.
        // In this case the PARENT_ACTIVITY defined in the AndroidManifest should be the one
        // to be navigated to next.
        value = super.onOptionsItemSelected(item);
        return value;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
    }
}
