package de.ktbl.eikotiger.view.datainterface.indicator

import androidx.lifecycle.LiveData
import de.ktbl.eikotiger.data.icdl.indicator.Indicator

/**
 * Data Access Interface for [de.ktbl.eikotiger.view.viewmodel.indicator.IndicatorInfoVM]
 */
interface IIndicatorInfoDA {

    /**
     * Loads an Indicator object identified by a given id.
     *
     * @param id id of the Indicator object to load
     * @return instantly returns a LiveData element with
     *          Indicator as type parameter.
     *          The loading process itself runs in a background
     *          thread and fills the returned LiveData
     *          element upon finish.
     */
    fun loadIndicatorById(id: Long): LiveData<Indicator>
}