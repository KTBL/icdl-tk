package de.ktbl.eikotiger.view.viewmodel.indicator

import android.content.res.AssetManager
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.liveData
import com.android.example.github.util.getOrAwaitValue
import de.ktbl.android.sharedlibrary.view.viewmodel.AbstractBaseViewModel
import de.ktbl.eikotiger.data.Constants
import de.ktbl.eikotiger.data.icdl.indicator.Var
import de.ktbl.eikotiger.view.datainterface.indicator.IIndicatorOptionDetailsDA
import de.ktbl.eikotiger.view.viewmodel.ImageItem
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Provider

class IndicatorOptionDetailsVM @Inject constructor(
    private val repository: IIndicatorOptionDetailsDA,
    private val imageItemProvider: Provider<ImageItem>,
) : AbstractBaseViewModel() {

    companion object {
        val TAG = IndicatorOptionDetailsVM::class.java.simpleName
    }

    @Inject
    lateinit var assetManager: AssetManager

    private val optionId: MutableLiveData<Long> = MutableLiveData()

    val option = Transformations.switchMap(optionId) {
        repository.loadOptionById(it)
    }

    val description = Transformations.map(option) {
        it.longDescription
    }

    val optionName = Transformations.map(option) {
        it.name
    }

    val imageItemList: LiveData<List<ImageItem>> = Transformations.switchMap(
        option,
        this::loadImages
    )

    private fun loadImages(option: Var): LiveData<List<ImageItem>> = liveData {
        val list = ArrayList<ImageItem>()
        val folderKey = repository.getFolderKeyByOptionId(optionId.getOrAwaitValue())
        for (image in option.images) {
            val item = imageItemProvider.get()
            val path =
                "${Constants.IMAGE_FOLDER}/$folderKey/${image.trim()}.${Constants.IMAGE_FILE_EXTENSION}"
            Timber.tag(TAG)
                .d("Add thumbnail image %s", path)
            item.imagePath.value = path
            item.description.value = option.longDescription
            list.add(item)
        }
        emit(list)
    }

    fun loadData(optionId: Long, mock: Boolean) {
        if (mock) {
            Timber.tag(IndicatorInfoVM.TAG)
                .i("Mocked data has been requested")
            this.loadMockedData()
            return
        } else {
            if (this.optionId.value != optionId)
                this.optionId.value = optionId

            Timber.tag(IndicatorInfoVM.TAG)
                .d("Load data for option id %s", optionId)
        }

    }
}