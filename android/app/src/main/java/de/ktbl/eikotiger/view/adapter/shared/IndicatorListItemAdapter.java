package de.ktbl.eikotiger.view.adapter.shared;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;

import java.util.List;

import de.ktbl.android.sharedlibrary.view.adapter.BaseBindingAdapter;
import de.ktbl.android.sharedlibrary.view.adapter.BindableViewHolder;
import de.ktbl.eikotiger.R;
import de.ktbl.eikotiger.databinding.AdapterIndicatorListBinding;
import de.ktbl.eikotiger.view.viewmodel.indicator.IndicatorListItem;

public class IndicatorListItemAdapter
        extends BaseBindingAdapter<AdapterIndicatorListBinding, IndicatorListItem> {

    public IndicatorListItemAdapter(@NonNull List<IndicatorListItem> dataset,
                                    LifecycleOwner lifecycleOwner) {
        super(AdapterIndicatorListBinding::bind,
              R.layout.adapter_indicator_list,
              dataset,
              lifecycleOwner);
    }

    @Override
    protected void bindData(@NonNull IndicatorListItem item,
                            @NonNull BindableViewHolder<AdapterIndicatorListBinding> holder) {
        holder.getViewBinding()
              .setVm(item);
    }
}
