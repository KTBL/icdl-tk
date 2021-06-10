package de.ktbl.eikotiger.data.icdl.indicator

import androidx.room.*
import de.ktbl.eikotiger.data.BaseModel

/**
 *
 */
@Entity(
        tableName = "Range",
        indices = [
            Index(value = ["id"]),
            Index(value = ["formulaId"])
        ],
        foreignKeys = [
            ForeignKey(
                    entity = Formula::class,
                    parentColumns = ["id"],
                    childColumns = ["formulaId"],
                    onDelete = ForeignKey.CASCADE,
                    onUpdate = ForeignKey.CASCADE
            )
        ]
)
class Range : BaseModel() {
    @ColumnInfo(name = "unit")
    var unit = "%"

    @ColumnInfo(name = "description")
    var description = ""

    @ColumnInfo(name = "visualLowerBound")
    var visualLowerBound = 0.0

    @ColumnInfo(name = "visualUpperBound")
    var visualUpperBound = 100.0

    @Embedded
    var targetRange: TargetAlertRange? = null

    @Embedded
    var alertrange: TargetAlertRange? = null

    @Embedded
    var warningRange: WarningRange? = null

    @ColumnInfo(name = "formulaId")
    var formulaId: Long = -1

}

data class TargetAlertRange(
        @ColumnInfo(name = "tag")
        val tag: String = "",

        @ColumnInfo(name = "description")
        val description: String = "",

        @ColumnInfo(name = "lowerBound")
        val lowerBound: Double,

        @ColumnInfo(name = "upperBound")
        val upperBound: Double
)

data class WarningRange(
        @ColumnInfo(name = "tag")
        val tag: String = "",

        @ColumnInfo(name = "description")
        val description: String = ""
)