package de.ktbl.eikotiger.data.apprepository

import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import de.ktbl.eikotiger.data.icdl.animalcategory.AnimalCategory
import de.ktbl.eikotiger.data.icdl.animalcategory.AnimalCategoryDao
import de.ktbl.eikotiger.data.mastermodel.instance.Instance
import de.ktbl.eikotiger.data.mastermodel.instance.InstanceDao
import de.ktbl.eikotiger.data.mastermodel.instance.InstanceWithStocksAndBranches
import de.ktbl.eikotiger.data.mastermodel.stock.Stock
import de.ktbl.eikotiger.data.mastermodel.stock.StockDao
import de.ktbl.eikotiger.view.datainterface.productiondirection.IProductionDirectionDetailsDA
import timber.log.Timber
import javax.inject.Inject


class ProductionDirectionDetailsRepository
@Inject constructor(private val animalCategoryDao: AnimalCategoryDao,
                    private val instanceDao: InstanceDao,
                    private val stockDao: StockDao) : IProductionDirectionDetailsDA {

    companion object {
        val TAG = ProductionDirectionDetailsRepository::class.simpleName
    }

    override fun loadInstanceWithStocksAndBranches(id: Long): LiveData<InstanceWithStocksAndBranches> = instanceDao.loadInstanceWithStocksAndBranches(
            id)


    override fun loadAnimalCategory(id: Long): LiveData<AnimalCategory> = animalCategoryDao.loadById(
            id)

    @SuppressLint("BinaryOperationInTimber")
    override suspend fun initializeNewInstance(animalCategoryId: Long): Long {
        val animalCategoryWithBranches = animalCategoryDao.getWithBranchesById(animalCategoryId)
        val newInstance = Instance.createFromAnimalCategory(animalCategoryWithBranches.animalCategory)
        var ids = instanceDao.insert(newInstance)
        if (ids.size != 1) {
            Timber.tag(TAG).e("Somehow inserting a new Instance failed. Returning -1")
            return -1
        }
        newInstance.id = ids[0]
        val stocks = mutableListOf<Stock>()
        for (branch in animalCategoryWithBranches.branches) {
            val newStock = Stock()
            newStock.branchId = branch.id
            newStock.instanceId = newInstance.id
            stocks.add(newStock)
        }
        ids = stockDao.insert(*stocks.toTypedArray())
        if (ids.size != stocks.size) {
            Timber.tag(TAG)
                    .e("Not all new Stocks could be inserted (${ids.size} out of ${stocks.size}). " +
                               "Aborting and undoing instance creation")
            instanceDao.delete(newInstance)
            return -1
        }
        Timber.tag(TAG).v("Created a new Instance of AnimalCategory = ${animalCategoryWithBranches.animalCategory.name}, " +
                                  "assigned id = ${newInstance.id} and created ${ids.size} Stocks.")

        return newInstance.id
    }

    override suspend fun updateInstance(instance: Instance): Boolean {
        val updated = instanceDao.update(instance)
        return updated == 1
    }

}