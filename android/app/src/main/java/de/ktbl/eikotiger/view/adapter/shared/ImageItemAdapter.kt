package de.ktbl.eikotiger.view.adapter.shared

import androidx.lifecycle.LifecycleOwner
import de.ktbl.android.sharedlibrary.view.adapter.BaseBindingAdapter
import de.ktbl.android.sharedlibrary.view.adapter.BindableViewHolder
import de.ktbl.eikotiger.R
import de.ktbl.eikotiger.databinding.AdapterImageItemBinding
import de.ktbl.eikotiger.view.viewmodel.ImageItem

class ImageItemAdapter(dataset: List<ImageItem>, lifecyclerOwner: LifecycleOwner) : BaseBindingAdapter<AdapterImageItemBinding, ImageItem>(
        AdapterImageItemBinding::bind,
        R.layout.adapter_image_item,
        dataset,
        lifecyclerOwner) {
    override fun bindData(item: ImageItem, holder: BindableViewHolder<AdapterImageItemBinding>) {
        holder.viewBinding.vm = item
    }
}