package de.ktbl.eikotiger.data.apprepository

import de.ktbl.eikotiger.data.icdl.indicator.IndicatorDao
import de.ktbl.eikotiger.data.icdl.indicator.VarDao
import de.ktbl.eikotiger.view.datainterface.indicator.IIndicatorOptionsOverviewDA
import javax.inject.Inject

class IndicatorOptionsOverviewRepository @Inject constructor(
        private val indicatorDao: IndicatorDao,
        private val varDao: VarDao
) : IIndicatorOptionsOverviewDA, IndicatorOptionListBaseRepository(indicatorDao, varDao)