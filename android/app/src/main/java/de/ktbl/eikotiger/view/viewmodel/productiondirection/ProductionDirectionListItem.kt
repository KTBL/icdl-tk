package de.ktbl.eikotiger.view.viewmodel.productiondirection

import de.ktbl.android.sharedlibrary.annotation.mock.Mock
import de.ktbl.android.sharedlibrary.view.adapter.Clickable
import de.ktbl.android.sharedlibrary.view.viewmodel.AbstractBaseViewModel
import javax.inject.Inject

typealias OnProdDirListItemSelectedCallback = (ProductionDirectionListItem) -> Unit

class ProductionDirectionListItem @Inject constructor() : AbstractBaseViewModel(), Clickable {
    @Mock(upperValueBound = 2)
    var category: String? = null
        private set

    @Mock(lowerValueBound = 3, upperValueBound = 5)
    var customDescription: String? = null
        private set
    private var clickEnabled = true
    var id: Long? = null
        private set
    var uuid: String? = null
        private set

    @Mock(upperValueBound = 2)
    var productionDirectionName: String? = null
        private set

    var onClickCallback: OnProdDirListItemSelectedCallback? = null

    constructor(uuid: String,
                productionDirectionName: String,
                customDescription: String,
                category: String,
                callback: OnProdDirListItemSelectedCallback?) : this() {
        this.uuid = uuid
        this.productionDirectionName = productionDirectionName
        this.customDescription = customDescription
        this.category = category
        this.onClickCallback = callback
    }

    constructor(id: Long?,
                productionDirectionName: String,
                customDescription: String,
                category: String,
                callback: OnProdDirListItemSelectedCallback?) : this() {
        this.id = id
        this.productionDirectionName = productionDirectionName
        this.customDescription = customDescription
        this.category = category
        this.onClickCallback = callback
    }

    override fun enableClick(enabled: Boolean) {
        clickEnabled = enabled
    }

    //This provides the callback - sg
    fun showDetails() {
        if (!clickEnabled) {
            return
        }
        onClickCallback?.invoke(this)

    }

    companion object {
        private val TAG = ProductionDirectionListItem::class.java.simpleName
    }
}