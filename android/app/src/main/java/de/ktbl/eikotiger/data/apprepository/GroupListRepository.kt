package de.ktbl.eikotiger.data.apprepository

import androidx.lifecycle.LiveData
import de.ktbl.eikotiger.data.mastermodel.location.Location
import de.ktbl.eikotiger.data.mastermodel.location.LocationDao
import de.ktbl.eikotiger.view.datainterface.productiondirection.group.IGroupListDA
import javax.inject.Inject

class GroupListRepository @Inject constructor(
        private val locationDao: LocationDao,
) : IGroupListDA {

    companion object {
        val TAG = GroupListRepository::class.simpleName
    }

    override fun loadAllLocationsBySectionId(stockId: Long): LiveData<List<Location>> {
        return locationDao.loadBySectionId(stockId)
    }

    override suspend fun deleteLocationsByIds(toDelete: List<Location>) {
        val toExpand = toDelete.toTypedArray()
        locationDao.delete(*toExpand)
    }
}