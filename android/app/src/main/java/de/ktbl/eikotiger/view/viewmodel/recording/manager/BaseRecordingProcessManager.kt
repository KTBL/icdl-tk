package de.ktbl.eikotiger.view.viewmodel.recording.manager

import android.annotation.SuppressLint
import de.ktbl.android.sharedlibrary.IllegalAppStateException
import de.ktbl.eikotiger.data.icdl.indicator.Indicator
import de.ktbl.eikotiger.data.icdl.indicator.IndicatorWithAssessment
import de.ktbl.eikotiger.data.icdl.indicator.Var
import de.ktbl.eikotiger.data.recordingmodel.location.LocationWithSnapshot
import de.ktbl.eikotiger.data.recordingmodel.record.IndicatorRecord
import de.ktbl.eikotiger.data.recordingmodel.record.IndicatorRecordWithSelectionsAndLocation
import de.ktbl.eikotiger.data.recordingmodel.session.IndicatorRecordingSessionWithRecords
import de.ktbl.eikotiger.data.recordingmodel.session.RecordingSession
import de.ktbl.eikotiger.view.datainterface.recording.IRecordingBaseManagerDA
import timber.log.Timber

/**
 *
 */
@SuppressLint("BinaryOperationInTimber")
abstract class BaseRecordingProcessManager(private val repository: IRecordingBaseManagerDA) {

    companion object {
        val TAG = BaseRecordingProcessManager::class.simpleName
        val taggedTimber = Timber.tag(TAG)
    }


    /**
     * Used to fetch all indicator records.
     */
    var indicatorRecordingSessions: Map<Long, IndicatorRecordingSessionWithRecords> = mapOf()
        protected set

    /**
     * This is the "Playlist"
     */
    var playlist: List<IndicatorWithAssessment> = listOf()
        protected set

    var isInitialized = false
        private set

    /**
     * Read only to get the currently worked Indicator
     */
    val currentIndicator: IndicatorWithAssessment
        get() {
            return if (currentIndicatorIndex < 0 || playlist.isNullOrEmpty() || currentIndicatorIndex >= playlist.size) {
                // This could actually happen in case this class is in an uninitliazed state
                // TODO: merge this branch with the if above, and add proper state validation!
                throw IllegalAppStateException("currentIndicatorIndex is out of bounds of currently available indicators!")
            } else {
                // Everything is fine
                playlist[currentIndicatorIndex]
            }
        }

    /**
     * Provides the index of the currently worked indicator.
     * Setting is only available for subclasses
     */
    var currentIndicatorIndex: Int = -1
        protected set

    /**
     * Provides the currently worked animal group
     *
     */
    var currentAnimalGroup: LocationWithSnapshot? = null
        protected set

    var isTestRecording: Boolean = false
        protected set

    /**
     * Initializes the RecordingProcessManager
     * @param indicators
     * @param startLocation
     */
    open suspend fun init(
        indicators: List<IndicatorWithAssessment>,
        startLocation: LocationWithSnapshot,
        recordingSession: RecordingSession,
        isTestRecording: Boolean = false
    ) {
        this.playlist = indicators
        this.currentAnimalGroup = startLocation
        this.indicatorRecordingSessions =
            repository.createIndicatorRecordingSessions(indicators, recordingSession)
        this.isInitialized = true
        this.isTestRecording = isTestRecording
        this.currentIndicatorIndex = 0
    }

    /**
     * Resets the RecordingProcessManager
     */
    open suspend fun shutdown() {
        this.playlist = listOf()
        this.currentAnimalGroup = null
        this.indicatorRecordingSessions = mapOf()
        this.isInitialized = false
    }

    protected fun initializedCheck() {
        if (!isInitialized) {
            val e = IllegalAppStateException("$TAG is not initialized!")
            taggedTimber.wtf(e)
            throw e
        }
    }


    /**
     * Changes the currently worked animal group.
     * @param nextAnimalGroup the animal group to change to
     * @param record if a record has been created and should be saved prior to the indicator change,
     *                  it can be passed here
     */
    open suspend fun changeAnimalGroup(nextAnimalGroup: LocationWithSnapshot, record: IndicatorRecordWithSelectionsAndLocation? = null): IndicatorWithAssessment {
        initializedCheck()
        if (record != null) {
            addIndicatorRecord(record)
        }
        taggedTimber.i("changeAnimalGroup: from ${currentAnimalGroup!!.location.editLabel}" +
                               "(id=${currentAnimalGroup!!.location.id}) to ${nextAnimalGroup.location.editLabel}" +
                               "(id=${nextAnimalGroup.location.id}, indicator stays")
        this.currentAnimalGroup = nextAnimalGroup
        return currentIndicator
    }

