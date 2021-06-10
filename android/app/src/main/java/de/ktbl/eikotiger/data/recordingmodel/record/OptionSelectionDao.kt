package de.ktbl.eikotiger.data.recordingmodel.record

import androidx.room.Dao
import androidx.room.Insert

@Dao
abstract class OptionSelectionDao {
    @Insert
    abstract suspend fun insert(optionSelectionDao: OptionSelection)
}