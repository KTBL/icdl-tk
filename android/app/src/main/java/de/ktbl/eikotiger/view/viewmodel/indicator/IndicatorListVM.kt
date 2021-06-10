package de.ktbl.eikotiger.view.viewmodel.indicator

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.liveData
import de.ktbl.android.sharedlibrary.view.viewmodel.AbstractBaseViewModel
import de.ktbl.eikotiger.data.icdl.indicator.Indicator
import de.ktbl.eikotiger.view.datainterface.indicator.IIndicatorListDA
import timber.log.Timber
import java.util.*
import javax.inject.Inject

class IndicatorListVM @Inject constructor(
        private val repository: IIndicatorListDA
) : AbstractBaseViewModel() {
    private val branchID = MutableLiveData<Long>()
    private var indicatorList = Transformations.switchMap(branchID) {
        repository.loadAllIndicatorsByBranchID(it)
    }
    private var indicatorListItems = Transformations.switchMap(indicatorList) {
        indicatorsToListItems(it)
    }

    fun setBranchID(branchID: Long) {
        Timber.tag(TAG)
                .v("Set branchID = $branchID")
        if (branchID != this.branchID.value) { //filter has changed
            this.branchID.value = branchID
        }
    }

    private fun indicatorsToListItems(indicators: List<Indicator>): LiveData<List<IndicatorListItem>> {
        return liveData {
            Timber.tag(TAG)
                    .i("Converting indicators to title-description-items.")
            val listItems: MutableList<IndicatorListItem> = ArrayList()
            if (indicators.isNotEmpty()) {
                for (indicator in indicators) {
                    listItems.add(IndicatorListItem(indicator.name,
                                                    indicator.description,
                                                    indicator.id,
                                                    80))
                }
                emit(listItems)
            }
        }
    }

    fun getIndicatorListItems(): LiveData<List<IndicatorListItem>> {
        Timber.tag(TAG)
                .v("Set list for indicators of branch with ID=${branchID.value}")
        return indicatorListItems
    }

    companion object {
        private val TAG = IndicatorListVM::class.java.simpleName
    }
}