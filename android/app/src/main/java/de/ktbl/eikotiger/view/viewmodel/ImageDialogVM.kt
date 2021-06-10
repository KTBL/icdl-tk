package de.ktbl.eikotiger.view.viewmodel

import android.content.res.AssetManager
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import de.ktbl.android.sharedlibrary.view.viewmodel.AbstractBaseViewModel
import de.ktbl.eikotiger.view.loadImageFromAsset
import javax.inject.Inject

class ImageDialogVM @Inject constructor(private val assetManager: AssetManager) : AbstractBaseViewModel() {

    companion object {
        val TAG = ImageDialogVM::class.java.simpleName
    }

    val imagePath = MutableLiveData<String>()
    val imageDrawable = Transformations.map(imagePath) {
        loadImageFromAsset(it, assetManager)
    }

}
