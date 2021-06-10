package de.ktbl.eikotiger.view.datainterface.recording

import de.ktbl.eikotiger.data.icdl.indicator.IndicatorWithAssessment
import de.ktbl.eikotiger.data.recordingmodel.record.IndicatorRecordWithSelectionsAndLocation
import de.ktbl.eikotiger.data.recordingmodel.session.IndicatorRecordingSessionWithRecords
import de.ktbl.eikotiger.data.recordingmodel.session.RecordingSession

interface IRecordingBaseManagerDA {
    suspend fun createIndicatorRecordingSessions(
        indicators: List<IndicatorWithAssessment>,
        recordingSession: RecordingSession
    ): Map<Long, IndicatorRecordingSessionWithRecords>

    suspend fun saveRecord(record: IndicatorRecordWithSelectionsAndLocation)
}