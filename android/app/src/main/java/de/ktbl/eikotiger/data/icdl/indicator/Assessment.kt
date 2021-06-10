package de.ktbl.eikotiger.data.icdl.indicator

import androidx.room.*
import de.ktbl.eikotiger.data.BaseModel
import de.ktbl.eikotiger.data.json.models.JsonAssessment

/**
 * Class representing an Assessment element within an Indicator
 * @see JsonAssessment
 */
@Entity(tableName = "Assessment",
        indices = [
            Index(value = ["id"]),
            Index(value = ["indicatorId"], unique = false)
        ],
        foreignKeys = [
            ForeignKey(entity = Indicator::class,
                       parentColumns = arrayOf("id"),
                       childColumns = arrayOf("indicatorId"),
                       onDelete = ForeignKey.CASCADE,
                       onUpdate = ForeignKey.CASCADE
            )
        ]) //set to match tables
class Assessment : BaseModel() {

    /**
     * Property for [JsonAssessment.defaultType]
     */
    @ColumnInfo(name = "defaultType")
    var defaultType: String? = null

    /**
     * Property for [JsonAssessment.typeChangeable]
     */
    @ColumnInfo(name = "typeChangeable", defaultValue = "false")
    var typeChangeable: Boolean = false

    /**
     * Property for [JsonAssessment.presentation]
     */
    @ColumnInfo(name = "presentation")
    var presentation: String? = null

    /**
     * Property for [JsonAssessment.presentationChangeable]
     */
    @ColumnInfo(name = "presentation_changeable", defaultValue = "false")
    var presentationChangeable: Boolean = false

    /**
     * Reference back to the Indicator this Assessment is part of
     */
    @ColumnInfo(name = "indicatorId")
    var indicatorId: Long = -1

    companion object {
        fun fromJsonAssessment(jsonAssessment: JsonAssessment): Assessment {
            return Assessment().apply {
                this.defaultType = jsonAssessment.defaultType
                this.presentation = jsonAssessment.presentation
                this.presentationChangeable = jsonAssessment.presentationChangeable
                this.typeChangeable = jsonAssessment.typeChangeable
            }
        }
    }
}

data class CompleteAssessment(
        @Embedded var assessment: Assessment,
        @Relation(
                entity = Group::class,
                parentColumn = "id",
                entityColumn = "assessmentId",
        )
        var groups: List<GroupWithVars>,
        @Relation(
                entity = ComposedVar::class,
                parentColumn = "id",
                entityColumn = "assessmentId"
        )
        var composedVars: List<ComposedVar>
)
