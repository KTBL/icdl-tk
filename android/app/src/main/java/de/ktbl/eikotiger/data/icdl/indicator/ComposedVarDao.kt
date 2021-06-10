package de.ktbl.eikotiger.data.icdl.indicator

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
abstract class ComposedVarDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    abstract suspend fun insert(composedVar: ComposedVar): Long

    @Query("SELECT * FROM ComposedVar WHERE id = :branchId")
    abstract fun load(branchId: String?): LiveData<ComposedVar?>?

    @get:Query("SELECT * FROM ComposedVar")
    abstract val all: LiveData<List<ComposedVar?>?>?

    @Query("SELECT * FROM ComposedVar WHERE id IN (:setIds)")
    abstract fun loadAllByIds(setIds: IntArray?): LiveData<List<ComposedVar?>?>?

    @Query("SELECT * FROM ComposedVar WHERE 1=1")
    abstract fun loadAll(): LiveData<List<ComposedVar?>?>?

    //LiveData can't be called from repositorythread
    @Query("SELECT * FROM ComposedVar WHERE id LIKE :row")
    abstract fun previous(row: Long): ComposedVar?

    //    @Query("UPDATE ComposedVar SET installed = strftime('%s','now'), updated = strftime('%s','now')  WHERE id LIKE :row")
    //    public abstract void confirmInstall(long row);
    @Query("UPDATE ComposedVar SET updated = strftime('%s','now')  WHERE id LIKE :row")
    abstract fun confirmUpdate(row: Long)
}