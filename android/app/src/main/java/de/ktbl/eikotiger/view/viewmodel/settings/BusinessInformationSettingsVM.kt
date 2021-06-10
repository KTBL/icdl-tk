package de.ktbl.eikotiger.view.viewmodel.settings

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.viewModelScope
import de.ktbl.android.sharedlibrary.navigation.NavigationCommand
import de.ktbl.android.sharedlibrary.view.viewmodel.AbstractBaseViewModel
import de.ktbl.eikotiger.data.mastermodel.company.Company
import de.ktbl.eikotiger.di.Injectable
import de.ktbl.eikotiger.view.datainterface.settings.IBusinessInformationSettingsDA
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

/**
 * ViewModel to be used with [de.ktbl.eikotiger.view.fragment.settings.BusinessInfoFragment]
 */
class BusinessInformationSettingsVM @Inject constructor(private var businessInformationDA: IBusinessInformationSettingsDA) : AbstractBaseViewModel(), Injectable {

    var additionalInformation: MediatorLiveData<String?>? = null
    var businessAddress: MediatorLiveData<String?>? = null
    var businessName: MediatorLiveData<String?>? = null
    var editorName: MediatorLiveData<String?>? = null
    var phoneNumber: MediatorLiveData<String?>? = null
    var company: LiveData<Company>? = null


    init {
        Timber.tag(TAG)
                .d("Init ViewModel! ")
        businessName = MediatorLiveData()
        businessAddress = MediatorLiveData()
        editorName = MediatorLiveData()
        additionalInformation = MediatorLiveData()
        phoneNumber = MediatorLiveData()
    }

    override fun saveDataToDB() {
        viewModelScope.launch {
            val updatedCompany = Company()
            if (company == null) {
                Timber.tag(TAG).wtf("Somehow no company is set!")
                return@launch
            }
            updatedCompany.id = company!!.value?.id
            updatedCompany.companyName = businessName!!.value
            updatedCompany.note = additionalInformation!!.value
            updatedCompany.address = businessAddress!!.value
            updatedCompany.ownerName = editorName!!.value
            updatedCompany.phone = phoneNumber!!.value
            businessInformationDA.update(updatedCompany)
        }
    }

    override fun loadDataFromDB(): Boolean {
        company = businessInformationDA.defaultCompany

        // Add Observers to the variables and copy their value
        businessName!!.addSource(company!!) { company: Company? ->
            if (company != null && businessName!!.value != company.companyName) {
                businessName!!.postValue(company.companyName)
            }
        }
        businessAddress!!.addSource(company!!) { company: Company? ->
            if (company != null && businessAddress!!.value != company.address) {
                businessAddress!!.postValue(company.address)
            }
        }
        editorName!!.addSource(company!!) { company: Company? ->
            if (company != null && editorName!!.value != company.ownerName) {
                editorName!!.postValue(company.ownerName)
            }
        }
        additionalInformation!!.addSource(company!!) { company: Company? ->
            if (company != null && additionalInformation!!.value != company.note) {
                additionalInformation!!.postValue(company.note)
            }
        }
        phoneNumber!!.addSource(company!!) { company: Company? ->
            if (company != null && phoneNumber!!.value != company.phone) {
                phoneNumber!!.postValue(company.phone)
            }
        }
        return true
    }

    fun onCancelClicked() {
        navigationEventHandler.notifyLiveEvent(NavigationCommand())
    }

    fun onSaveClicked() {
        //Assumes two way binding works and current fiels are correctly filles with user choice
        saveDataToDB()
        navigationEventHandler.notifyLiveEvent(NavigationCommand())
    }

    companion object {
        private val TAG = BusinessInformationSettingsVM::class.java.simpleName
    }
}