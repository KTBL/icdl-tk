package de.ktbl.eikotiger.view.viewmodel.productiondirection

import androidx.lifecycle.LiveData
import de.ktbl.android.sharedlibrary.navigation.NavigationCommand
import de.ktbl.eikotiger.data.apprepository.AnimalCategoryListRepository
import de.ktbl.eikotiger.data.mastermodel.instance.BasicCategory
import de.ktbl.eikotiger.view.fragment.productiondirection.ProductionDirectionsOverviewFragmentDirections
import de.ktbl.eikotiger.view.fragment.productiondirection.ProductionDirectionsOverviewFragmentMode
import timber.log.Timber
import java.util.*
import javax.inject.Inject

open class DirListModeVM @Inject constructor(dataAccessRepository: AnimalCategoryListRepository) : DirectionListBaseVM() {

    companion object {
        val TAG = DirListModeVM::class.java.simpleName
    }

    private val activeCategoryList: LiveData<List<BasicCategory>>

    init {
        Timber.tag(TAG).d("initializing DirListModeVM")
        isFABVisible.value = true
        Timber.tag(TAG).d("initiating activeCategoryList")
        activeCategoryList = dataAccessRepository.loadAllAvailableInstancesAsBasicCategory()
        prodDirListItems.addSource(activeCategoryList) {
            updateProductionDirectionListItemsAsync(it)
        }
    }

    override fun basicCategoryToListItem(element: BasicCategory): ProductionDirectionListItem {
        return ProductionDirectionListItem(element.id,
                                           element.description ?: "",
                                           element.name ?: "",
                                           element.species ?: "",
                                           this::onListItemSelected)
    }

    fun deleteProdDirInstances(toDelete: ArrayList<ProductionDirectionListItem>) {
        //As elemennts of Type [VMName]ListItem they must be translated to some POJO Room is able to recognisze and project to a known entity
        if (toDelete.isNotEmpty()) { //we cannot uninstall presets
            val deleteMap: List<Long> = toDelete.map { it.id!! }
            Timber.tag(TAG)
                    .v("Requested to delete %s items", toDelete.size)
//            icdlDataAccessRepository.deleteInstances(deleteMap)
        }
    }

    override fun onFABClicked() {
        val action = ProductionDirectionsOverviewFragmentDirections.actionProdDirListAddingMode(
            ProductionDirectionsOverviewFragmentMode.ADDING_MODE
        )
        navigationEventHandler.notifyLiveEvent(NavigationCommand(action))
    }




}