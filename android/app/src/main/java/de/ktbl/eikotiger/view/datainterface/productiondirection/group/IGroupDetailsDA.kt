package de.ktbl.eikotiger.view.datainterface.productiondirection.group

import androidx.lifecycle.LiveData
import de.ktbl.eikotiger.data.mastermodel.location.Location

/**
 * Data Access Interface for [GroupDetailVM][de.ktbl.eikotiger.view.viewmodel.productiondirection.group.GroupDetailVM]
 *
 */
interface IGroupDetailsDA {

    /**
     * Loads a Location object by a given Location object id. If the id
     * is equal to -1, a new location is created and returned
     *
     * @param id id of the Location object to load.
     * @return instantly returns a LiveData element with
     *          Location as type parameter.
     *          The loading process itself runs in a background
     *          thread and fills the returned LiveData
     *          element upon finish.
     */
    fun loadLocationByID(id: Long): LiveData<Location>

    /**
     * Creates a new Location and associates it with the Stock identified
     * by the given id
     *
     * @param stockId id of the stock the newly created Location should be associated
     *              with
     *
     * @return instantly returns a LiveData element with Long (for the database
     *          id of the newly created Location) as type parameter.
     *          The loading process itself runs in a background
     *          thread and fills the returned LiveData
     *          element upon finish.
     */
    suspend fun createNewlocationForStock(stockId: Long): Long

    /**
     * Asynchronously saves a given Location to the database.
     *
     * @param toSave the Location object to save.
     */
    suspend fun saveLocation(toSave: Location)
}