package de.ktbl.eikotiger.data.mastermodel.company.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import de.ktbl.eikotiger.data.mastermodel.company.Company
import de.ktbl.eikotiger.data.mastermodel.company.CompanyDao
import de.ktbl.eikotiger.view.datainterface.settings.IBusinessInformationSettingsDA
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

/**
 * separation of concerns
 *
 */
@Singleton
class CompanyRepository @Inject internal constructor(private val companyDao: CompanyDao) : IBusinessInformationSettingsDA {

    /**
     * Provides access to the default company. The element provided in here is not updated
     */
    override val defaultCompany: LiveData<Company>
        get() = liveData {
            val companies = companyDao.loadAll()
            if (companies.isEmpty()) {
                Timber.tag(TAG).wtf("Somehow not even a single company exists! How? Time to crash the app!")
                throw IllegalStateException("No company exists!")
            }
            emit(companies[0])
        }


    override suspend fun update(company: Company) {
        companyDao.update(company)
    }

    fun delete(@Suppress("UNUSED_PARAMETER") company: Company) {
        TODO("Yet not implemented, since no company ever should be deleted yet.")
    }

    companion object {
        const val TAG = "CompanyRepository"
    }
}