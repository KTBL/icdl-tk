package de.ktbl.eikotiger.data.icdl.indicator

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
abstract class IndicatorDao {
    @Query("SELECT * FROM Indicator WHERE id = :id")
    abstract fun loadById(id: Long): LiveData<Indicator>

    @Insert(onConflict = OnConflictStrategy.ABORT)
    abstract suspend fun insert(vararg indicators: Indicator): List<Long>

    @Insert(onConflict = OnConflictStrategy.ABORT)
    abstract suspend fun insertIndicatorBranchCrossRef(vararg crossRef: IndicatorBranchCrossRef)

    @Update
    abstract suspend fun updateAll(vararg indicators: Indicator)

    // Even though the columns of IndicatorBranchCrossRef
    // are SELECTed as well, we use StarProjection.
    // The columns of IndicatorBranchCrossRef do not collide
    // the columns of Indicator. Thus, we simply use
    // the Rewrite... annotation.
    @RewriteQueriesToDropUnusedColumns
    @Query(
        "SELECT * FROM Indicator\n" +
                "LEFT JOIN IndicatorBranchCrossRef on Indicator.id = IndicatorBranchCrossRef.indicatorId\n" +
                "WHERE IndicatorBranchCrossRef.branchId = :branchID"
    )
    abstract fun loadByBranchId(branchID: Long): LiveData<List<Indicator>>

    @Query(
        "SELECT AnimalCategory.__key FROM Indicator\n" +
                "LEFT JOIN AnimalCategory ON AnimalCategory.id = Indicator.animalCategoryId\n" +
                "WHERE Indicator.id = :id"
    )
    abstract suspend fun getFolderKeyById(id: Long): String

    @Transaction
    @Query("SELECT * From Indicator " +
                   "LEFT JOIN IndicatorBranchCrossRef on Indicator.id = IndicatorBranchCrossRef.indicatorId " +
                   "WHERE IndicatorBrancHCrossRef.branchId = :branchID"
    )
    abstract suspend fun loadAllWithAssessmentByBranchId(branchID: Long): List<IndicatorWithAssessment>


}