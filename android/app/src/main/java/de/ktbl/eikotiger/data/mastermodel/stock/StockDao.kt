package de.ktbl.eikotiger.data.mastermodel.stock

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
abstract class StockDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    abstract suspend fun insert(vararg stocks: Stock): List<Long>

    @Delete
    abstract suspend fun delete(vararg stocks: Stock)

    @Update
    abstract suspend fun update(vararg stocks: Stock)

    @Query("SELECT * FROM stock WHERE id = :stockId")
    abstract fun load(stockId: Long): LiveData<Stock>

    @Query("SELECT * FROM stock WHERE id IN (:stockIds)")
    abstract fun loadAllByIds(stockIds: IntArray?): LiveData<List<Stock>>

    @Query("SELECT * FROM stock WHERE 1=1")
    abstract fun loadAll(): LiveData<List<Stock>>

    @Query("SELECT * FROM stock WHERE instanceId = :id")
    abstract fun loadStockByInstanceId(id: Long): LiveData<List<Stock>>

    @Transaction
    @Query("SELECT * FROM Stock WHERE id = :id")
    abstract fun loadStockWithBranchById(id: Long): LiveData<StockWithBranch>
}