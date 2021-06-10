package de.ktbl.eikotiger.view.viewmodel.indicator

import android.content.res.AssetManager
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import de.ktbl.android.sharedlibrary.navigation.NavigationCommand
import de.ktbl.android.sharedlibrary.view.adapter.Clickable
import de.ktbl.android.sharedlibrary.view.viewmodel.AbstractBaseViewModel
import de.ktbl.eikotiger.data.Constants
import de.ktbl.eikotiger.data.icdl.indicator.Var
import de.ktbl.eikotiger.view.fragment.indicator.IndicatorOptionListFragmentDirections
import de.ktbl.eikotiger.view.loadImageFromAsset
import timber.log.Timber
import javax.inject.Inject

typealias OnOptionSelectedHandler = (option: Var) -> Unit

/* Treat this as data object: There is no performance gain in doing this from here, cause fetching a room relation is more efficient as multiple single rows
    * Furthermore this is called from an background thread hence it would not become visible in forground anyway
    * So we only need to copy relevant values
    */
class ClassificationOptionVM @Inject constructor() : AbstractBaseViewModel(), Clickable {


    @Inject
    lateinit var assetManager: AssetManager

    private lateinit var onOptionSelectedHandler: OnOptionSelectedHandler

    private val inputOption = MutableLiveData<Var?>()
    var title = Transformations.map(this.inputOption) {
        it?.name ?: ""
    }
    var description = Transformations.map(this.inputOption) {
        if (it != null) {
            it.name + ": " + it.shortDescription
        } else {
            ""
        }
    }

    var thumbnail = Transformations.map(this.inputOption) {
        if (it != null) {
            val path = "${Constants.IMAGE_FOLDER}/$folderKey/${it.thumbnail}.${Constants.IMAGE_FILE_EXTENSION}"
            loadImageFromAsset(path, assetManager)
        } else {
            null
        }
    }

    private var folderKey = ""

    var selectOptionEnabled: MutableLiveData<Boolean> = MediatorLiveData()


    /**
     * Passes the icdl description in background
     * for imutable fields this can simply be copied as there is no version check or whatever
     *
     * @param option
     */
    fun postOption(option: Var, folderKey: String, onOptionSelectedHandler: OnOptionSelectedHandler) {
        this.onOptionSelectedHandler = onOptionSelectedHandler
        this.folderKey = folderKey
        this.inputOption.postValue(option)
        Timber.tag(TAG)
                .d("Received option data for %s with contents %s - %s at %s",
                   option.__key,
                   option.name,
                   option.shortDescription,
                   option.thumbnail)

    }

    override fun enableClick(enabled: Boolean) {
        // todo
    }


    fun chooseClicked() {
        this.onOptionSelectedHandler(inputOption.value!!)
    }

    fun detailsClicked() {
        var navId = -1L
        if (this.inputOption.value != null) {
            navId = this.inputOption.value!!.id
        }
        val action = IndicatorOptionListFragmentDirections.actionIndicatorOptionListToIndicatorOptionDetailsFragment(
                navId)
        this.navigationEventHandler.notifyLiveEvent(NavigationCommand(action))
    }

    companion object {
        private val TAG = ClassificationOptionVM::class.java.simpleName
    }

    fun showImage() {
        // The value of inputOption could change while creating the path variable below. Therefore
        // we're creating a copy of its value first
        val option = inputOption.value
        if (option != null) {
            val path = "${Constants.IMAGE_FOLDER}/$folderKey/${option.thumbnail}.${Constants.IMAGE_FILE_EXTENSION}"
            val navDirection = IndicatorOptionListFragmentDirections.actionIndicatorOptionListToFullscreenImageDialog(
                    path)
            this.navigationEventHandler.notifyLiveEvent(NavigationCommand(navDirection))
        }
    }
}