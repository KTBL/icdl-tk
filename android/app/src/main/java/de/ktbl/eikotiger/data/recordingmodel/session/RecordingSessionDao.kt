package de.ktbl.eikotiger.data.recordingmodel.session

import androidx.room.Dao
import androidx.room.Insert

@Dao
abstract class RecordingSessionDao {
    @Insert
    abstract suspend fun insert(session: RecordingSession): Long
}