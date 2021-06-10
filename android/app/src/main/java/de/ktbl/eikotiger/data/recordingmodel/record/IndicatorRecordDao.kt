package de.ktbl.eikotiger.data.recordingmodel.record

import androidx.room.Dao
import androidx.room.Insert

@Dao
abstract class IndicatorRecordDao {
    @Insert
    abstract suspend fun insert(record: IndicatorRecord): Long
}