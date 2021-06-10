package de.ktbl.eikotiger.data.apprepository

import androidx.lifecycle.LiveData
import de.ktbl.eikotiger.data.mastermodel.instance.Instance
import de.ktbl.eikotiger.data.mastermodel.instance.InstanceDao
import de.ktbl.eikotiger.data.mastermodel.stock.StockDao
import de.ktbl.eikotiger.data.mastermodel.stock.StockWithBranch
import de.ktbl.eikotiger.view.datainterface.recording.IRecordingEnvironmentDA
import javax.inject.Inject

class RecordingEnvironmentRepository @Inject constructor(
        private val instanceDao: InstanceDao,
        private val stockDao: StockDao
) : IRecordingEnvironmentDA {
    override fun loadInstanceById(id: Long): LiveData<Instance> {
        return instanceDao.load(id)
    }

    override fun loadStockWithBranch(id: Long): LiveData<StockWithBranch> {
        return stockDao.loadStockWithBranchById(id)
    }
}