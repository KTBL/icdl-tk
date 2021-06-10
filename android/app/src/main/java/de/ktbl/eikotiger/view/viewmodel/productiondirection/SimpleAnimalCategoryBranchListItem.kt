package de.ktbl.eikotiger.view.viewmodel.productiondirection

import de.ktbl.android.sharedlibrary.view.adapter.Clickable
import de.ktbl.eikotiger.data.mastermodel.stock.StockWithBranch

typealias AnimalCategoryBranchItemSelectedCallback = (SimpleAnimalCategoryBranchListItem) -> Unit

class SimpleAnimalCategoryBranchListItem(stockWithBranches: StockWithBranch) : Clickable {
    private var clickable = true
    val stockID: Long
    var title = ""
    var description = ""
    var amountAnimals = 0
    var callback: AnimalCategoryBranchItemSelectedCallback? = null

    init {
        title = stockWithBranches.branch.name ?: ""
        description = stockWithBranches.branch.description ?: ""
        amountAnimals = stockWithBranches.stock.animalCount
        stockID = stockWithBranches.stock.id
    }

    override fun enableClick(enabled: Boolean) {
        clickable = enabled
    }

    fun onClick() {
        if (!clickable) {
            return
        }
        // Using invoke to be able to use elvis here.
        callback?.invoke(this)
    }
}