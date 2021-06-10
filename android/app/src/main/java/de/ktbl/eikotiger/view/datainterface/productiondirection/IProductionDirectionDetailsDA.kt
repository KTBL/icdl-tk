package de.ktbl.eikotiger.view.datainterface.productiondirection

import androidx.lifecycle.LiveData
import de.ktbl.eikotiger.data.icdl.animalcategory.AnimalCategory
import de.ktbl.eikotiger.data.mastermodel.instance.Instance
import de.ktbl.eikotiger.data.mastermodel.instance.InstanceWithStocksAndBranches

/**
 * Data Access Interface for ProductionDirectionDetailsVM
 *
 * @see de.ktbl.eikotiger.view.viewmodel.productiondirection.ProductionDirectionDetailsVM
 *
 * This interface is possible subject to major changes.
 */
interface IProductionDirectionDetailsDA {

    /**
     * Loads an Instance including Stock And Branch elements associated with it
     *
     * @param id id of the Instance to be loaded
     * @return instantly returns a LiveData element of type InstanceWithStocksAndBranches
     *
     */
    fun loadInstanceWithStocksAndBranches(id: Long): LiveData<InstanceWithStocksAndBranches>

    /**
     * Loads an AnimalCategory by Id
     *
     * @param id id of the AnimalCategory to be loaded
     * @return instantly returns a LiveData element of type AnimalCategory. Filled asynchronously.
     */
    fun loadAnimalCategory(id: Long): LiveData<AnimalCategory>

    /**
     * Creates and initializes a new Instance of the given AnimalCategory (by ID).
     * This includes the creation of any Stocks necessary.
     *
     * @param animalCategoryId id of the AnimalCategory the to be created Instance is of
     * @return asynchronously returns the ID of the newly created Instance
     */
    suspend fun initializeNewInstance(animalCategoryId: Long): Long

    /**
     * Asynchronously saves a given Instance object to the database
     *
     * @param instance Instance object to save
     * @return asynchronously returns and Boolean indicating success or failure of the update
     *          process
     */
    suspend fun updateInstance(instance: Instance): Boolean
}