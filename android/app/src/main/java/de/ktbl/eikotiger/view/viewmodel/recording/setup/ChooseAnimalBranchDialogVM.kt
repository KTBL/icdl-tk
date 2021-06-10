package de.ktbl.eikotiger.view.viewmodel.recording.setup

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import de.ktbl.android.sharedlibrary.view.viewmodel.AbstractBaseViewModel
import de.ktbl.eikotiger.data.mastermodel.instance.InstanceWithStocksAndBranches
import de.ktbl.eikotiger.view.datainterface.recording.IChooseAnimalBranchDialogDA
import de.ktbl.eikotiger.view.viewmodel.productiondirection.SimpleAnimalCategoryBranchListItem
import javax.inject.Inject

/**
 *
 * @property instanceID
 * @property instanceWithStocksAndBranches
 */
class ChooseAnimalBranchDialogVM @Inject constructor(
        private val repository: IChooseAnimalBranchDialogDA
) : AbstractBaseViewModel() {

    var instanceID: MutableLiveData<Long> = MutableLiveData()
    var instanceWithStocksAndBranches = Transformations.switchMap(instanceID) {
        repository.loadInstanceWithStockAndBranches(it)
    }

    var branchItems = Transformations.map(instanceWithStocksAndBranches, this::createBranchItems)

    private fun createBranchItems(instanceWithStocksAndBranches: InstanceWithStocksAndBranches):
            List<SimpleAnimalCategoryBranchListItem> {
        val listItems = arrayListOf<SimpleAnimalCategoryBranchListItem>()
        val stockWithBranches = instanceWithStocksAndBranches.stockWithBranches
        for (element in stockWithBranches) {
            listItems += SimpleAnimalCategoryBranchListItem(element)
        }
        return listItems
    }

}