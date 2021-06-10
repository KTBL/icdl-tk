package de.ktbl.eikotiger.view.adapter.shared

import androidx.lifecycle.LifecycleOwner
import de.ktbl.android.sharedlibrary.view.adapter.BaseBindingAdapter
import de.ktbl.android.sharedlibrary.view.adapter.BindableViewHolder
import de.ktbl.eikotiger.R
import de.ktbl.eikotiger.databinding.AdapterGroupListBinding
import de.ktbl.eikotiger.view.viewmodel.productiondirection.group.GroupListItem

class GroupListItemAdapter(
    lifecycleOwner: LifecycleOwner,
    dataset: List<GroupListItem> = listOf()
) : BaseBindingAdapter<AdapterGroupListBinding, GroupListItem>(
    AdapterGroupListBinding::bind,
    R.layout.adapter_group_list,
    dataset,
    lifecycleOwner
) {
    override fun bindData(
        item: GroupListItem,
        holder: BindableViewHolder<AdapterGroupListBinding>
    ) {
        holder.viewBinding.vm = item
    }
}