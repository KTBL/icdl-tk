package de.ktbl.eikotiger.view.viewmodel.productiondirection

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import de.ktbl.android.sharedlibrary.AsyncTask
import de.ktbl.android.sharedlibrary.annotation.mock.MockCreator
import de.ktbl.android.sharedlibrary.navigation.NavigationCommand
import de.ktbl.android.sharedlibrary.view.viewmodel.AbstractBaseViewModel
import de.ktbl.eikotiger.BuildConfig
import de.ktbl.eikotiger.data.mastermodel.instance.BasicCategory
import de.ktbl.eikotiger.view.fragment.productiondirection.ProductionDirectionsOverviewFragmentDirections
import timber.log.Timber
import java.util.*

abstract class DirectionListBaseVM : AbstractBaseViewModel() {

    companion object {
        val TAG = DirectionListBaseVM::class.java.simpleName
    }

    val isFABVisible = MutableLiveData<Boolean>()
    val prodDirListItems = MediatorLiveData<List<ProductionDirectionListItem>>()
    var isMock = false

    open fun onFABClicked() {}

    protected fun updateProductionDirectionListItemsAsync(activeInstancees: List<BasicCategory>) {
        // kschneider:
        //  Below the way params is passed is needed, since params is a var-arg parameter (see
        //  AsyncTask!)
        //  Since we have exactly ONE argument every time this function is called,
        //  updateAsyncTask we simply take out the first part. If this fails, it's a hard programmer
        //  mistake, and therefore it should not be handled at runtime, but rather at development
        //  time!
        val updateAsyncTask = AsyncTask<List<BasicCategory>?, Void, Void?> { params: Array<List<BasicCategory>?> ->
            updateProductionDirectionListItems(
                    params[0])
            null
        }
        updateAsyncTask.execute(activeInstancees)
    }

    private fun updateProductionDirectionListItems(changedActiveInstancees: List<BasicCategory>?) {
        Timber.tag(TAG)
                .d("updating productionDirectionListItems")
        var updatedItemList: MutableList<ProductionDirectionListItem> = ArrayList()
        if (changedActiveInstancees != null && changedActiveInstancees.isNotEmpty()) {
            for (element in changedActiveInstancees) {
                val item: ProductionDirectionListItem = basicCategoryToListItem(element)

                Timber.tag(TAG)
                        .i("Load : ${item.productionDirectionName} - ${item.customDescription}")
                Timber.tag(TAG)
                        .d("Added productionDirectionListItem %s", item.productionDirectionName)
                updatedItemList.add(item)
            }
        } else if (BuildConfig.DEBUG && isMock) {
            Timber.tag(TAG)
                    .d("creating productionDirectionListItem mock content")
            updatedItemList = MockCreator.createMockList(ProductionDirectionListItem::class.java,
                                                         2,
                                                         5)
        }
        prodDirListItems.postValue(updatedItemList)
    }

    protected open fun onListItemSelected(item: ProductionDirectionListItem) {
        val action = ProductionDirectionsOverviewFragmentDirections.actionProdDirListToDetails(item.id!!)
        navigationEventHandler.notifyLiveEvent(NavigationCommand(action))
    }

    protected abstract fun basicCategoryToListItem(element: BasicCategory): ProductionDirectionListItem

}