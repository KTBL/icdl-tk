package de.ktbl.eikotiger.view.viewmodel.productiondirection

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import de.ktbl.android.sharedlibrary.annotation.mock.Mock
import de.ktbl.android.sharedlibrary.navigation.NavigationCommand
import de.ktbl.android.sharedlibrary.view.adapter.Clickable
import de.ktbl.android.sharedlibrary.view.viewmodel.AbstractBaseViewModel
import de.ktbl.eikotiger.data.icdl.animalcategory.Branch
import de.ktbl.eikotiger.data.mastermodel.stock.Stock
import de.ktbl.eikotiger.view.fragment.productiondirection.ProductionDirectionDetailFragmentDirections
import timber.log.Timber
import javax.inject.Inject

class ProductionDirectionSectionVM
@Inject internal constructor() : AbstractBaseViewModel(), Clickable {
    @JvmField
    @Mock(upperValueBound = 2)
    var sectionTitle = MediatorLiveData<String?>()

    @JvmField
    @Mock(lowerValueBound = 5, upperValueBound = 18)
    var sectionDescription = MediatorLiveData<String?>()

    @JvmField
    @Mock(type = Mock.Type.INT, lowerValueBound = 0, upperValueBound = 220)
    var sectionGroupSize = MediatorLiveData<Int>() //this is the input to store

    /*Id choice is only needed if we want to reuse this in standalone fragment activity*/
    private var sectionId: MediatorLiveData<Long?>? = null
    private val section = MutableLiveData<Stock>()

    //Refer to the instance by id for the time we do not have and realy unique unchangable identifier - sg
    private var instanceId: MediatorLiveData<Long?>? = MediatorLiveData()

    /**
     * Passes the full section description from the background
     * for imutable fields this can simply be copied as there is no version check or whatever
     *
     * @param branch
     * @param stock
     */
    fun postSection(branch: Branch, stock: Stock?) {
        if (stock != null) {
            if (instanceId == null) {
                instanceId = MediatorLiveData()
                instanceId!!.postValue(-0L)
            }
            if (sectionId == null) {
                sectionId = MediatorLiveData()
                sectionId!!.postValue(null)
            }
            /* if (number == null) {
               number = new MediatorLiveData<>();
               number.postValue(null);
            }*/
            @Suppress("UNNECESSARY_NOT_NULL_ASSERTION") // Even though its unnecessary android studio does not get it..
            section.postValue(stock!!)
            sectionTitle.postValue(branch.name)
            sectionDescription.postValue(branch.description)
            //This value is just cached to send it to ui, because animal Count is now calculated from group data
            sectionGroupSize.postValue(stock.animalCount)
            instanceId!!.postValue(stock.instanceId)
            //this.number.postValue(stock.instanceId);
            sectionId!!.postValue(stock.id)
            Timber.tag(TAG)
                    .d("Received Section data for %s", branch.__key)
            Timber.tag(TAG)
                    .d("Section id %s", stock.id)
            Timber.tag(TAG)
                    .v("Branch %s", branch.toString())
            Timber.tag(TAG)
                    .v("Stock %s", stock.toString())
        }
    }

    fun onNavigateToGroupList() {
        if (sectionId != null && sectionId!!.value != null) {
            Timber.tag(TAG)
                    .d("Navigation to the Group List for ID %s requested.", sectionId!!.value)
            val action = ProductionDirectionDetailFragmentDirections.actionProdDirDetailsFragmentToGroupList(
                    sectionId!!.value!!)
            action.sectionId = sectionId!!.value!!
            navigationEventHandler.notifyLiveEvent(NavigationCommand(action))
        } else {
            Timber.tag(TAG)
                    .e("No navigation target provided")
        }
    }

    fun onNavigateToIndicatorList() {
        if (sectionId!!.value != null && instanceId!!.value != null) {
            Timber.tag(TAG)
                    .d("Navigation to the Indicatorlist for ID %s requested.",
                       sectionId!!.value.toString())
            val action = ProductionDirectionDetailFragmentDirections.actionProdDirDetailsToIndicatorList(
                    section.value!!.branchId!!)
            navigationEventHandler.notifyLiveEvent(NavigationCommand(action))
        } else {
            Timber.tag(TAG)
                    .e("No navigation target provided")
        }
    }

    override fun enableClick(enabled: Boolean) {
        // nothing to do, thus empty
    }

    companion object {
        private val TAG = ProductionDirectionSectionVM::class.java.simpleName
    }
}