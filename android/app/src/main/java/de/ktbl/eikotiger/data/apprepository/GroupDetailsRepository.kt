package de.ktbl.eikotiger.data.apprepository

import androidx.lifecycle.LiveData
import de.ktbl.eikotiger.data.mastermodel.location.Location
import de.ktbl.eikotiger.data.mastermodel.location.LocationDao
import de.ktbl.eikotiger.view.datainterface.productiondirection.group.IGroupDetailsDA
import timber.log.Timber
import javax.inject.Inject

class GroupDetailsRepository @Inject constructor(
        private val locationDao: LocationDao
) : IGroupDetailsDA {

    companion object {
        val TAG = GroupListRepository::class.simpleName
    }

    override fun loadLocationByID(id: Long): LiveData<Location> = locationDao.load(id)

    override suspend fun createNewlocationForStock(stockId: Long): Long {
        val newLocation = Location().apply {
            this.stockId = stockId
            this.count = 0
        }
        val ids = locationDao.insert(newLocation)
        return if (ids.size != 1) {
            Timber.tag(TAG).wtf("Could not create new location!")
            -1
        } else {
            ids[0]
        }
    }

    override suspend fun saveLocation(toSave: Location) {
        locationDao.update(toSave)
    }
}