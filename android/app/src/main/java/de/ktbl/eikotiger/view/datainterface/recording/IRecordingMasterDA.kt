package de.ktbl.eikotiger.view.datainterface.recording

import de.ktbl.eikotiger.data.icdl.indicator.IndicatorWithAssessment
import de.ktbl.eikotiger.data.mastermodel.instance.Instance
import de.ktbl.eikotiger.data.mastermodel.stock.Stock
import de.ktbl.eikotiger.data.recordingmodel.location.LocationWithSnapshot
import de.ktbl.eikotiger.data.recordingmodel.session.RecordingSession
import java.util.*

interface IRecordingMasterDA : IRecordingBaseManagerDA {

    suspend fun loadOrCreateLocationsWithSnapshots(stockId: Long, date: Date, recordingSession: RecordingSession): List<LocationWithSnapshot>
    suspend fun createRecordingSession(instance: Instance, stock: Stock): RecordingSession
    suspend fun loadIndicatorsWithAssessment(branchId: Long): List<IndicatorWithAssessment>
}