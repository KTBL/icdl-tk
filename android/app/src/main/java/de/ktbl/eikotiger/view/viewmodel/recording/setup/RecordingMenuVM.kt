package de.ktbl.eikotiger.view.viewmodel.recording.setup

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import de.ktbl.android.sharedlibrary.livedata.valueOrDefault
import de.ktbl.android.sharedlibrary.navigation.NavigationCommand
import de.ktbl.android.sharedlibrary.view.viewmodel.AbstractBaseViewModel
import de.ktbl.eikotiger.view.fragment.recording.setup.RecordingSetupMenuFragmentDirections
import de.ktbl.eikotiger.view.viewmodel.RecordingInitializer
import de.ktbl.eikotiger.view.viewmodel.recording.manager.ProtectedRecordingEnvironment
import de.ktbl.eikotiger.view.viewmodel.recording.manager.RecordingMode
import kotlinx.coroutines.launch
import javax.inject.Inject

class RecordingMenuVM @Inject constructor() : AbstractBaseViewModel() {

    val perIndicatorLoading = MutableLiveData(false)
    val perAnimalLoading = MutableLiveData(false)
    val isLoading = MediatorLiveData<Boolean>()
    private lateinit var initializeRecording: RecordingInitializer

    private lateinit var protectedRecordingEnvironment: ProtectedRecordingEnvironment

    init {
        isLoading.addSource(perIndicatorLoading) {
            isLoading.value = checkIfLoading()
        }
    }

    fun load(protectedRecordingEnvironment: ProtectedRecordingEnvironment, initializer: RecordingInitializer) {
        this.protectedRecordingEnvironment = protectedRecordingEnvironment
        this.initializeRecording = initializer
    }

    private fun checkIfLoading(): Boolean {
        return perIndicatorLoading.valueOrDefault(false)
    }


    fun startPerIndicatorRecording() {
        viewModelScope.launch {
            perIndicatorLoading.postValue(true)
            initializeRecording(RecordingMode.BY_INDICATOR, protectedRecordingEnvironment)
            perIndicatorLoading.postValue(false)
            val navDir =
                RecordingSetupMenuFragmentDirections.actionRecordingSetupMenuFragmentToRecordingOverviewFragment()
            this@RecordingMenuVM.navigationEventHandler.postNotifyLiveEvent(NavigationCommand(navDir))
        }
    }

    fun startPerAnimalRecording() {
        viewModelScope.launch {
            perAnimalLoading.postValue(true)
            initializeRecording(RecordingMode.BY_ANIMAL, protectedRecordingEnvironment)
            perAnimalLoading.postValue(false)
            val navDir =
                RecordingSetupMenuFragmentDirections.actionRecordingSetupMenuFragmentToRecordingOverviewFragment()
            this@RecordingMenuVM.navigationEventHandler.postNotifyLiveEvent(NavigationCommand(navDir))
        }
    }

}