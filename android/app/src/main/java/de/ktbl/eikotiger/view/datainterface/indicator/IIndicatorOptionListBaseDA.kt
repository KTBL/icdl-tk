package de.ktbl.eikotiger.view.datainterface.indicator

import androidx.lifecycle.LiveData
import de.ktbl.eikotiger.data.icdl.indicator.Indicator
import de.ktbl.eikotiger.data.icdl.indicator.Var

/**
 * Data Access Interface for [de.ktbl.eikotiger.view.viewmodel.indicator.IndicatorOptionListBaseVM]
 * and subclasses
 *
 * @see de.ktbl.eikotiger.view.viewmodel.indicator.IndicatorOptionListBaseVM
 * @see de.ktbl.eikotiger.view.viewmodel.indicator.IndicatorOptionsOverviewVM
 */
interface IIndicatorOptionListBaseDA {

    /**
     * Load all Options associated with an Indicator identified via a given id
     *
     * @param id id of the Indicator load all Options of
     * @return instantly returns a LiveData element with
     *          List<Options> as type parameter.
     *          The loading process itself runs in a background
     *          thread and fills the returned LiveData
     *          element upon finish.
     */
    fun loadOptionsByIndicatorId(id: Long): LiveData<List<Var>>

    /**
     * Load an Indicator identified by the given ID
     */
    fun loadIndicatorById(id: Long): LiveData<Indicator>

    /**
     * Return the Folder Key for used identified Queried using the indicator ID
     */
    suspend fun getFolderKeyByIndicatorId(id: Long): String

}