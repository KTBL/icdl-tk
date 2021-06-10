package de.ktbl.eikotiger.data.recordingmodel

import androidx.room.ColumnInfo
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import de.ktbl.eikotiger.data.db.converters.DateConverter
import java.util.*

open class RecordingBaseModel {
    @ColumnInfo(name = "id")
    @PrimaryKey(autoGenerate = true)
    var id = 0L

    @ColumnInfo(name = "inserted")
    @TypeConverters(DateConverter::class)
    var inserted: Date? = Date()

    @ColumnInfo(name = "updated")
    @TypeConverters(DateConverter::class)
    var updated: Date? = Date()
}