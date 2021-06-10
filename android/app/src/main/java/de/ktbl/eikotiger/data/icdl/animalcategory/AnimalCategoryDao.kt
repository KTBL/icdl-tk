package de.ktbl.eikotiger.data.icdl.animalcategory

import androidx.lifecycle.LiveData
import androidx.room.*
import de.ktbl.eikotiger.data.mastermodel.instance.BasicCategory

@Dao
abstract class AnimalCategoryDao {

    @Query("SELECT * FROM AnimalCategory WHERE id = :animalCategoryId")
    abstract suspend fun getById(animalCategoryId: Long): AnimalCategory

    @Query("SELECT * FROM AnimalCategory WHERE id = :animalCategoryId")
    abstract fun loadById(animalCategoryId: Long): LiveData<AnimalCategory>

    @Query("SELECT * FROM AnimalCategory")
    abstract fun loadAll(): LiveData<List<AnimalCategory>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract suspend fun insert(vararg categories: AnimalCategory): List<Long>

    @Update
    abstract suspend fun updateAll(vararg categories: AnimalCategory)

    @Query("SELECT * FROM animalCategory ORDER BY animalCategory.species")
    abstract fun loadAllBasicCategories(): LiveData<List<BasicCategory>>

    @Transaction
    @Query("SELECT * FROM AnimalCategory WHERE id = :animalCategoryId")
    abstract fun loadWithBranchesById(animalCategoryId: Long): LiveData<AnimalCategoryWithBranches>

    @Transaction
    @Query("SELECT * FROM AnimalCategory WHERE id = :animalCategoryId")
    abstract suspend fun getWithBranchesById(animalCategoryId: Long): AnimalCategoryWithBranches

}