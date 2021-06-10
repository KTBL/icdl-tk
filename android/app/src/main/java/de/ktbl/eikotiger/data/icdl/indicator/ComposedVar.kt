package de.ktbl.eikotiger.data.icdl.indicator

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import de.ktbl.eikotiger.data.icdl.KeyedBaseModel
import de.ktbl.eikotiger.data.json.models.JsonComposedVar


/**
 *
 */
@Entity(tableName = "ComposedVar",
        indices = [
            Index(value = ["id"]),
            Index(value = arrayOf("assessmentId"))
        ],
        foreignKeys = [
            ForeignKey(
                    entity = Assessment::class,
                    parentColumns = arrayOf("id"),
                    childColumns = arrayOf("assessmentId"),
                    onUpdate = ForeignKey.CASCADE,
                    onDelete = ForeignKey.CASCADE
            )
        ]
)
class ComposedVar : KeyedBaseModel() {
    /**
     * Property for [JsonComposedVar.expression]
     */
    @ColumnInfo(name = "expression")
    var expression: String? = null

    @ColumnInfo(name = "assessmentId")
    var assessmentId: Long? = null

    companion object {
        fun fromJsonComposedVar(jsonComposedVar: JsonComposedVar): ComposedVar {
            return ComposedVar().apply {
                this.expression = jsonComposedVar.expression
                this.__key = jsonComposedVar.__key
            }
        }
    }
}