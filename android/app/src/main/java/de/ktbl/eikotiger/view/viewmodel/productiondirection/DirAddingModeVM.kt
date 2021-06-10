package de.ktbl.eikotiger.view.viewmodel.productiondirection

import androidx.lifecycle.LiveData
import de.ktbl.android.sharedlibrary.navigation.NavigationCommand
import de.ktbl.eikotiger.data.apprepository.AnimalCategoryListRepository
import de.ktbl.eikotiger.data.mastermodel.instance.BasicCategory
import de.ktbl.eikotiger.view.fragment.productiondirection.ProductionDirectionsOverviewFragmentDirections
import timber.log.Timber
import javax.inject.Inject

class DirAddingModeVM @Inject constructor(val repository: AnimalCategoryListRepository) : DirectionListBaseVM() {

    companion object {
        val TAG = DirAddingModeVM::class.java.simpleName
    }

    private val availableCategoriesList: LiveData<List<BasicCategory>>

    init {
        Timber.tag(TAG).d("initiating")
        isFABVisible.value = false
        availableCategoriesList = repository.loadAllAvailableAnimalCategoriesAsBasicCategory()
        prodDirListItems.addSource(availableCategoriesList) {
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

    override fun onListItemSelected(item: ProductionDirectionListItem) {
        val action = ProductionDirectionsOverviewFragmentDirections.actionProdDirListToDetails(item.id!!)
        action.create = true
        navigationEventHandler.notifyLiveEvent(NavigationCommand(action))
    }
}