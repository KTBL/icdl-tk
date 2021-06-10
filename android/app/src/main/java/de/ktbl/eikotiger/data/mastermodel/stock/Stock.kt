package de.ktbl.eikotiger.data.mastermodel.stock

import androidx.room.*
import de.ktbl.eikotiger.data.BaseModel
import de.ktbl.eikotiger.data.icdl.animalcategory.Branch
import de.ktbl.eikotiger.data.mastermodel.instance.Instance

/**
 * Stock i.e. animal live stock handles persistence of farm attributes
 * like general sections and animal counts in subsections
 *
 *
 * The name and level attributes are ICDL dependent, and can be processed semantically
 * i.e. formulas and indicator choice can be depdendent on this level
 * each stock is connected to a production type for now (Bbut not company)
 *
 *
 * User entered sections contain name and a fixed set oft attributes
 * currently number of animals
 *
 *
 * sg
 */
@Entity(tableName = "stock",
        indices = [
            Index(value = ["branchId"]),
            Index(value = ["instanceId"]),
            Index(value = ["id"], unique = true)
        ],
        foreignKeys = [
            ForeignKey(entity = Instance::class,
                       parentColumns = ["id"],
                       childColumns = ["instanceId"],
                       onDelete = ForeignKey.CASCADE,
                       onUpdate = ForeignKey.CASCADE
            ),
            ForeignKey(entity = Branch::class,
                       parentColumns = ["id"],
                       childColumns = ["branchId"],
                       onDelete = ForeignKey.CASCADE,
                       onUpdate = ForeignKey.CASCADE
            )
        ]
)
class Stock : BaseModel() {
    @JvmField
    @ColumnInfo(name = "instanceId")
    var instanceId: Long? = null

    @ColumnInfo(name = "branchId")
    var branchId: Long? = null

    @Ignore
    var animalCount = 0

    companion object {
        //Place to enter defaults for testing on startup
        // returns List if objects that can be processed by insertAll
        fun populateData(): Array<Stock> {
            //supposed to add instances of stock entity here
            return arrayOf(
                    Stock(),
                    Stock(),
                    Stock())
        }
    }
}

/**
 *
 */
data class StockWithBranch(
        @Embedded val stock: Stock,
        @Relation(
                parentColumn = "branchId",
                entityColumn = "id",
        )
        val branch: Branch
)