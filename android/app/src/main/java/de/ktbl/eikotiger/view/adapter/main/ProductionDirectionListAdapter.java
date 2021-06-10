package de.ktbl.eikotiger.view.adapter.main;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;

import java.util.List;

import de.ktbl.android.sharedlibrary.view.adapter.BaseBindingAdapter;
import de.ktbl.android.sharedlibrary.view.adapter.BindableViewHolder;
import de.ktbl.eikotiger.R;
import de.ktbl.eikotiger.databinding.AdapterProdDirectionBinding;
import de.ktbl.eikotiger.view.viewmodel.productiondirection.ProductionDirectionListItem;

public class ProductionDirectionListAdapter
        extends BaseBindingAdapter<AdapterProdDirectionBinding, ProductionDirectionListItem> {

    public ProductionDirectionListAdapter(@NonNull List<ProductionDirectionListItem> dataset,
                                          LifecycleOwner lifecycleOwner) {
        super(AdapterProdDirectionBinding::bind,
              R.layout.adapter_prod_direction,
              dataset,
              lifecycleOwner);
    }

    @Override
    protected void bindData(@NonNull ProductionDirectionListItem item,
                            @NonNull BindableViewHolder<AdapterProdDirectionBinding> holder) {
        holder.getViewBinding()
              .setVm(item);
    }
}
