package de.ktbl.eikotiger.view.datainterface.settings

import androidx.lifecycle.LiveData
import de.ktbl.eikotiger.data.mastermodel.company.Company

/**
 * Data Access Interface for [de.ktbl.eikotiger.view.viewmodel.settings.BusinessInformationSettingsVM]
 */
interface IBusinessInformationSettingsDA {

    /**
     *
     * Providing the default Company
     *
     * @return instantly returns a LiveData element with
     *          CompanyEdit as type parameter.
     *          The loading process itself runs in a background
     *          thread and fills the returned LiveData
     *          element upon finish.
     */
    val defaultCompany: LiveData<Company>

    /**
     * Asynchronously save the given Company as default Company
     *
     * @param company the object to save
     */
    suspend fun update(company: Company)
}