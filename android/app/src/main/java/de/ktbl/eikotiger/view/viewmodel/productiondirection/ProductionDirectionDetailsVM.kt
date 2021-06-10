package de.ktbl.eikotiger.view.viewmodel.productiondirection

import androidx.lifecycle.*
import de.ktbl.android.sharedlibrary.view.viewmodel.AbstractBaseViewModel
import de.ktbl.eikotiger.data.icdl.animalcategory.AnimalCategory
import de.ktbl.eikotiger.data.mastermodel.instance.Instance
import de.ktbl.eikotiger.data.mastermodel.stock.StockWithBranch
import de.ktbl.eikotiger.di.Injectable
import de.ktbl.eikotiger.view.datainterface.productiondirection.IProductionDirectionDetailsDA
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.*
import javax.inject.Inject

/**
 * ViewModel used to provide data to show details of a production direction
 * ("Produktionsrichtungsdetails")
 */
class ProductionDirectionDetailsVM
@Inject constructor(private val repository: IProductionDirectionDetailsDA) : AbstractBaseViewModel(), Injectable {

    var instanceId: MutableLiveData<Long?> = MutableLiveData()
        private set


    var instanceWithStockAndBranches = Transformations.switchMap(instanceId) { instanceId: Long? ->
        if (instanceId != null && instanceId > 0L) {
            Timber.tag(TAG).d("Loading Instance with ID = $instanceId")
            return@switchMap repository.loadInstanceWithStocksAndBranches(instanceId)
        } else {
            return@switchMap null
        }
    }

    var animalCategory: LiveData<AnimalCategory> = Transformations.switchMap(
            instanceWithStockAndBranches) {

        if (it != null) { //if we do not have a valid e cannot go on
            Timber.tag(TAG)
                    .d("Query AnimalCategory of Instance ${it.instance.customName}")
            return@switchMap repository.loadAnimalCategory(it.instance.animalCategoryId)
        } else {
            return@switchMap null
        }
    }

    /**
     * Description of the "Produktionsrichtung". This description should be general and valid
     * for all(!) prod.dir.sections ("Produktionsrichtungsabschnitte").
     */
    var productionDescription = MediatorLiveData<String>() //new MutableLiveData<>("");

    /**
     * This variable holds the custom instance name given by the user to identify the
     * prod.dir instance.
     */
    var instanceName = MediatorLiveData<String>()

    /**
     * This is the name of the prod.dir. as given by the ICDL. Thus it cannot be changed by
     * the user.
     */
    var prodDirName = MediatorLiveData<String>()

    /**
     * The (sub)sections of the prod.direction are provided as a list. The data of these
     * sections is provided via their own VM.
     *
     * @see ProductionDirectionSectionVM
     */
    var sectionVMList: LiveData<List<ProductionDirectionSectionVM>> = Transformations.map(
            instanceWithStockAndBranches) {
        if (it == null) {
            listOf()
        } else {
            syncSections(it.stockWithBranches)
        }
    }


    init {
        prodDirName.addSource(animalCategory) {
            if (it != null) {
                prodDirName.value = it.name
            } else {
                prodDirName.value = ""
            }
        }

        productionDescription.addSource(animalCategory) {
            if (it != null) {
                productionDescription.value = it.description
            } else {
                productionDescription.value = ""
            }
        }

        instanceName.addSource(instanceWithStockAndBranches) {
            if (it != null) {
                instanceName.value = it.instance.customName ?: prodDirName.value
            } else {
                instanceName.value = ""
            }
        }
    }

    /**
     * Takes SubTypes and Stock Information add them to a single list for display elements
     *
     * @param animalStock
     */
    private fun syncSections(animalStock: List<StockWithBranch>): List<ProductionDirectionSectionVM> {
        Timber.tag(TAG).d("Start updating Section List items")
        val sections: MutableList<ProductionDirectionSectionVM> = ArrayList()
        if (animalStock.isNotEmpty()) {
            for (section in animalStock) {
                val item = ProductionDirectionSectionVM()
                Timber.tag(TAG)
                        .v("Sync items for branch %s", animalStock.toString())
                item.postSection(section.branch,
                                 section.stock) //first parameter ist the template, second the instance
                Timber.tag(TAG)
                        .i("Load Sections for: ${section.branch.name} (id: ${section.branch.__key} ) - ${section.stock.id})")
                sections.add(item)
            }
        }
        return sections
    }


    fun createAndLoadInstance(animalCategoryId: Long) {
        Timber.tag(TAG).i("Creating new Instance of AnimalCategory with ID=$animalCategoryId")
        viewModelScope.launch {
            val newId = repository.initializeNewInstance(animalCategoryId)
            Timber.tag(TAG).i("Newly created Instance has the ID=$newId")
            instanceId.postValue(newId)
        }
    }


    /**
     * Loads an existing Instance using the ID
     */
    fun loadInstance(instanceId: Long) { //assume element exist
        if (instanceId <= 0L) {
            Timber.tag(TAG).e("No valid instance id passed! Cannot load data")
            return
        }
        Timber.tag(TAG)
                .v("Load instance with id %s", instanceId)
        this.instanceId.value = instanceId
    }

    /**
     * Store the current ViewModel data fields to db
     *
     *
     * Copy to datamodel and update
     *
     *
     * Note this could be simplified to room update list, if we copy the user input somewhere else
     */
    fun save(): LiveData<Boolean> {
        val success = MutableLiveData<Boolean>()
        //Keep it here as we need to access VMs here
        viewModelScope.launch {
            val instance = instanceWithStockAndBranches.value?.instance
            if (instance != null && instance.id > 0) {
                val updateCopy = Instance()
                updateCopy.id = instance.id
                updateCopy.animalCategoryId = instance.animalCategoryId
                Timber.tag(TAG)
                        .v("Save current state of ${instance.id}. ${instance.customName}. Nr ${instance.id}")
                Timber.tag(TAG)
                        .d("Save input: %s", instanceName.value)
                updateCopy.customName = instanceName.value
                updateCopy.updated = Date()
                updateCopy.species = instance.species
                val updateSuccess = repository.updateInstance(updateCopy)
                success.postValue(updateSuccess)
            }
        }
        return success
    }

    companion object {
        private val TAG = ProductionDirectionDetailsVM::class.java.simpleName
    }
}