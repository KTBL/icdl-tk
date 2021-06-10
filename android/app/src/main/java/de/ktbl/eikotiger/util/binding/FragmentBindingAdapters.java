package de.ktbl.eikotiger.util.binding;

import androidx.fragment.app.Fragment;

import javax.inject.Inject;

/**
 * Binding adapters that work with a fragment instance.
 */
public class FragmentBindingAdapters {
    final Fragment fragment;

    @Inject
    public FragmentBindingAdapters(Fragment fragment) {
        this.fragment = fragment;
    }
}