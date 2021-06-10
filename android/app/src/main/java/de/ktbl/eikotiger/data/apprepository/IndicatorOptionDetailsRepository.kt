package de.ktbl.eikotiger.data.apprepository

import androidx.lifecycle.LiveData
import de.ktbl.eikotiger.data.icdl.indicator.Var
import de.ktbl.eikotiger.data.icdl.indicator.VarDao
import de.ktbl.eikotiger.view.datainterface.indicator.IIndicatorOptionDetailsDA
import javax.inject.Inject

class IndicatorOptionDetailsRepository @Inject constructor(
        private val varDao: VarDao
) : IIndicatorOptionDetailsDA {
    override fun loadOptionById(id: Long): LiveData<Var> {
        return varDao.loadOptionById(id)
    }

    override suspend fun getFolderKeyByOptionId(optionId: Long): String {
        return varDao.getFolderKeyById(optionId)
    }

}