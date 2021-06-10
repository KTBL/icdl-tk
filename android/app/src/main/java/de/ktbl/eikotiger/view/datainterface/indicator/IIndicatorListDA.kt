package de.ktbl.eikotiger.view.datainterface.indicator

import androidx.lifecycle.LiveData
import de.ktbl.eikotiger.data.icdl.indicator.Indicator

/**
 * Data Access Interface for [de.ktbl.eikotiger.view.viewmodel.indicator.IndicatorListVM]
 */
interface IIndicatorListDA {
    /**
     * TODO: Question see: http://192.168.200.21:3000/EiKoTiGer/EiKoTiGer-Android/issues/13#issuecomment-289
     * Loads all available Indicators for a given Section identified via id
     *
     * @param branchID identifier of the associated section
     * @return instantly returns a LiveData element with
     *          List<Indicator> as type parameter.
     *          The loading process itself runs in a background
     *          thread and fills the returned LiveData
     *          element upon finish.
     *
     */
    fun loadAllIndicatorsByBranchID(branchID: Long): LiveData<List<Indicator>>
}