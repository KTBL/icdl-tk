package de.ktbl.eikotiger.view.viewmodel.indicator

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.liveData
import com.android.example.github.util.getOrAwaitValue
import de.ktbl.android.sharedlibrary.view.viewmodel.AbstractBaseViewModel
import de.ktbl.eikotiger.data.icdl.indicator.Indicator
import de.ktbl.eikotiger.data.icdl.indicator.Var
import de.ktbl.eikotiger.view.datainterface.indicator.IIndicatorOptionListBaseDA
import timber.log.Timber
import javax.inject.Provider

/**
 * To be used with {@link IndicatorOptionList}
 * This is the base ViewModel for the list view of options in an classification style.
 * Either the context this VM is used in is the recording of indicators (thus only if classifcation
 * indicators are recorded) or in case an overview of all available indicator options (classification,
 * and non classification) is given.
 */

open class IndicatorOptionListBaseVM constructor(private val repository: IIndicatorOptionListBaseDA,
                                                 private val optionChoosable: Boolean,
                                                 private val optionVMProvider: Provider<ClassificationOptionVM>
) : AbstractBaseViewModel() {
    companion object {
        val TAG = IndicatorOptionListBaseVM::class.java.simpleName
        val taggedTimber = Timber.tag(TAG)
    }

    protected val _isProcessing = MutableLiveData(false)
    val isProcessing: LiveData<Boolean>
        get() = _isProcessing

    protected open val _indicatorId = MutableLiveData<Long>()
    val indicatorId: LiveData<Long> get() = _indicatorId

    lateinit var indicatorInstance: LiveData<Indicator>

    lateinit var description: LiveData<String>

    lateinit var name: LiveData<String>

    private lateinit var optionsICDL: LiveData<List<Var>>

    lateinit var optionItems: LiveData<List<ClassificationOptionVM>>

    val showForwardButtons = MutableLiveData(false)

    /**
     * This function initializes the LiveData properties provided by this ViewModel.
     * This is done here, because _indicatorId may be overriden by subclasses, which would
     * possibly lead to a wrong initialization of the other properties. Therefore, we do this here
     * once the class is initialized.
     * see: https://kotlinlang.org/docs/inheritance.html#overriding-rules
     */
    protected fun initializeLiveData() {
        indicatorInstance = Transformations.switchMap(indicatorId) {
            repository.loadIndicatorById(it)
        }

        description = Transformations.map(indicatorInstance) {
            it.description
        }

        name = Transformations.map(indicatorInstance) {
            it.name
        }

        optionsICDL = Transformations.switchMap(indicatorId) {
            repository.loadOptionsByIndicatorId(it)
        }

        optionItems = Transformations.switchMap(optionsICDL) {
            loadOptionList(it)
        }
    }

    private fun loadOptionList(options: List<Var>): LiveData<List<ClassificationOptionVM>> = liveData {
        val vmItems: MutableList<ClassificationOptionVM> = mutableListOf()
        val folderKey = repository.getFolderKeyByIndicatorId(indicatorId.getOrAwaitValue())
        options.forEach { option ->
            val item: ClassificationOptionVM? = optionVMProvider.get()
            item?.postOption(option, folderKey, this@IndicatorOptionListBaseVM::onOptionSelected)
            item?.selectOptionEnabled?.value = optionChoosable
            if (item != null) {
                vmItems.add(item)
            }
            Timber.tag(IndicatorOptionsOverviewVM.TAG)
                    .d("Load Option %s", option.toString())
        }
        Timber.tag(IndicatorOptionsOverviewVM.TAG)
                .d("Options %s", vmItems.toString())
        emit(vmItems)
    }

    open fun loadData(indicatorID: Long) {
        initializeLiveData()
        _indicatorId.value = indicatorID
        taggedTimber
            .d("Load options for indicator id %s", indicatorID)

    }

    open fun onOptionSelected(option: Var) {

    }

    open fun onForward() {

    }

    open fun onFastForward() {

    }

    open fun clear() {

    }
}