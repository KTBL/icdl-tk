package de.ktbl.eikotiger.data.icdl.indicator

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.RewriteQueriesToDropUnusedColumns

@Dao
abstract class VarDao {
    @Insert
    abstract suspend fun insert(toInsertVar: Var): Long

    @Query(
        "SELECT Var.name, Var.thumbnail, Var.images, Var.shortDescription, " +
                "Var.longDescription, Var.groupId, Var.__key, Var.id, Var.deleted, " +
                "Var.archived, Var.inserted, Var.updated " +
                "FROM Var\n" +
                "LEFT JOIN `Group` ON `Group`.id = Var.groupId\n" +
                "LEFT JOIN Assessment ON Assessment.id = `Group`.assessmentId\n" +
                "WHERE  Assessment.indicatorId = :indicatorId"
    )
    abstract fun loadOptionsByIndicatorId(indicatorId: Long): LiveData<List<Var>>

    @Query("SELECT * FROM Var WHERE id = :id")
    abstract fun loadOptionById(id: Long): LiveData<Var>

    @RewriteQueriesToDropUnusedColumns
    @Query(
        "SELECT AnimalCategory.__key FROM Var\n" +
                "LEFT JOIN `Group` ON `Group`.id = Var.groupId\n" +
                "LEFT JOIN Assessment ON Assessment.id = `Group`.assessmentId\n" +
                "LEFT JOIN Indicator ON Indicator.id = Assessment.indicatorId\n" +
                "LEFT JOIN AnimalCategory ON AnimalCategory.id = Indicator.animalCategoryId\n" +
                "WHERE Var.id = :id"
    )
    abstract suspend fun getFolderKeyById(id: Long): String

}