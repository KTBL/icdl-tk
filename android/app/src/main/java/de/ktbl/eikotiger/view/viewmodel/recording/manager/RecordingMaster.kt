package de.ktbl.eikotiger.view.viewmodel.recording.manager

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import de.ktbl.android.sharedlibrary.util.validator
import de.ktbl.eikotiger.data.icdl.animalcategory.Branch
import de.ktbl.eikotiger.data.icdl.indicator.IndicatorWithAssessment
import de.ktbl.eikotiger.data.icdl.indicator.Var
import de.ktbl.eikotiger.data.mastermodel.instance.Instance
import de.ktbl.eikotiger.data.recordingmodel.location.LocationWithSnapshot
import de.ktbl.eikotiger.data.recordingmodel.session.RecordingSession
import de.ktbl.eikotiger.view.datainterface.recording.IRecordingMasterDA
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

/**
 * One Time Use only!
 * Once this class has been used, it cannot be reinitialized for another recording run!
 */
@Suppress("PropertyName")
class RecordingMaster @Inject constructor(private val repository: IRecordingMasterDA) {

    companion object {
        val TAG = RecordingMaster::class.simpleName
        val taggedTimber = Timber.tag(TAG)
    }

    private lateinit var protectedRecordingEnv: ProtectedRecordingEnvironment
    private lateinit var recordingMode: RecordingMode
    val currentRecordingMode: RecordingMode
        get() = recordingMode
    private lateinit var recordingProcessManager: BaseRecordingProcessManager
    private lateinit var recordingSession: RecordingSession
    private lateinit var availableLocations: List<LocationWithSnapshot>

    /**
     * Provide LiveData Access to the available and current Indicators and Locations
     */
    private lateinit var _bindable: BindableImpl
    val bindable: Bindable
        get() = _bindable


    suspend fun init(
        protectedRecordingEnv: ProtectedRecordingEnvironment,
        recordingMode: RecordingMode
    ): Boolean = withContext(
        Dispatchers.Default
    ) {
        this@RecordingMaster.protectedRecordingEnv = protectedRecordingEnv
        if (protectedRecordingEnv.isPracticeRecording) {
            this@RecordingMaster.recordingMode = recordingMode.toTestMode()
        } else {
            this@RecordingMaster.recordingMode = recordingMode
        }
        recordingSession = repository.createRecordingSession(
            protectedRecordingEnv.animalCategoryInstance,
            protectedRecordingEnv.chosenStock.stock
        )
        recordingProcessManager = createRecordingManager()
        val stockId = protectedRecordingEnv.chosenStock.stock.id
        availableLocations = repository.loadOrCreateLocationsWithSnapshots(
            stockId,
            protectedRecordingEnv.dateOfRecord,
            recordingSession
        )
        val branchId = protectedRecordingEnv.chosenStock.branch.id
        val indicators = repository.loadIndicatorsWithAssessment(branchId)
        recordingProcessManager.init(
            indicators,
            availableLocations[0],
            recordingSession,
            isTestRecording = recordingMode.isTestMode
        )

        setupBindable(indicators)

        return@withContext validate(false)
    }

    private fun setupBindable(indicators: List<IndicatorWithAssessment>) {
        _bindable = BindableImpl()
        _bindable._availableIndicators.postValue(indicators)
        _bindable._availableLocations.postValue(this@RecordingMaster.availableLocations)
        _bindable._currentIndicator.postValue(recordingProcessManager.currentIndicator)
        _bindable._currentLocation.postValue(this@RecordingMaster.availableLocations[0])
        _bindable._instance.postValue(protectedRecordingEnv.animalCategoryInstance)
        _bindable._branch.postValue(protectedRecordingEnv.chosenStock.branch)
        _bindable._recordingDate.postValue(protectedRecordingEnv.dateOfRecord.toString())
        _bindable._isTestRecording.postValue(protectedRecordingEnv.isPracticeRecording)
    }

    suspend fun setActiveIndicator(indicatorId: Long) {
        validate(false)
        val index = isIndicatorIdValid(indicatorId)
        val nextIndicator = recordingProcessManager.setIndicator(index)
        _bindable._currentIndicator.postValue(nextIndicator)
    }

    suspend fun setActiveLocation(locationId: Long) {
        validate(true)
        val location = isLocationIdValid(locationId)
        val nextIndicator = recordingProcessManager.changeAnimalGroup(location)
        _bindable._currentIndicator.postValue(nextIndicator)
        _bindable._currentLocation.postValue(recordingProcessManager.currentAnimalGroup)
    }


