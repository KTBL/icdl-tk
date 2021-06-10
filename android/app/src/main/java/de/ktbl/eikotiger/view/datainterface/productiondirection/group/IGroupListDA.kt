package de.ktbl.eikotiger.view.datainterface.productiondirection.group

import androidx.lifecycle.LiveData
import de.ktbl.eikotiger.data.mastermodel.location.Location

/**
 *
 */
interface IGroupListDA {

    /**
     * Loads all available Locations associated to a Stock identified via id
     *
     * @param stockId id of the stock of which all available Location objects should
     *              be loaded.
     * @return instantly returns a LiveData element with
     *          List<Location> as type parameter.
     *          The loading process itself runs in a background
     *          thread and fills the returned LiveData
     *          element upon finish.
     */
    fun loadAllLocationsBySectionId(stockId: Long): LiveData<List<Location>>

    /**
     * Asynchronously deletes all Locations identified via the given Location ids
     *
     * @param toDelete Ids of Locations to delete
     */
    suspend fun deleteLocationsByIds(toDelete: List<Location>)
}