package de.ktbl.eikotiger.view.datainterface.indicator

import androidx.lifecycle.LiveData
import de.ktbl.eikotiger.data.icdl.indicator.Var

interface IIndicatorOptionDetailsDA {

    /**
     *
     */
    fun loadOptionById(id: Long): LiveData<Var>

    suspend fun getFolderKeyByOptionId(optionId: Long): String
}