package de.ktbl.eikotiger.view.datainterface.recording

import androidx.lifecycle.LiveData
import de.ktbl.eikotiger.data.mastermodel.instance.InstanceWithStocksAndBranches

/**
 *
 * Data Access Interface for ChooseAnimalBranchDialogVM.
 */
interface IChooseAnimalBranchDialogDA {

    /**
     * Loads all StockByBranch elements associated to a given Instance
     * identified via a given id.
     *
     * @param id id of the Instance which StockByBranch elements
     * @return instantly returns a LiveData element with
     *          List<StockByBranch> as type parameter.
     *          The loading process itself runs in a background
     *          thread and fills the returned LiveData
     *          element upon finish.
     */
    fun loadInstanceWithStockAndBranches(id: Long): LiveData<InstanceWithStocksAndBranches>

}