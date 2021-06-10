package de.ktbl.eikotiger.data.recordingmodel.location

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Update

@Dao
abstract class LocationSnapshotDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    abstract suspend fun insert(snapshot: LocationSnapshot): Long

    @Update
    abstract suspend fun update(snapshot: LocationSnapshot): Int
}