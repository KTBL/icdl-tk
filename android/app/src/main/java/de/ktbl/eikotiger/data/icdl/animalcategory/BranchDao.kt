package de.ktbl.eikotiger.data.icdl.animalcategory

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
abstract class BranchDao {
    @Query("SELECT * FROM Branch WHERE id = :branchId")
    abstract fun loadById(branchId: Long): LiveData<Branch>

    @Query("SELECT * FROM Branch")
    abstract fun loadAll(): LiveData<List<Branch>>

    @Insert
    abstract suspend fun insertAll(vararg branches: Branch): List<Long>

    @Update
    abstract fun updateAll(vararg branches: Branch)
}