package de.ktbl.eikotiger.data.icdl.indicator

import androidx.room.*
import de.ktbl.eikotiger.data.BaseModel

@Entity(
        tableName = "Formula",
        indices = [
            Index(value = ["id"]),
            Index(value = ["indicatorId"])
        ],
        foreignKeys = [
            ForeignKey(
                    entity = Indicator::class,
                    parentColumns = ["id"],
                    childColumns = ["indicatorId"],
                    onDelete = ForeignKey.CASCADE,
                    onUpdate = ForeignKey.CASCADE
            )
        ]
)
class Formula : BaseModel() {
    @ColumnInfo(name = "name")
    var name = ""

    @ColumnInfo(name = "description")
    var description = ""

    @ColumnInfo(name = "expression")
    var expression = ""

    @ColumnInfo(name = "indicatorId")
    var indicatorId: Long = -1
}

data class FormulaWithRanges(
        @Embedded val formula: Formula,
        @Relation(
                entity = Range::class,
                parentColumn = "id",
                entityColumn = "formulaId"
        )
        val formulas: List<Range>
)