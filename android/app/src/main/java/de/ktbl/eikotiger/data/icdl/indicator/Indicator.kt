package de.ktbl.eikotiger.data.icdl.indicator

import androidx.room.*
import de.ktbl.eikotiger.data.icdl.KeyedBaseModel
import de.ktbl.eikotiger.data.icdl.animalcategory.AnimalCategory
import de.ktbl.eikotiger.data.json.models.JsonIndicator

/**
 * @See JsonIndicator
 */
@Entity(tableName = "Indicator",
        indices = [
            Index(value = ["id"]),
            Index(value = ["animalCategoryId"])
        ],
        foreignKeys = [
            /**
             * Reference to the AnimalCategory.
             * In case the AnimalCategory is deleted, this indicator is no longer
             * of any use, since all Branches this indicator is used in are of
             * this one AnimalCategory.
             */
            ForeignKey(entity = AnimalCategory::class,
                       parentColumns = arrayOf("id"),
                       childColumns = arrayOf("animalCategoryId"),
                       onDelete = ForeignKey.CASCADE,
                       onUpdate = ForeignKey.CASCADE)

        ])
class Indicator : KeyedBaseModel() {
    /**
     * Property for [JsonIndicator.name]
     */
    @ColumnInfo(name = "name")
    var name: String? = null

    /**
     * Property for [JsonIndicator.description]
     */
    @ColumnInfo(name = "description")
    var description: String? = null

    /**
     * Property for [JsonIndicator.lastModified]
     */
    @ColumnInfo(name = "lastModified")
    var lastModified: String? = null

    /**
     * Property for [JsonIndicator.animalCategory}
     * Instead of referencing the name, here the [AnimalCategory] entity is referenced
     * by its id
     */
    @ColumnInfo(name = "animalCategoryId")
    var animalCategoryId: Long? = null

    /**
     * Property for [JsonIndicator.sorting]
     */
    @ColumnInfo(name = "sortOrder", defaultValue = "-1")
    var sortOrder = -1

    /**
     * Property for [JsonIndicator.whatAndWhy]
     */
    @ColumnInfo(name = "whatAndWhy")
    var whatAndWhy: String? = null

    /**
     * Property for [JsonIndicator.whenAndHowOften]
     */
    @ColumnInfo(name = "whenAndHowOften")
    var whenAndHowOften: String? = null

    /**
     * Property for [JsonIndicator.whichAndHowMany]
     */
    @ColumnInfo(name = "whichAndHowMany")
    var whichAndHowMany: String? = null

    /**
     * Property for [JsonIndicator.how]
     */
    @ColumnInfo(name = "how")
    var how: String? = null

    companion object {
        fun fromJsonIndicator(jsonIndicator: JsonIndicator): Indicator {
            return Indicator().apply {
                this.__key = jsonIndicator.__key
                this.description = jsonIndicator.description
                this.name = jsonIndicator.name
                this.lastModified = jsonIndicator.lastModified
                this.whatAndWhy = jsonIndicator.whatAndWhy
                this.whenAndHowOften = jsonIndicator.whenAndHowOften
                this.whichAndHowMany = jsonIndicator.whichAndHowMany
                this.how = jsonIndicator.how
            }
        }
    }
}

@Entity(tableName = "IndicatorBranchCrossRef", primaryKeys = ["branchId", "indicatorId"])
data class IndicatorBranchCrossRef(
        val branchId: Long,
        val indicatorId: Long,
)

data class IndicatorWithAssessment(
        @Embedded
        var indicator: Indicator,

        @Relation(
                parentColumn = "id",
                entityColumn = "indicatorId",
                entity = Assessment::class,
        )
        var assessment: CompleteAssessment
)