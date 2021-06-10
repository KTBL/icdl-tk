package de.ktbl.eikotiger.view.viewmodel

import android.content.res.AssetManager
import android.graphics.drawable.Drawable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import de.ktbl.android.sharedlibrary.annotation.mock.Mock
import de.ktbl.android.sharedlibrary.navigation.NavigationCommand
import de.ktbl.android.sharedlibrary.view.adapter.Clickable
import de.ktbl.android.sharedlibrary.view.viewmodel.AbstractBaseViewModel
import de.ktbl.eikotiger.view.fragment.indicator.IndicatorOptionDetailsFragmentDirections
import de.ktbl.eikotiger.view.loadImageFromAsset
import javax.inject.Inject

class ImageItem @Inject constructor(private val assetManager: AssetManager) : AbstractBaseViewModel(), Clickable {
    @SuppressWarnings("unused")
    val imagePath: MutableLiveData<String?> = MutableLiveData()
    val imageDrawable: LiveData<Drawable?> = Transformations.map(imagePath) {
        loadImageFromAsset(it, assetManager)
    }

    @Mock
    val description: MutableLiveData<String> = MutableLiveData("")

    override fun enableClick(enabled: Boolean) {
        // nop
    }

    fun onEnlargeImageClick() {
        // The value of imagePath could change while the body of if(..) is executed. Therefore
        // we copy it's value first.
        val path: String = imagePath.value ?: ""
        if (path.trim().isNotEmpty()) {
            val action = IndicatorOptionDetailsFragmentDirections.actionIndicatorOptionDetailsFragmentToFullscreenImageDialog(
                    path)
            this.navigationEventHandler.notifyLiveEvent(NavigationCommand(action))
        }
    }

}