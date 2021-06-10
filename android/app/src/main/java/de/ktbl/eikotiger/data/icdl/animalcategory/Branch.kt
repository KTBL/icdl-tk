package de.ktbl.eikotiger.data.icdl.animalcategory

import androidx.room.*
import de.ktbl.eikotiger.data.icdl.KeyedBaseModel
import de.ktbl.eikotiger.data.icdl.indicator.Indicator
import de.ktbl.eikotiger.data.icdl.indicator.IndicatorBranchCrossRef
import de.ktbl.eikotiger.data.json.models.JsonBranch

@Entity(tableName = "Branch",
        indices = [Index(value = ["animalCategoryId"])],
        foreignKeys = [ForeignKey(entity = AnimalCategory::class,
                                  parentColumns = arrayOf("id"),
                                  childColumns = arrayOf("animalCategoryId"),
                                  onDelete = ForeignKey.CASCADE)]) //set to match tables
class Branch : KeyedBaseModel() {
    @ColumnInfo(name = "name")
    var name: String? = null

    @ColumnInfo(name = "description")
    var description: String? = null

    @ColumnInfo(name = "animalCategoryId")
    var animalCategoryId: Long? = null

    companion object {
        fun fromJsonBranch(jsonBranch: JsonBranch): Branch {
            return Branch().apply {
                __key = jsonBranch.__key
                name = jsonBranch.name
                description = jsonBranch.description
            }
        }
    }
}

data class BranchWithIndicators(
        @Embedded val branch: Branch,
        @Relation(
                parentColumn = "branchId",
                entityColumn = "indicatorId",
                associateBy = Junction(IndicatorBranchCrossRef::class)
        )
        val indicator: List<Indicator>
)