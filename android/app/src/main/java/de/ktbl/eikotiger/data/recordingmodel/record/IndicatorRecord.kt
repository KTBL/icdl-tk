package de.ktbl.eikotiger.data.recordingmodel.record

import androidx.room.*
import de.ktbl.eikotiger.data.db.converters.DateConverter
import de.ktbl.eikotiger.data.icdl.indicator.Indicator
import de.ktbl.eikotiger.data.icdl.indicator.Var
import de.ktbl.eikotiger.data.recordingmodel.RecordingBaseModel
import de.ktbl.eikotiger.data.recordingmodel.location.LocationSnapshot
import de.ktbl.eikotiger.data.recordingmodel.session.IndicatorRecordingSession
import java.util.*

@Entity(
        tableName = "IndicatorRecord",
        indices = [
            Index(value = ["id"]),
            Index(value = ["indicatorId"]),
            Index(value = ["locationSnapshotId"]),
            Index(value = ["indicatorRecordingSessionId"])
        ],
        foreignKeys = [
            ForeignKey(
                    parentColumns = ["id"],
                    childColumns = ["indicatorId"],
                    entity = Indicator::class,
                    onUpdate = ForeignKey.CASCADE,
                    onDelete = ForeignKey.CASCADE,
            ),
            ForeignKey(
                    parentColumns = ["id"],
                    childColumns = ["locationSnapshotId"],
                    entity = LocationSnapshot::class,
                    onUpdate = ForeignKey.CASCADE,
                    onDelete = ForeignKey.CASCADE,
            ),
            ForeignKey(
                    parentColumns = ["id"],
                    childColumns = ["indicatorRecordingSessionId"],
                    entity = IndicatorRecordingSession::class,
                    onDelete = ForeignKey.CASCADE,
                    onUpdate = ForeignKey.CASCADE,
            )
        ],
)
class IndicatorRecord : RecordingBaseModel() {
    @ColumnInfo(name = "indicatorId")
    var indicatorId = 0L

    @ColumnInfo(name = "timestamp")
    @TypeConverters(DateConverter::class)
    var timestamp: Date = Date()

    @ColumnInfo(name = "locationSnapshotId")
    var locationSnapshotId = 0L

    @ColumnInfo(name = "indicatorRecordingSessionId")
    var indicatorRecordingSessionId = 0L
}

data class IndicatorRecordWithSelectionsAndLocation(
        @Embedded
        var indicatorRecord: IndicatorRecord,

        @Relation(
                parentColumn = "id",
                entityColumn = "indicatorRecordId",
                associateBy = Junction(OptionSelection::class)
        )
        var optionsSelected: List<Var> = mutableListOf(),

        @Relation(
                parentColumn = "id",
                entityColumn = "locationSnapshotId",
        )
        var locationSnapshot: LocationSnapshot
)