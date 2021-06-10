package de.ktbl.eikotiger.data.apprepository

import de.ktbl.eikotiger.data.db.converters.DateStringConverter
import de.ktbl.eikotiger.data.icdl.indicator.IndicatorDao
import de.ktbl.eikotiger.data.icdl.indicator.IndicatorWithAssessment
import de.ktbl.eikotiger.data.mastermodel.instance.Instance
import de.ktbl.eikotiger.data.mastermodel.location.Location
import de.ktbl.eikotiger.data.mastermodel.location.LocationDao
import de.ktbl.eikotiger.data.mastermodel.stock.Stock
import de.ktbl.eikotiger.data.recordingmodel.location.EmptyLocationSnapshot
import de.ktbl.eikotiger.data.recordingmodel.location.LocationSnapshot
import de.ktbl.eikotiger.data.recordingmodel.location.LocationSnapshotDao
import de.ktbl.eikotiger.data.recordingmodel.location.LocationWithSnapshot
import de.ktbl.eikotiger.data.recordingmodel.record.IndicatorRecordDao
import de.ktbl.eikotiger.data.recordingmodel.record.IndicatorRecordWithSelectionsAndLocation
import de.ktbl.eikotiger.data.recordingmodel.record.OptionSelection
import de.ktbl.eikotiger.data.recordingmodel.record.OptionSelectionDao
import de.ktbl.eikotiger.data.recordingmodel.session.*
import de.ktbl.eikotiger.view.datainterface.recording.IRecordingBaseManagerDA
import de.ktbl.eikotiger.view.datainterface.recording.IRecordingMasterDA
import java.util.*
import javax.inject.Inject
import kotlin.collections.HashMap


class RecordingRepository @Inject constructor(
    private val indicatorDao: IndicatorDao,
    private val locationDao: LocationDao,
    private val locationSnapshotDao: LocationSnapshotDao,
    private val recordingSessionDao: RecordingSessionDao,
    private val indicatorRecordDao: IndicatorRecordDao,
    private val indicatorRecordingSessionDao: IndicatorRecordingSessionDao,
    private val optionSelectionDao: OptionSelectionDao,
) : IRecordingBaseManagerDA, IRecordingMasterDA {

    override suspend fun loadOrCreateLocationsWithSnapshots(
        stockId: Long,
        date: Date,
        recordingSession: RecordingSession
    ): List<LocationWithSnapshot> {
        val allLocations = locationDao.getByStockId(stockId)
        val locationsWithSnapshots = locationDao.getLocationsWithSnapshots(
            stockId,
            DateStringConverter.dateFormat.format(
                date
            )
        )
        val locationWithSnapshotsMap =
            locationsWithSnapshots.map { it.location.id to it }.toMap().toMutableMap()
        // locationsWithSnapshots contains only Locations which have a Snapshot for the current day
        // The list is empty, if now Snapshot has been created for the current day yet and,
        // in case a Location has been added in the meanwhile, a snapshot must be created for it.
        // Also, in case the animal count of a Location has changed during the day it will be updated
        // as well.

        for (location in allLocations) {
            val locationwithSnapshot =
                locationWithSnapshotsMap.getOrDefault(location.id, LocationWithSnapshot(location))
            val snapshot = locationwithSnapshot.locationSnapshot
            if (snapshot is EmptyLocationSnapshot) {
                locationwithSnapshot.locationSnapshot =
                    createLocationSnapshot(location, recordingSession.id)
                locationWithSnapshotsMap[location.id] = locationwithSnapshot
            } else {
                if (location.count != snapshot.countSnapshot) {
                    snapshot.countSnapshot = location.count
                    locationSnapshotDao.update(snapshot)
                }
            }
        }

        return locationWithSnapshotsMap.map { it.value }.toList()
    }

    private suspend fun createLocationSnapshot(
        location: Location,
        sessionId: Long
    ): LocationSnapshot {
        val newSnapshot = LocationSnapshot().apply {
            this.locationId = location.id
            this.countSnapshot = location.count
            this.recordingSessionId = sessionId
        }
        val id = locationSnapshotDao.insert(newSnapshot)
        newSnapshot.id = id
        return newSnapshot
    }

    override suspend fun createRecordingSession(
        instance: Instance,
        stock: Stock
    ): RecordingSession {
        val session = RecordingSession().apply {
            this.instanceId = instance.id
            this.stockId = stock.id
        }
        val id = recordingSessionDao.insert(session)
        session.id = id
        return session
    }

    override suspend fun loadIndicatorsWithAssessment(branchId: Long): List<IndicatorWithAssessment> {
        return indicatorDao.loadAllWithAssessmentByBranchId(branchId)
    }

    override suspend fun createIndicatorRecordingSessions(
        indicators: List<IndicatorWithAssessment>,
        recordingSession: RecordingSession
    ): Map<Long, IndicatorRecordingSessionWithRecords> {
        val map = HashMap<Long, IndicatorRecordingSessionWithRecords>()
        for (indicator in indicators) {
            val session = IndicatorRecordingSession()
            val indicatorId = indicator.indicator.id
            session.indicatorId = indicatorId
            session.recordingSessionId = recordingSession.id
            val id = indicatorRecordingSessionDao.insert(session)
            session.id = id
            val sessionWithRecords = IndicatorRecordingSessionWithRecords(session, mutableListOf())
            map[indicatorId] = sessionWithRecords
        }
        return map
    }

    override suspend fun saveRecord(record: IndicatorRecordWithSelectionsAndLocation) {
        val indicatorRecord = record.indicatorRecord
        if (indicatorRecord.locationSnapshotId == 0L) {
            indicatorRecord.locationSnapshotId = record.locationSnapshot.id
        }
        val id = indicatorRecordDao.insert(indicatorRecord)
        indicatorRecord.id = id
        for (option in record.optionsSelected) {
            val optionSelection = OptionSelection().apply {
                this.indicatorRecordId = indicatorRecord.id
                this.varId = option.id
            }
            optionSelectionDao.insert(optionSelection)
        }
    }
}