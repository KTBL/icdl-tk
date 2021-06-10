package de.ktbl.eikotiger.view.viewmodel.recording

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import de.ktbl.android.sharedlibrary.SnackbarNotificationEvent
import de.ktbl.eikotiger.data.icdl.indicator.Var
import de.ktbl.eikotiger.view.datainterface.indicator.IIndicatorOptionsOverviewDA
import de.ktbl.eikotiger.view.viewmodel.indicator.ClassificationOptionVM
import de.ktbl.eikotiger.view.viewmodel.indicator.IndicatorOptionListBaseVM
import de.ktbl.eikotiger.view.viewmodel.recording.manager.RecordingMaster
import de.ktbl.eikotiger.view.viewmodel.recording.manager.RecordingMode
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Provider

class ClassificationInputVM @Inject constructor(
    repository: IIndicatorOptionsOverviewDA,
    optionVMProvider: Provider<ClassificationOptionVM>
) : IndicatorOptionListBaseVM(repository, true, optionVMProvider) {

    private lateinit var recordingMaster: RecordingMaster

    private val _indicatorIdBinding = MediatorLiveData<Long>()

    override val _indicatorId: MutableLiveData<Long>
        get() = _indicatorIdBinding


    override fun onOptionSelected(option: Var) {
        viewModelScope.launch(Dispatchers.Default) {
            _isProcessing.postValue(true)
            recordingMaster.forward(option)
            snackbarNotificationEventLiveEventHandler.postNotifyLiveEvent(
                SnackbarNotificationEvent(
                    "Eingabe gespeichert! ✔"
                )
            )
            _isProcessing.postValue(false)
        }
    }

    private suspend fun updateCurrentIndicator() {
        val currentIndicatorId =
            recordingMaster.bindable.currentIndicator.value?.indicator?.id ?: -1L
        if (currentIndicatorId != indicatorId.value) {
            withContext(Dispatchers.Main) {
                super.loadData(currentIndicatorId)
            }
        }
    }

    override fun onForward() {
        viewModelScope.launch(Dispatchers.Default) {
            _isProcessing.postValue(true)
            recordingMaster.forward()
            _isProcessing.postValue(false)
        }
    }

    override fun onFastForward() {
        viewModelScope.launch(Dispatchers.Default) {
            _isProcessing.postValue(true)
            recordingMaster.fastForward()
            _isProcessing.postValue(false)
        }
    }

    fun loadData(indicatorId: Long, recordingMaster: RecordingMaster) {
        initializeLiveData()
        _isProcessing.value = true
        _indicatorIdBinding.addSource(recordingMaster.bindable.currentIndicator) {
            _indicatorIdBinding.value = it.indicator.id
        }
        this.recordingMaster = recordingMaster
        showForwardButtons.value =
            recordingMaster.currentRecordingMode == RecordingMode.BY_ANIMAL ||
                    recordingMaster.currentRecordingMode == RecordingMode.BY_ANIMAL_TEST
        _isProcessing.value = false
    }

    override fun clear() {
        _indicatorIdBinding.removeSource(recordingMaster.bindable.currentIndicator)
    }

}