    /**
     * Adds a given IndicatorRecrodWithSelectionsAndLocation to the locally hold
     * recording map and saves it to the database.
     * If the passed record is null, nothing happens.
     */
    protected suspend fun addIndicatorRecord(record: IndicatorRecordWithSelectionsAndLocation) {
        initializedCheck()
        val indicatorId = currentIndicator.indicator.id
        val sessionWithRecords = indicatorRecordingSessions[indicatorId]!!
        taggedTimber.i("addIndicatorRecord: locationId= ${record.locationSnapshot.locationId}," +
                               " indicatorId=$indicatorId," +
                               " ${record.optionsSelected.joinToString { it.name }}," +
                               " isTestRecording=$isTestRecording")
        sessionWithRecords.records.add(record)
        if (!isTestRecording) {
            repository.saveRecord(record)
        }

    }

    protected fun createRecord(selection: List<Var>, indicator: Indicator): IndicatorRecordWithSelectionsAndLocation {
        val indicatorId = indicator.id
        val sessionWithRecords = indicatorRecordingSessions[indicatorId]!!
        val indicatorRecord = IndicatorRecord()
        indicatorRecord.indicatorId = indicator.id
        indicatorRecord.locationSnapshotId = currentAnimalGroup!!.locationSnapshot.id
        indicatorRecord.indicatorRecordingSessionId =
            sessionWithRecords.indicatorRecordingSession.id
        return IndicatorRecordWithSelectionsAndLocation(
            optionsSelected = selection,
            locationSnapshot = currentAnimalGroup!!.locationSnapshot,
            indicatorRecord = indicatorRecord
        )
    }

    protected suspend fun createAndAddRecord(selection: List<Var>?, indicator: Indicator) {
        if (selection == null) {
            return
        }
        val record = createRecord(selection, indicator)
        addIndicatorRecord(record)
    }

    /**
     * Changes the indicator to the one at the given index. The index is in relation to the
     * indicators property.
     * @param index the of the indicator to go to
     * @param record if a record has been created and should be saved prior to the indicator change,
     *                  it can be passed here
     */
    suspend fun setIndicator(
        index: Int,
        record: IndicatorRecordWithSelectionsAndLocation? = null
    ): IndicatorWithAssessment {
        initializedCheck()
        if (index < 0 || index >= playlist.size) {
            throw IllegalAppStateException("Indicator index set to out of bounds $index")
        }
        if (record != null) {
            addIndicatorRecord(record)
        }
        taggedTimber.i(
            "setIndicator: from index=$currentIndicatorIndex(name=${currentIndicator.indicator.name})" +
                    " to index=$index(name=${playlist[index].indicator.name})"
        )
        currentIndicatorIndex = index
        return currentIndicator
    }


    /**
     * Move to the next animal and start over with the first
     * @param selection if a record has been created and should be saved prior to the indicator change,
     *                  it can be passed here
     */
    abstract suspend fun nextAnimal(selection: List<Var>? = null): IndicatorWithAssessment


    /**
     * Move to the next Indicator.
     */
    suspend fun nextIndicator(selection: List<Var>? = null): IndicatorWithAssessment {
        initializedCheck()
        if (selection != null) {
            val record = createRecord(selection, currentIndicator.indicator)
            addIndicatorRecord(record)
        }
        val nextIndex = (currentIndicatorIndex + 1) % playlist.size
        taggedTimber.i("setIndicator: from index=$currentIndicatorIndex(name=${currentIndicator.indicator.name})" +
                               " to index=$nextIndex(name=${playlist[nextIndex].indicator.name})")
        currentIndicatorIndex = nextIndex
        return currentIndicator
    }


    /**
     * Move to the nex Indicator or Animal. The manager decides what to do
     */
    abstract suspend fun forward(selection: List<Var>? = null): IndicatorWithAssessment

    abstract suspend fun fastForward(selection: List<Var>? = null): IndicatorWithAssessment
}