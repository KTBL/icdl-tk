package de.ktbl.eikotiger.data

import androidx.room.ColumnInfo
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import de.ktbl.eikotiger.data.db.converters.DateConverter
import java.util.*

open class BaseModel {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Long = 0

    @ColumnInfo(name = "archived")
    var active: Int = 0

    @ColumnInfo(name = "deleted")
    var deleted: Int = 0

    @ColumnInfo(name = "inserted")
    @TypeConverters(DateConverter::class)
    var inserted: Date? = Date()

    @ColumnInfo(name = "updated")
    @TypeConverters(DateConverter::class)
    var updated: Date? = Date()
}