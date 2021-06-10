package de.ktbl.eikotiger.view.datainterface.recording

import androidx.lifecycle.LiveData
import de.ktbl.eikotiger.data.mastermodel.instance.Instance
import de.ktbl.eikotiger.data.mastermodel.stock.StockWithBranch

/**
 * Data Access Interface for the viewmodel RecordingEnvironment
 *
 * @see de.ktbl.eikotiger.view.viewmodel.recording.manager.RecordingEnvironment
 */
interface IRecordingEnvironmentDA {
    /**
     * Loads a Instance identified via a given Instance id
     *
     * @param id id of the AnimalCategory Instance needs to be to loaded
     * @return instantly returns a LiveData element with String as
     *          type parameter. The loading process itself runs in a
     *          background thread and fills the returned LiveData
     *          element upon finish.
     */
    fun loadInstanceById(id: Long): LiveData<Instance>

    /**
     * Loads a StockWithBranch element identified by the stock ID
     *
     * @param id id of the Stock to be loaded
     * @return instantly returns a LiveData element, to be filled
     *          with the queried element
     */
    fun loadStockWithBranch(id: Long): LiveData<StockWithBranch>

}