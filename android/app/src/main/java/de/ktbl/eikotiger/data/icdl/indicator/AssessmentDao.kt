package de.ktbl.eikotiger.data.icdl.indicator

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
abstract class AssessmentDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    abstract suspend fun insert(assessments: Assessment): Long

    @Query("SELECT * FROM Assessment WHERE id = :branchId")
    abstract fun load(branchId: String?): LiveData<Assessment?>?

    @get:Query("SELECT * FROM Assessment")
    abstract val all: LiveData<List<Assessment?>?>?

    @Query("SELECT * FROM Assessment WHERE id IN (:setIds)")
    abstract fun loadAllByIds(setIds: IntArray?): LiveData<List<Assessment?>?>?

    @Query("SELECT * FROM Assessment WHERE 1=1")
    abstract fun loadAll(): LiveData<List<Assessment?>?>?
}