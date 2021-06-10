package de.ktbl.eikotiger.data.recordingmodel.session

import androidx.room.*
import de.ktbl.eikotiger.data.db.converters.DateConverter
import de.ktbl.eikotiger.data.mastermodel.instance.Instance
import de.ktbl.eikotiger.data.mastermodel.stock.Stock
import de.ktbl.eikotiger.data.recordingmodel.RecordingBaseModel
import de.ktbl.eikotiger.data.recordingmodel.location.LocationSnapshot
import java.util.*

/**
 * Central access model for a RecordingSession.
 * This class summarizes and provides access to all
 * IndicatorRecordingSessions done within a single
 * RecordingSession. It also provides access to
 * every LocationSnapshot created for this session.
 */
@Entity(
        tableName = "RecordingSession",
        indices = [
            Index(value = ["id"]),
            Index(value = ["instanceId"]),
            Index(value = ["stockId"])
        ],
        foreignKeys = [
            ForeignKey(
                    parentColumns = ["id"],
                    childColumns = ["instanceId"],
                    entity = Instance::class,
                    onDelete = ForeignKey.CASCADE,
                    onUpdate = ForeignKey.CASCADE,
            ),
            ForeignKey(
                    parentColumns = ["id"],
                    childColumns = ["stockId"],
                    entity = Stock::class,
                    onDelete = ForeignKey.CASCADE,
                    onUpdate = ForeignKey.CASCADE
            )
        ],
)
class RecordingSession : RecordingBaseModel() {
    @ColumnInfo(name = "startTimestamp")
    @TypeConverters(DateConverter::class)
    var startTimestamp: Date = Date()

    @ColumnInfo(name = "endTimestamp")
    @TypeConverters(DateConverter::class)
    var endTimestamp: Date = Date()

    @ColumnInfo(name = "instanceId")
    var instanceId = 0L

    @ColumnInfo(name = "stockId")
    var stockId = 0L
}

data class CompleteRecordingSession(
        @Embedded
        var recordingSession: RecordingSession,

        @Relation(
                parentColumn = "id",
                entityColumn = "recordingSessionId",
        )
        var locationSnapshots: List<LocationSnapshot> = mutableListOf(),

        @Relation(
                parentColumn = "id",
                entityColumn = "recordingSessionId"
        )
        var indicatorSessions: List<IndicatorRecordingSession> = mutableListOf(),
)