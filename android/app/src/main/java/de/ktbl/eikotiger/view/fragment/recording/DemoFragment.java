package de.ktbl.eikotiger.view.fragment.recording;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import de.ktbl.eikotiger.R;

public class DemoFragment extends Fragment {
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Bundle bundle = this.getArguments();
        if (bundle == null) {
            this.getActivity().onBackPressed();
            return null;
        }
        int demoId = 0; // DemoFragmentArgs.fromBundle(bundle).getDemoId();
        if (demoId == 0) {
            return inflater.inflate(R.layout.fragment_example_indicator_milk, container, false);
        } else {
            return inflater.inflate(R.layout.fragment_example_indicator_milk_input, container,
                                    false);
        }

    }
}
