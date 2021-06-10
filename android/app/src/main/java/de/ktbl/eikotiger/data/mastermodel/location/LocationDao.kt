package de.ktbl.eikotiger.data.mastermodel.location

import androidx.lifecycle.LiveData
import androidx.room.*
import de.ktbl.eikotiger.data.recordingmodel.location.LocationWithSnapshot

@Dao
abstract class LocationDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    abstract suspend fun insert(vararg locations: Location): List<Long>

    @Update
    abstract suspend fun update(vararg location: Location)

    @Delete
    abstract suspend fun delete(vararg locations: Location)

    @Query("SELECT * FROM location WHERE id = :locationId")
    abstract fun load(locationId: Long): LiveData<Location>

    @Query("SELECT * FROM location WHERE stockId = :sectionId")
    abstract fun loadBySectionId(sectionId: Long): LiveData<List<Location>>

    @Query("SELECT * FROM location WHERE stockId = :stockId")
    abstract fun getByStockId(stockId: Long?): List<Location>

    @Query("SELECT * FROM location")
    abstract fun loadAll(): LiveData<List<Location>>

    @Query("SELECT * FROM location WHERE id IN (:locationIds)")
    abstract fun loadAllByIds(locationIds: IntArray?): LiveData<List<Location>>

    @Transaction
    @Query(
        "SELECT * FROM Location " +
                "Left JOIN LocationSnapshot ON LocationSnapshot.locationId = Location.id " +
                "WHERE Location.stockId = :stockId AND LocationSnapshot.timestamp = :dateString"
    )
    abstract suspend fun getLocationsWithSnapshots(stockId: Long, dateString: String): List<LocationWithSnapshot>

}