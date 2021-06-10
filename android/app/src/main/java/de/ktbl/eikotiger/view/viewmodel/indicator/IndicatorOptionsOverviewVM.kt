package de.ktbl.eikotiger.view.viewmodel.indicator

import de.ktbl.eikotiger.view.datainterface.indicator.IIndicatorOptionsOverviewDA
import javax.inject.Inject
import javax.inject.Provider

/**
 * To be Used within: IndicatorOptionList
 */
class IndicatorOptionsOverviewVM @Inject constructor(repository: IIndicatorOptionsOverviewDA,
                                                     optionVMProvider: Provider<ClassificationOptionVM>
) : IndicatorOptionListBaseVM(repository, false, optionVMProvider) {

    companion object {
        val TAG = IndicatorOptionsOverviewVM::class.java.simpleName
    }

}