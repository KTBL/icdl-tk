package de.ktbl.eikotiger.view.viewmodel.indicator

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import de.ktbl.android.sharedlibrary.navigation.NavigationCommand
import de.ktbl.android.sharedlibrary.view.viewmodel.AbstractBaseViewModel
import de.ktbl.eikotiger.view.datainterface.indicator.IIndicatorInfoDA
import de.ktbl.eikotiger.view.fragment.indicator.IndicatorInfoFragmentDirections
import de.ktbl.eikotiger.view.fragment.indicator.IndicatorOptionListFragmentMode
import timber.log.Timber
import javax.inject.Inject

class IndicatorInfoVM @Inject constructor(repository: IIndicatorInfoDA) : AbstractBaseViewModel() {

    companion object {
        val TAG = IndicatorInfoVM::class.java.simpleName
    }

    private val _indicatorId: MutableLiveData<Long> = MutableLiveData()
    val indicatorId: LiveData<Long> get() = _indicatorId

    val indicatorICDL = Transformations.switchMap(indicatorId) {
        repository.loadIndicatorById(it)
    }

    // This is built from different fields
    // e.g. the Video references
    var furtherInformation = ""

    fun loadData(indicatorID: Long, mockData: Boolean) {
        if (mockData) {
            Timber.tag(TAG)
                    .i("Mocked data has been requested")
            this.loadMockedData()
            return
        } else {
            if (_indicatorId.value != indicatorID)
                _indicatorId.postValue(indicatorID)
            Timber.tag(TAG)
                    .d("Load data for indicator id %s", indicatorID)
        }

    }

    fun onShowOptions() {
        val action = IndicatorInfoFragmentDirections.actionIndicatorInfoFragmentToIndicatorOptionList(
            IndicatorOptionListFragmentMode.OVERVIEW,
            indicatorId.value!!
        )
        //action.mock = true changed for testing -sg
        this.navigationEventHandler.notifyLiveEvent(NavigationCommand(action))
    }

}