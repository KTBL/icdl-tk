package de.ktbl.eikotiger.data.mastermodel.location

import androidx.room.*
import de.ktbl.eikotiger.data.BaseModel
import de.ktbl.eikotiger.data.mastermodel.stock.Stock

@Entity(tableName = "location",
        indices = [
            Index(value = ["id"]),
            Index(value = ["stockId"])
        ],
        foreignKeys = [
            ForeignKey(entity = Stock::class,
                       parentColumns = ["id"],
                       childColumns = ["stockId"],
                       onDelete = ForeignKey.CASCADE)
        ]
) //set to match tables
class Location : BaseModel {
    //Associate animal stock with this prod.Type/part of company
    @JvmField
    @ColumnInfo(name = "stockId")
    var stockId: Long? = null

    //Editable Label
    @JvmField
    @ColumnInfo(name = "editLabel")
    var editLabel: String? = null

    //Animal count
    @JvmField
    @ColumnInfo(name = "count")
    var count: Int = 0

    @JvmField
    @ColumnInfo(name = "display_group_area")
    var display_group_area //0 - hidden, 1 - available
            : Int? = null

    //Input Length
    @JvmField
    @ColumnInfo(name = "length")
    var length: Double? = null

    //Input width
    @JvmField
    @ColumnInfo(name = "width")
    var width: Double? = null

    //Input width
    @JvmField
    @ColumnInfo(name = "area")
    var area: Double? = null

    //Direct input of Area Value or not
    @JvmField
    @ColumnInfo(name = "directInput")
    var directInput: Boolean? = null

    //Editable Description
    @JvmField
    @ColumnInfo(name = "editDescription")
    var editDescription: String? = null

    /**
     * standard constructor for autognerated entities
     */
    constructor()

    //Stub for delete
    @Ignore
    constructor(id: Long) {
        this.id = id
    }

    @Ignore
    constructor(id: Long?, editLabel: String?, label: String?) {
        this.id = id!!
        if (label == null) {
            this.editLabel = ""
        } else {
            this.editLabel = label
        }
        if (editLabel == null) {
            this.editLabel = "-"
        } else {
            this.editLabel = editLabel
        }
    }

    companion object {
        //Place to enter defaults for testing on startup
        // returns List if objects that can be processed by insertAll
        fun populateData(): Array<Location> {
            //supposed to add instances of location entity here
            return arrayOf(
                    Location(0L, "INDI0001", "Test location 1"),
                    Location(0L, "INDI0010", "Test location 2"),
                    Location(0L, "INDI0011", "Test location 3"))
        }
    }
}