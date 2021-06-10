package de.ktbl.eikotiger.data.recordingmodel.location

import androidx.room.*
import de.ktbl.eikotiger.data.db.converters.DateStringConverter
import de.ktbl.eikotiger.data.mastermodel.location.Location
import de.ktbl.eikotiger.data.recordingmodel.RecordingBaseModel
import de.ktbl.eikotiger.data.recordingmodel.session.RecordingSession
import java.util.*

/**
 * An IndicatorRecord is always associated with a Location. The properties
 * of a location are used to evaluate the indicator. Since the Location
 * can change over time (size, name, amount of animals, ..) a snapshot
 * of the Location is needed. This snapshot copies the critical data,
 * currently the amount of animals, and marks them with a timestamp and
 * references the original one
 */
@Entity(
        tableName = "LocationSnapshot",
        indices = [
            Index(value = ["id"]),
            Index(value = ["locationId"]),
            Index(value = ["recordingSessionId"]),
        ],
        foreignKeys = [
            ForeignKey(
                    parentColumns = ["id"],
                    childColumns = ["locationId"],
                    entity = Location::class,
                    onUpdate = ForeignKey.CASCADE,
                    onDelete = ForeignKey.CASCADE,
            ),
            ForeignKey(
                parentColumns = ["id"],
                childColumns = ["recordingSessionId"],
                entity = RecordingSession::class,
                onUpdate = ForeignKey.CASCADE,
                onDelete = ForeignKey.CASCADE,
            )
        ]
)
open class LocationSnapshot : RecordingBaseModel() {
    @ColumnInfo(name = "locationId")
    var locationId = 0L

    @ColumnInfo(name = "recordingSessionId")
    var recordingSessionId = 0L

    @ColumnInfo(name = "countSnapshot")
    var countSnapshot = 0

    @ColumnInfo(name = "timestamp")
    @TypeConverters(DateStringConverter::class)
    var timestamp: Date = Date()
}

// This is used instead of a nullable type.
// This way the locationSnapshot property in the LocationWithSnapshot
// class must not be nullable. Instead, this EmptyLocationSnapshot is
// used. It can be checked if this is set, without the downsides of a
// nullable value.
class EmptyLocationSnapshot : LocationSnapshot()

data class LocationWithSnapshot(
    @Embedded
    var location: Location,

    @Relation(
        parentColumn = "id",
        entityColumn = "locationId",
    )
    var locationSnapshot: LocationSnapshot = EmptyLocationSnapshot(),
)