package de.ktbl.eikotiger.view.viewmodel.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import de.ktbl.eikotiger.data.json.ICDLParser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

/**
 * Worked by k.schneider
 * ViewModel dedicated to handle the SplashscreenActivity data
 *
 *
 */
class SplashscreenVM : ViewModel {

    val isDownloadRunning = MutableLiveData(false)
    lateinit var icdlParser: ICDLParser

    //empty constructor for androidx.lifecycle.ViewModelProvider$NewInstanceFactory
    constructor() {
        //this.isDownloadRunning = new ObservableBoolean(false);
    }

    @Inject
    constructor(icdlParser: ICDLParser) {
        //this.isDownloadRunning = new ObservableBoolean(false);
        this.icdlParser = icdlParser
    }

    /**
     * Called for example if the Update-Button in the UI has been clicked
     */
    fun startUpdate() {
        Timber.tag(TAG).i("Start Update: " + true + " ")
//        downloadManager!!.downloadIndicatorData { message: InitializationStatus, count: Int ->
//            onUpdateFinished(message,
//                             count)
//        }
        isDownloadRunning.value = true
        viewModelScope.launch(Dispatchers.IO) {
            icdlParser.parseAndInsertAllAssets()
            isDownloadRunning.postValue(false)
        }
    }

    /**
     * Called for example if the Cancle-Button in the UI has been clicked
     */
    fun stopUpdate() {
    }


    fun onStartCancelUpdateClicked() {
        Timber.tag(TAG).d("onStartCancleUpdateCLicked()")
        startUpdate()
    }

    companion object {
        private const val TAG = "SplashScreenVM"
    }
}