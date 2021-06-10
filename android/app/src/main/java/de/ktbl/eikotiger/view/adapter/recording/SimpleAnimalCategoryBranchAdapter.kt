package de.ktbl.eikotiger.view.adapter.recording

import androidx.lifecycle.LifecycleOwner
import de.ktbl.android.sharedlibrary.view.adapter.BaseBindingAdapter
import de.ktbl.android.sharedlibrary.view.adapter.BindableViewHolder
import de.ktbl.eikotiger.R
import de.ktbl.eikotiger.databinding.AdapterAnimalCategoryBranchSimpleBinding
import de.ktbl.eikotiger.view.viewmodel.productiondirection.SimpleAnimalCategoryBranchListItem

class SimpleAnimalCategoryBranchAdapter(lifecycleOwner: LifecycleOwner) :
    BaseBindingAdapter<AdapterAnimalCategoryBranchSimpleBinding, SimpleAnimalCategoryBranchListItem>(
        AdapterAnimalCategoryBranchSimpleBinding::bind,
        R.layout.adapter_animal_category_branch_simple,
        ArrayList(),
        lifecycleOwner
    ) {

    override fun bindData(
        item: SimpleAnimalCategoryBranchListItem,
        holder: BindableViewHolder<AdapterAnimalCategoryBranchSimpleBinding>
    ) {
        holder.viewBinding.vm = item
    }
}