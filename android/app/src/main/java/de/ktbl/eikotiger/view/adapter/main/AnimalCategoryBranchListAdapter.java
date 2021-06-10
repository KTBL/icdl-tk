package de.ktbl.eikotiger.view.adapter.main;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;

import java.util.List;

import de.ktbl.android.sharedlibrary.view.adapter.BaseBindingAdapter;
import de.ktbl.android.sharedlibrary.view.adapter.BindableViewHolder;
import de.ktbl.eikotiger.R;
import de.ktbl.eikotiger.databinding.AdapterAnimalCategoryBranchBinding;
import de.ktbl.eikotiger.view.viewmodel.productiondirection.ProductionDirectionSectionVM;

public class AnimalCategoryBranchListAdapter
        extends BaseBindingAdapter<AdapterAnimalCategoryBranchBinding, ProductionDirectionSectionVM> {
    public AnimalCategoryBranchListAdapter(@NonNull List<ProductionDirectionSectionVM> dataset,
                                           LifecycleOwner lifecycleOwner) {
        super(AdapterAnimalCategoryBranchBinding::bind,
              R.layout.adapter_animal_category_branch,
              dataset,
              lifecycleOwner);
    }

    @Override
    protected void bindData(@NonNull ProductionDirectionSectionVM item,
                            @NonNull BindableViewHolder<AdapterAnimalCategoryBranchBinding> holder) {
        holder.getViewBinding()
              .setVm(item);
    }
}
