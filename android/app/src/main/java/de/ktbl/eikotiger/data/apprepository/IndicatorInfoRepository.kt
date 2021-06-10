package de.ktbl.eikotiger.data.apprepository

import androidx.lifecycle.LiveData
import de.ktbl.eikotiger.data.icdl.indicator.Indicator
import de.ktbl.eikotiger.data.icdl.indicator.IndicatorDao
import de.ktbl.eikotiger.view.datainterface.indicator.IIndicatorInfoDA
import javax.inject.Inject

class IndicatorInfoRepository @Inject constructor(
        private val indicatorDao: IndicatorDao
) : IIndicatorInfoDA {
    override fun loadIndicatorById(id: Long): LiveData<Indicator> = indicatorDao.loadById(id)
}