package de.ktbl.eikotiger.view.adapter.shared;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;

import java.util.List;

import de.ktbl.android.sharedlibrary.view.adapter.BaseBindingAdapter;
import de.ktbl.android.sharedlibrary.view.adapter.BindableViewHolder;
import de.ktbl.eikotiger.R;
import de.ktbl.eikotiger.databinding.AdapterClassificationOptionAltBinding;
import de.ktbl.eikotiger.view.viewmodel.indicator.ClassificationOptionVM;

public class ClassificationOptionListAdapter
        extends BaseBindingAdapter<AdapterClassificationOptionAltBinding, ClassificationOptionVM> {
    public ClassificationOptionListAdapter(@NonNull List<ClassificationOptionVM> dataset,
                                           LifecycleOwner lifecycleOwner) {
        super(AdapterClassificationOptionAltBinding::bind,
              R.layout.adapter_classification_option_alt,
              dataset,
              lifecycleOwner);
    }

    @Override
    protected void bindData(@NonNull ClassificationOptionVM item,
                            @NonNull BindableViewHolder<AdapterClassificationOptionAltBinding> holder) {
        holder.getViewBinding()
              .setVm(item);
    }
}
