package de.ktbl.eikotiger.data.apprepository

import androidx.lifecycle.LiveData
import de.ktbl.eikotiger.data.mastermodel.instance.InstanceDao
import de.ktbl.eikotiger.data.mastermodel.instance.InstanceWithStocksAndBranches
import de.ktbl.eikotiger.view.datainterface.recording.IChooseAnimalBranchDialogDA
import javax.inject.Inject

class ChooseAnimalBranchDialogRepository @Inject constructor(
        private val instanceDao: InstanceDao
) : IChooseAnimalBranchDialogDA {
    override fun loadInstanceWithStockAndBranches(id: Long): LiveData<InstanceWithStocksAndBranches> {
        return instanceDao.loadInstanceWithStocksAndBranches(id)
    }
}