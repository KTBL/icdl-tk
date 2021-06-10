package de.ktbl.eikotiger.data.mastermodel.instance

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
abstract class InstanceDao {

    @Delete
    abstract suspend fun delete(instance: Instance)

    @Update
    abstract suspend fun update(vararg instances: Instance): Int

    @Insert(onConflict = OnConflictStrategy.ABORT)
    abstract suspend fun insert(vararg instances: Instance): List<Long>

    @Query("SELECT * FROM instance WHERE id = :instanceId")
    abstract fun load(instanceId: Long): LiveData<Instance>

    @Query("SELECT * FROM instance WHERE 1=1 ORDER BY species")
    abstract fun loadAll(): LiveData<List<Instance>>

    @SuppressWarnings(RoomWarnings.CURSOR_MISMATCH)
    @Query("SELECT id, name, species FROM instance ORDER BY species")
    abstract fun loadAllBasicCategories(): LiveData<List<BasicCategory>>

    @Transaction
    @Query("Select * FROM instance WHERE id = :instanceId")
    abstract fun loadInstanceWithStocksAndBranches(instanceId: Long): LiveData<InstanceWithStocksAndBranches>
}