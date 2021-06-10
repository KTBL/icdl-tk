package de.ktbl.eikotiger.data.apprepository

import androidx.lifecycle.LiveData
import de.ktbl.eikotiger.data.icdl.indicator.Indicator
import de.ktbl.eikotiger.data.icdl.indicator.IndicatorDao
import de.ktbl.eikotiger.view.datainterface.indicator.IIndicatorListDA
import javax.inject.Inject

class IndicatorListRepository @Inject constructor(
        private val indicatorDao: IndicatorDao
) : IIndicatorListDA {
    override fun loadAllIndicatorsByBranchID(branchID: Long): LiveData<List<Indicator>> {
        return indicatorDao.loadByBranchId(branchID)
    }
}