package de.ktbl.eikotiger.data.mastermodel.company

import androidx.room.*


/**
 * DAO class for {Company}
 */
@Dao
interface CompanyDao {
    @Update
    suspend fun update(company: Company?)

    @Query("SELECT * FROM company WHERE id = :companyId")
    suspend fun load(companyId: Long?): Company?

    @Query("SELECT * FROM company WHERE 1=1")
    suspend fun loadAll(): List<Company>

    @Insert
    fun insert(company: Company)

    @Delete
    fun delete(company: Company)
}