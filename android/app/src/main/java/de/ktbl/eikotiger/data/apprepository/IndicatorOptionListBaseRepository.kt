package de.ktbl.eikotiger.data.apprepository

import androidx.lifecycle.LiveData
import de.ktbl.eikotiger.data.icdl.indicator.Indicator
import de.ktbl.eikotiger.data.icdl.indicator.IndicatorDao
import de.ktbl.eikotiger.data.icdl.indicator.Var
import de.ktbl.eikotiger.data.icdl.indicator.VarDao
import de.ktbl.eikotiger.view.datainterface.indicator.IIndicatorOptionListBaseDA
import javax.inject.Inject

open class IndicatorOptionListBaseRepository @Inject constructor(
        private val indicatorDao: IndicatorDao,
        private val varDao: VarDao
) : IIndicatorOptionListBaseDA {
    override fun loadOptionsByIndicatorId(id: Long): LiveData<List<Var>> {
        return varDao.loadOptionsByIndicatorId(id)
    }

    override fun loadIndicatorById(id: Long): LiveData<Indicator> {
        return indicatorDao.loadById(id)
    }

    override suspend fun getFolderKeyByIndicatorId(id: Long): String {
        return indicatorDao.getFolderKeyById(id)
    }
}