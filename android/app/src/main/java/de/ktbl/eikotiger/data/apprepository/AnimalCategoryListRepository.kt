package de.ktbl.eikotiger.data.apprepository

import androidx.lifecycle.LiveData
import de.ktbl.eikotiger.data.icdl.animalcategory.AnimalCategoryDao
import de.ktbl.eikotiger.data.mastermodel.instance.BasicCategory
import de.ktbl.eikotiger.data.mastermodel.instance.InstanceDao
import de.ktbl.eikotiger.view.datainterface.productiondirection.IDirListBaseDA
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AnimalCategoryListRepository @Inject constructor(
        private val animalCategoryDAO: AnimalCategoryDao,
        private val instanceDAO: InstanceDao
) : IDirListBaseDA {


    override fun loadAllAvailableInstancesAsBasicCategory(): LiveData<List<BasicCategory>> {
        return instanceDAO.loadAllBasicCategories()
    }

    override fun deleteInstancesByIds(toDelete: List<Long>) {
        TODO("To be implemented!")
    }

    override fun loadAllAvailableAnimalCategoriesAsBasicCategory(): LiveData<List<BasicCategory>> {
        return animalCategoryDAO.loadAllBasicCategories()
    }
}