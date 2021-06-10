package de.ktbl.eikotiger.data.mastermodel.instance

import androidx.room.*
import de.ktbl.eikotiger.data.BaseModel
import de.ktbl.eikotiger.data.icdl.animalcategory.AnimalCategory
import de.ktbl.eikotiger.data.mastermodel.stock.Stock
import de.ktbl.eikotiger.data.mastermodel.stock.StockWithBranch

@Entity(tableName = "instance",
        indices = [
            Index(
                    value = ["id"],
                    unique = true
            ),
            Index(value = ["animalCategoryId"])
        ],
        foreignKeys = [
            ForeignKey(
                    entity = AnimalCategory::class,
                    childColumns = ["animalCategoryId"],
                    parentColumns = ["id"],
                    onUpdate = ForeignKey.CASCADE,
                    onDelete = ForeignKey.CASCADE
            )
        ]
)
class Instance : BaseModel() {

    // Field is simply copied from AnimalCategory
    // Since this is needed to group Elements regarding species
    @ColumnInfo(name = "species")
    var species: String = ""

    @ColumnInfo(name = "name")
    var customName: String? = null

    @ColumnInfo(name = "animalCategoryId")
    var animalCategoryId: Long = 0

    companion object {
        //Place to enter defaults for testing on startup
        // returns List if objects that can be processed by insertAll
        fun populateData(): Array<Instance> {
            //supposed to add instances of instance entity here
            return arrayOf()
        }

        fun createFromAnimalCategory(animalCategory: AnimalCategory): Instance {
            return Instance().apply {
                this.species = animalCategory.species ?: ""
                this.customName = animalCategory.name
                this.animalCategoryId = animalCategory.id
            }
        }
    }
}

data class InstanceWithStocksAndBranches(
        @Embedded val instance: Instance,
        @Relation(
                entity = Stock::class,
                entityColumn = "instanceId",
                parentColumn = "id"
        )
        val stockWithBranches: List<StockWithBranch>
)