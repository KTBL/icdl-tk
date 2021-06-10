package de.ktbl.eikotiger.view.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import de.ktbl.android.sharedlibrary.livedata.valueOrDefault
import de.ktbl.android.sharedlibrary.navigation.NavigationCommand
import de.ktbl.android.sharedlibrary.view.viewmodel.AbstractBaseViewModel
import de.ktbl.eikotiger.MainNavGraphDirections
import de.ktbl.eikotiger.view.fragment.indicator.IndicatorInfoFragmentModes
import de.ktbl.eikotiger.view.fragment.indicator.IndicatorOptionListFragmentMode
import de.ktbl.eikotiger.view.viewmodel.recording.manager.ProtectedRecordingEnvironment
import de.ktbl.eikotiger.view.viewmodel.recording.manager.RecordingMaster
import de.ktbl.eikotiger.view.viewmodel.recording.manager.RecordingMode
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Provider


typealias RecordingInitializer = suspend (mode: RecordingMode, protectedRecordingEnvironment: ProtectedRecordingEnvironment) -> Unit

/**
 * This ViewModels purpose is to hold general, app wide (state) information. Therefore it
 * falls into the category of SharedViewModels. It is bound to the MainActivity
 */
class RecordingStateVM @Inject constructor() : AbstractBaseViewModel() {

    companion object {
        val TAG = RecordingStateVM::class.simpleName
        val taggedTimber = Timber.tag(TAG)
    }

    @Inject
    lateinit var recordingMasterProvider: Provider<RecordingMaster>

    val isRecordingActive = MutableLiveData(false)
    var recordingMaster: RecordingMaster? = null
    val recordingMasterBindable: RecordingMaster.Bindable?
        get() = recordingMaster?.bindable


    suspend fun initializeRecording(mode: RecordingMode, protectedRecordingEnvironment: ProtectedRecordingEnvironment) {
        recordingMaster = recordingMasterProvider.get()
        recordingMaster!!.init(protectedRecordingEnvironment, mode)
        isRecordingActive.postValue(true)
    }

    fun shutdownRecording() {
        viewModelScope.launch {
            // We first navigate to the HomeFragment, then we shut down everything in the background
            val action = MainNavGraphDirections.actionGlobalHomeFragment()
            navigationEventHandler.postNotifyLiveEvent(NavigationCommand(action))

            if (isRecordingActive.value!!) {
                recordingMaster!!.shutdown()
            }
            recordingMaster = null
        }
    }

    fun navigateToIndicatorInfo() {
        if (!isRecordingActive.valueOrDefault(false)) {
            taggedTimber.wtf("navigateToIndicatorInfo: isRecordingActive == false!")
            return
        }
        val indicatorId = recordingMasterBindable?.currentIndicator?.value?.indicator?.id ?: -1
        val mode = IndicatorInfoFragmentModes.TEXT_ONLY
        val action = MainNavGraphDirections.actionGlobalIndicatorInfoFragmentOverview(
            indicatorId,
            mode
        )
        navigationEventHandler.notifyLiveEvent(NavigationCommand(action))
    }

    fun navigateToOverview() {
        if (!isRecordingActive.valueOrDefault(false)) {
            taggedTimber.wtf("navigateToOverview: isRecordingActive == false!")
        }
        val action = MainNavGraphDirections.actionGlobalRecordingOverviewFragment()
        navigationEventHandler.notifyLiveEvent(NavigationCommand(action))

    }

    fun navigateToInput() {
        if (!isRecordingActive.valueOrDefault(false)) {
            taggedTimber.wtf("navigateToInput: isRecordingActive == false!")
        }
        val mode = IndicatorOptionListFragmentMode.INPUT_CLASSIFICATION
        val indicatorId = recordingMasterBindable?.currentIndicator?.value?.indicator?.id ?: -1
        val action = MainNavGraphDirections.actionGlobalIndicatorOptionList(mode, indicatorId)
        navigationEventHandler.notifyLiveEvent(NavigationCommand(action))
    }


}