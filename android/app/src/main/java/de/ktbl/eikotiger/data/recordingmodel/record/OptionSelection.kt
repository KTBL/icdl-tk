package de.ktbl.eikotiger.data.recordingmodel.record

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import de.ktbl.eikotiger.data.icdl.indicator.Var

/**
 * Represents an Indicator Var which has been selected while recording
 * an Indicator. This is basically a CrossReference only containing the
 * Id of the Var and the Id of the IndicatorRecord
 */
@Entity(
        tableName = "OptionSelection",
        primaryKeys = [
            "varId",
            "indicatorRecordId"
        ],
        indices = [
            Index("indicatorRecordId", "varId"),
        ],
        foreignKeys = [
            ForeignKey(entity = IndicatorRecord::class,
                       parentColumns = ["id"],
                       childColumns = ["indicatorRecordId"],
                       onDelete = ForeignKey.CASCADE,
                       onUpdate = ForeignKey.CASCADE
            ),
            ForeignKey(
                    entity = Var::class,
                    parentColumns = ["id"],
                    childColumns = ["varId"],
                    onDelete = ForeignKey.CASCADE,
                    onUpdate = ForeignKey.CASCADE
            )
        ]
)
data class OptionSelection(
        @ColumnInfo(name = "varId")
        var varId: Long = 0L,

        @ColumnInfo(name = "indicatorRecordId")
        var indicatorRecordId: Long = 0L,
)