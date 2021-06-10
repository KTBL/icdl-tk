package de.ktbl.eikotiger.data.recordingmodel.session

import androidx.room.*
import de.ktbl.eikotiger.data.icdl.indicator.Indicator
import de.ktbl.eikotiger.data.recordingmodel.RecordingBaseModel
import de.ktbl.eikotiger.data.recordingmodel.record.IndicatorRecordWithSelectionsAndLocation

@Entity(
        tableName = "IndicatorRecordingSession",
        indices = [
            Index(value = ["recordingSessionId"]),
            Index(value = ["indicatorId"]),
            Index(value = ["id"])
        ],
        foreignKeys = [
            ForeignKey(
                    parentColumns = ["id"],
                    childColumns = ["recordingSessionId"],
                    entity = RecordingSession::class,
                    onDelete = ForeignKey.CASCADE,
                    onUpdate = ForeignKey.CASCADE
            ),
            ForeignKey(
                    parentColumns = ["id"],
                    childColumns = ["indicatorId"],
                    entity = Indicator::class,
                    onUpdate = ForeignKey.CASCADE,
                    onDelete = ForeignKey.CASCADE
            )
        ]
)
class IndicatorRecordingSession : RecordingBaseModel() {
    @ColumnInfo(name = "recordingSessionId")
    var recordingSessionId = 0L

    @ColumnInfo(name = "indicatorId")
    var indicatorId = 0L
}

data class IndicatorRecordingSessionWithRecords(
        @Embedded
        var indicatorRecordingSession: IndicatorRecordingSession,

        @Relation(
                parentColumn = "id",
                entityColumn = "recordingSessionId"
        )
        var records: MutableList<IndicatorRecordWithSelectionsAndLocation>
)
