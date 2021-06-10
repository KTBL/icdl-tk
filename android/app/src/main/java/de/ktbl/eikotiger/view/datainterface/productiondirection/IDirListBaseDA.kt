package de.ktbl.eikotiger.view.datainterface.productiondirection

import androidx.lifecycle.LiveData
import de.ktbl.eikotiger.data.mastermodel.instance.BasicCategory

/**
 * DataAccess Interface used for all subclasses of DirectionListBaseVM.
 *
 * @see de.ktbl.eikotiger.view.viewmodel.productiondirection.DirectionListBaseVM
 * @see de.ktbl.eikotiger.view.viewmodel.productiondirection.DirListModeVM
 * @see de.ktbl.eikotiger.view.viewmodel.productiondirection.DirSelectionModeVM
 * @see de.ktbl.eikotiger.view.viewmodel.productiondirection.DirAddingModeVM
 */
interface IDirListBaseDA {

    /**
     * Loads all available, non-archived AnimalCategory Instances.
     *
     * @return instantly returns a LiveData element with
     *          List<BasicCategorization> as type parameter.
     *          The loading process itself runs in a background
     *          thread and fills the returned LiveData
     *          element upon finish.
     */
    fun loadAllAvailableInstancesAsBasicCategory(): LiveData<List<BasicCategory>>

    /**
     * Asynchronously deletes all Instances with the given ids
     *
     * @param toDelete list of Instance ids which should be deleted
     */
    fun deleteInstancesByIds(toDelete: List<Long>)

    /**
     * Loads all available AnimalCategory objects as BasicCategory each.
     *
     * @return instantly returns a LiveData element with
     *          List<BasicCategorization> as type parameter.
     *          The loading process itself runs in a background
     *          thread and fills the returned LiveData
     *          element upon finish.
     */
    fun loadAllAvailableAnimalCategoriesAsBasicCategory(): LiveData<List<BasicCategory>>
}