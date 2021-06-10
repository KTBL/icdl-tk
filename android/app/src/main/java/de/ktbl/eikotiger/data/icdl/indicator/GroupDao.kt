package de.ktbl.eikotiger.data.icdl.indicator

import androidx.room.Dao
import androidx.room.Insert


@Dao
abstract class GroupDao {
    @Insert
    abstract suspend fun insert(group: Group): Long
}