    private fun createRecordingManager(): BaseRecordingProcessManager = when (recordingMode) {
        RecordingMode.BY_INDICATOR           -> PerIndicatorRecordingProcessManager(repository)
        RecordingMode.BY_ANIMAL              -> PerAnimalRecordingProcessManager(repository)
        RecordingMode.BY_ANIMAL_TEST         -> PerAnimalRecordingProcessManager(repository)
        RecordingMode.BY_INDICATOR_TEST      -> PerIndicatorRecordingProcessManager(repository)
        RecordingMode.OFFICE_INDICATORS      -> TODO()
        RecordingMode.OFFICE_INDICATORS_TEST -> TODO()
    }


    suspend fun shutdown() {
        recordingProcessManager.shutdown()
    }

    private fun validate(throws: Boolean) = validator(throws) {
        isNotNull(protectedRecordingEnv)
        isNotNull(recordingMode)
        isNotNull(recordingProcessManager)
        isNotNull(availableLocations)
    }

    private fun isIndicatorIdValid(indicatorId: Long): Int {
        for (index in recordingProcessManager.playlist.indices) {
            val indicator = recordingProcessManager.playlist[index]
            if (indicator.indicator.id == indicatorId) {
                return index
            }
        }

        val e =
            IllegalStateException("isIndicatorIdValid: no indicator with id=$indicatorId found!")
        taggedTimber.wtf(e)
        throw e
    }

    private fun isLocationIdValid(locationId: Long): LocationWithSnapshot {
        for (location in availableLocations) {
            if (location.location.id == locationId) {
                return location
            }
        }

        val e = IllegalStateException("isIndicatorIdValid: no location with id=$locationId found!")
        taggedTimber.wtf(e)
        throw e
    }

    suspend fun forward(option: Var? = null) = withContext(Dispatchers.Default) {
        var optionList: List<Var>? = null
        if (option != null) {
            optionList = listOf(option)
        }
        val nextIndicator = recordingProcessManager.forward(optionList)
        _bindable._currentIndicator.postValue(nextIndicator)
    }

    suspend fun fastForward(option: Var? = null) = withContext(Dispatchers.Default) {
        var optionList: List<Var>? = null
        if (option != null) {
            optionList = listOf(option)
        }
        val nextIndicator = recordingProcessManager.fastForward(optionList)
        _bindable._currentIndicator.postValue(nextIndicator)
    }


    /**
     * Private implementation of Bindable so we can set the values, without exposing the set
     * functionalities.
     * This is not part of the RecordingMaster itself, because the RecordingMaster should be
     * free of LiveData
     */
    private class BindableImpl : Bindable {
        val _availableLocations: MutableLiveData<List<LocationWithSnapshot>> = MutableLiveData()
        override val availableLocations: LiveData<List<LocationWithSnapshot>>
            get() = _availableLocations

        val _currentLocation: MutableLiveData<LocationWithSnapshot> = MutableLiveData()
        override val currentLocation: LiveData<LocationWithSnapshot>
            get() = _currentLocation

        val _currentIndicator: MutableLiveData<IndicatorWithAssessment> = MutableLiveData()
        override val currentIndicator: LiveData<IndicatorWithAssessment>
            get() = _currentIndicator

        val _availableIndicators: MutableLiveData<List<IndicatorWithAssessment>> = MutableLiveData()
        override val availableIndicators: LiveData<List<IndicatorWithAssessment>>
            get() = _availableIndicators

        val _instance: MutableLiveData<Instance> = MutableLiveData()
        override val instance: LiveData<Instance>
            get() = _instance

        val _branch: MutableLiveData<Branch> = MutableLiveData()
        override val branch: LiveData<Branch>
            get() = _branch

        val _recordingDate: MutableLiveData<String> = MutableLiveData()
        override val recordingDate: LiveData<String>
            get() = _recordingDate

        val _isTestRecording: MutableLiveData<Boolean> = MutableLiveData()
        override val isTestRecording: LiveData<Boolean>
            get() = _isTestRecording
    }

    /**
     * Public interface for access of the properties
     */
    interface Bindable {
        val availableLocations: LiveData<List<LocationWithSnapshot>>
        val currentLocation: LiveData<LocationWithSnapshot>
        val currentIndicator: LiveData<IndicatorWithAssessment>
        val availableIndicators: LiveData<List<IndicatorWithAssessment>>
        val instance: LiveData<Instance>
        val branch: LiveData<Branch>
        val recordingDate: LiveData<String>
        val isTestRecording: LiveData<Boolean>
    }
}


