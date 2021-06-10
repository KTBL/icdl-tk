package de.ktbl.eikotiger.view.viewmodel.recording

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.viewModelScope
import de.ktbl.android.sharedlibrary.navigation.NavigationCommand
import de.ktbl.android.sharedlibrary.view.viewmodel.AbstractBaseViewModel
import de.ktbl.eikotiger.data.icdl.indicator.IndicatorWithAssessment
import de.ktbl.eikotiger.view.fragment.indicator.IndicatorOptionListFragmentMode
import de.ktbl.eikotiger.view.fragment.recording.RecordingOverviewFragmentDirections
import de.ktbl.eikotiger.view.fragment.recording.RecordingOverviewIndicatorListItem
import de.ktbl.eikotiger.view.viewmodel.recording.manager.RecordingMaster
import kotlinx.coroutines.launch
import javax.inject.Inject

class RecordingOverviewVM @Inject constructor() : AbstractBaseViewModel() {
    private lateinit var recordingMaster: RecordingMaster
    private lateinit var availableIndicators: LiveData<List<IndicatorWithAssessment>>
    lateinit var availableIndicatorItems: LiveData<List<RecordingOverviewIndicatorListItem>>
        private set


    private fun createIndicatorListItems(indicators: List<IndicatorWithAssessment>): List<RecordingOverviewIndicatorListItem> {
        val items = mutableListOf<RecordingOverviewIndicatorListItem>()
        for (indicator in indicators) {
            items.add(
                RecordingOverviewIndicatorListItem().apply {
                    this.id = indicator.indicator.id
                    this.description = indicator.indicator.description
                    this.title = indicator.indicator.name
                    this.onClickCallback = this@RecordingOverviewVM::onIndicatorChosen
                }
            )
        }
        return items
    }

    fun load(recordingMaster: RecordingMaster) {
        this.recordingMaster = recordingMaster
        this.availableIndicators = recordingMaster.bindable.availableIndicators
        this.availableIndicatorItems = Transformations.map(
            availableIndicators,
            this::createIndicatorListItems
        )
    }

    private fun onIndicatorChosen(indicatorId: Long) {
        viewModelScope.launch {
            recordingMaster.setActiveIndicator(indicatorId)
            val action = RecordingOverviewFragmentDirections.toIndicatorInput(
                IndicatorOptionListFragmentMode.INPUT_CLASSIFICATION,
                indicatorId
            )
            this@RecordingOverviewVM.navigationEventHandler.postNotifyLiveEvent(
                NavigationCommand(
                    action
                )
            )
        }
    }
}

